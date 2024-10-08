package ggs.srr.service.system;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.jwt.JWTUtil;
import ggs.srr.oauth.auth.FtProjectParser;
import ggs.srr.oauth.auth.FtUserDataParser;
import ggs.srr.oauth.auth.dto.ProjectStatusDto;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.system.exception.NotFoundAdminUserException;
import ggs.srr.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InitDataManager {

    private final String CAMPUS_USER_URI = "https://api.intra.42.fr/v2/cursus/21/users";
    private final String USER_INFO_BASE_URI = "https://api.intra.42.fr/v2/users";

    private final String NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE = "cannot found admin user";
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final FtProjectParser projectParser;
    private final ProjectUserService projectUserService;

    @Autowired
    public InitDataManager(UserService userService, JWTUtil jwtUtil, FtProjectParser projectParser, ProjectUserService projectUserService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.projectParser = projectParser;
        this.projectUserService = projectUserService;
    }

    public void initData(String token) {
        String intraId = jwtUtil.getIntraId(token);

        Optional<FtUser> optional = userService.findByIntraId(intraId);
        if (optional.isEmpty()) {
            throw new NotFoundAdminUserException(NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE);
        }
        FtUser admin = optional.get();
        log.info("admin = {} token = {}", admin.getIntraId(), admin.getOAuth2AccessToken());
        String oAuth2AccessToken = admin.getOAuth2AccessToken();
        List<Long> allUser42ServerId = getAllUser42ServerId(oAuth2AccessToken);
        //
        persistAllUser(allUser42ServerId, oAuth2AccessToken);
        log.info("ids = {}", allUser42ServerId);
    }

    private void persistAllUser(List<Long> allUser42ServerId, String oAuth2AccessToken) {
        int page = 0;

        long startTime = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuth2AccessToken);
        HttpEntity request = new HttpEntity(headers);
        for(long userId : allUser42ServerId) {
            String uri = USER_INFO_BASE_URI + "/" + userId;
            String requestURI = UriComponentsBuilder
                    .fromUriString(uri)
                    .queryParam("page", page)
                    .build()
                    .toString();
            RestTemplate restTemplate = new RestTemplate();

            // 사용자가 하고 있는 프로젝트 !! -=> project_user
            ResponseEntity<HashMap> responseEntity = restTemplate.exchange(requestURI, HttpMethod.GET, request, HashMap.class);
            FtUserDataParser ftUserDataParser = new FtUserDataParser();
            log.info("data = {}", responseEntity.getBody());

            FtUser user = ftUserDataParser.parseUser(responseEntity);
            List<ProjectStatusDto> projects = projectParser.parseUsersProject(responseEntity);


            log.info("projects = {}", projects);
            if (userService.findByIntraId(user.getIntraId()).isEmpty()) {
                userService.save(user);
            }

            FtUser resUser = userService.findByIntraId(user.getIntraId()).get();
            for (ProjectStatusDto project : projects) {
                ProjectUser projectUser = new ProjectUser();
                projectUser.initUser(resUser);
                projectUser.initProject(project.getProject());
                //projectUser.initStatus(project.getStatus());
                projectUserService.save(projectUser);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info ("time = {}", endTime - startTime);
    }



    private List<Long> getAllUser42ServerId(String oAuth2AccessToken) {

        try {
            List<Long> ftServerIds = new ArrayList<>();

            int page = 0;

            log.info("start init data!!");
            long startTime = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + oAuth2AccessToken);
            HttpEntity request = new HttpEntity(headers);
            while (true) {
                String uri = UriComponentsBuilder
                        .fromUriString(CAMPUS_USER_URI)
                        .queryParam("campus_id", 69)
                        .queryParam("page", page)
                        .encode()
                        .build()
                        .toString();
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ArrayList> response = restTemplate.exchange(uri, HttpMethod.GET, request, ArrayList.class);
                ArrayList<HashMap<String, Object>> body = response.getBody();

                log.info("page : {} : body = {}", page, body);
                if (body == null || body.isEmpty())
                    break;
                addId(ftServerIds, body);
                page++;
            }
            long endTime = System.currentTimeMillis();
            log.info("time = {}", endTime - startTime);
            log.info("size = {}", ftServerIds.size());
            return ftServerIds;
        } catch (HttpClientErrorException e) {
            log.info("access token expire!! 갱신 로직 추가 필요");
            throw new RuntimeException("access token expired");
        }
    }

    private void addId(List<Long> list, ArrayList<HashMap<String, Object>> body) {

        for(int i = 0 ; i < body.size(); i++) {
            HashMap<String, Object> data = body.get(i);
            long id = (long) (int) data.get("id");
            list.add(id);
        }
    }
}
