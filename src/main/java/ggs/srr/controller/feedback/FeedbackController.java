package ggs.srr.controller.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ggs.srr.domain.feedback.Feedback;
import ggs.srr.service.feedback.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
    
    // 피드백 생성
    @PostMapping
    public ResponseEntity<List<Feedback>> createFeedback(HttpServletRequest request) {
        try {
            String authorization = request.getHeader("Authorization");
            log.debug("Authorization header: {}", authorization); // Authorization 헤더 로그
            
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.warn("Unauthorized request: Authorization header is missing or invalid"); // 경고 로그
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
            }
            
            String token = authorization.split(" ")[1];
            log.debug("Extracted token: {}", token); // 추출한 토큰 로그
            
            List<Feedback> savedFeedback = feedbackService.parseAndSaveMultipleFeedbacks(token);
            log.info("Successfully saved feedbacks: {}", savedFeedback.size()); // 피드백 저장 성공 로그
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback); // 201 Created 상태 코드와 함께 반환

        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("ArrayIndexOutOfBoundsException: {}", e.getMessage()); // 오류 로그
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
        } catch (Exception e) {
            log.error("Error occurred while saving feedback: {}", e.getMessage(), e); // 기타 예외 처리 로그
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // ID로 피드백 조회
    // @GetMapping("/{id}")
    // public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
    //     Feedback feedback = feedbackService.getFeedbackById(id);
    //     if (feedback != null) {
    //         return ResponseEntity.ok(feedback); // 200 OK 상태 코드와 함께 반환
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found 상태 코드
    //     }
    // }

    // // 모든 피드백 조회
    // @GetMapping
    // public ResponseEntity<List<Feedback>> getAllFeedbacks() {
    //     List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
    //     return ResponseEntity.ok(feedbacks); // 200 OK 상태 코드와 함께 반환
    // }

    // // 특정 사용자 피드백 조회
    // @GetMapping("/user/{userLogin}")
    // public ResponseEntity<List<Feedback>> getFeedbacksByUser(@PathVariable String userLogin) {
    //     List<Feedback> feedbacks = feedbackService.getFeedbacksByUser(userLogin);
    //     if (feedbacks.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found 상태 코드
    //     }
    //     return ResponseEntity.ok(feedbacks); // 200 OK 상태 코드와 함께 반환
    // }

    // // 특정 평점으로 피드백 조회
    // @GetMapping("/rating/{rating}")
    // public ResponseEntity<List<Feedback>> getFeedbacksByRating(@PathVariable int rating) {
    //     List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(rating);
    //     if (feedbacks.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found 상태 코드
    //     }
    //     return ResponseEntity.ok(feedbacks); // 200 OK 상태 코드와 함께 반환
    // }

    // // 피드백 삭제
    // @DeleteMapping
    // public ResponseEntity<Void> deleteFeedback(@RequestBody Feedback feedback) {
    //     feedbackService.deleteFeedback(feedback);
    //     return ResponseEntity.noContent().build(); // 204 No Content 상태 코드
    // }
}

