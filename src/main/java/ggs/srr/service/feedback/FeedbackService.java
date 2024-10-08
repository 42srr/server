package ggs.srr.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import ggs.srr.domain.feedback.Feedback;
import ggs.srr.domain.feedback.FeedbackDetail;
import ggs.srr.domain.user.FtUser;
import ggs.srr.jwt.JWTUtil;
import ggs.srr.repository.feedback.CustomFeedbackRepository;
import ggs.srr.repository.feedback.FeedbackRepository;
import ggs.srr.service.system.exception.NotFoundAdminUserException;
import ggs.srr.service.user.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeedbackService{

    private final String apiUrl = "https://api.intra.42.fr/v2/feedbacks?filter[campus_id]=69";
    private final String NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE = "cannot found admin user";
    private final int page = 0;

    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final FeedbackRepository feedbackRepository;
    private final CustomFeedbackRepository customFeedbackRepository;
    
    @Autowired
    public FeedbackService(JWTUtil jwtUtil, RestTemplate restTemplate, ObjectMapper objectMapper, 
                         UserService userService, FeedbackRepository feedbackRepository, 
                         CustomFeedbackRepository customFeedbackRepository) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.feedbackRepository = feedbackRepository;
        this.customFeedbackRepository = customFeedbackRepository;
    }

    public List<Feedback> parseAndSaveMultipleFeedbacks(String token) {
        // API로부터 JSON 데이터 배열 가져오기
        String intraId = jwtUtil.getIntraId(token);
        Optional<FtUser> optional = userService.findByIntraId(intraId);
        
        if (optional.isEmpty()) {
            log.warn("Admin user not found for intraId: {}", intraId); // 경고 로그
            throw new NotFoundAdminUserException(NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE);
        }
        
        FtUser admin = optional.get();
        log.info("Admin found: {} with token: {}", admin.getIntraId(), admin.getOAuth2AccessToken());
        
        String oAuth2AccessToken = admin.getOAuth2AccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuth2AccessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        String uri = UriComponentsBuilder
                        .fromUriString(apiUrl)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toString();
        
        List<Feedback> feedbacks = new ArrayList<>();
    
        try {
            log.debug("Sending request to URI: {}", uri); // 요청 URI 로그
            ResponseEntity<JsonNode[]> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, JsonNode[].class);
            JsonNode[] jsonResponses = responseEntity.getBody();
            log.info("Received {} responses from API", jsonResponses.length); // 응답 개수 로그
            
            for (JsonNode rootNode : jsonResponses) {
                Feedback feedback = new Feedback();
                feedback.setId(rootNode.get("id").asLong());
                feedback.setUserInfo(rootNode.get("user").toString());
                feedback.setFeedbackableType(rootNode.get("feedbackable_type").asText());
                feedback.setFeedbackableId(rootNode.get("feedbackable_id").asLong());
                feedback.setComment(rootNode.get("comment").asText());
                feedback.setRating(rootNode.get("rating").asInt());
    
                // created_at 파싱
                String createdAtString = rootNode.get("created_at").asText();
                LocalDateTime createdAt = LocalDateTime.parse(createdAtString, DateTimeFormatter.ISO_DATE_TIME);
                feedback.setCreatedAt(createdAt);
    
                // feedback_details 파싱
                List<FeedbackDetail> feedbackDetails = objectMapper.convertValue(
                    rootNode.get("feedback_details"),
                    new TypeReference<List<FeedbackDetail>>() {}
                );
                feedback.setFeedbackDetails(objectMapper.writeValueAsString(feedbackDetails));
    
                feedbacks.add(feedback);
                log.debug("Parsed feedback: {}", feedback); // 개별 피드백 파싱 로그
            }
        } catch (Exception e) {
            log.error("Error occurred while calling the API: {}", e.getMessage(), e); // 오류 발생 시 스택 트레이스와 함께 로그
            // 추가적인 오류 처리 로직
        }
    
        // 모든 피드백을 한 번에 저장
        List<Feedback> savedFeedbacks = feedbackRepository.saveAll(feedbacks);
        log.info("Successfully saved {} feedbacks to the repository", savedFeedbacks.size()); // 저장된 피드백 개수 로그
        return savedFeedbacks;
    }    
}
