// package ggs.srr.controller.admin;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @AutoConfigureMockMvc
// class AdminControllerTest {

//     @Autowired
//     MockMvc mockMvc;

//     @Test
//     @DisplayName("일반 사용자가 /admin 으로 시작하는 url 접근시 접근 거부")
//     void giveUser_whenRequestToAdminPath_thenForbidden() throws Exception {

//         mockMvc.perform(get("/admin/test")
//                 .with(oauth2Login()
//                         .authorities(new SimpleGrantedAuthority("ROLE_USER"))
//                 )
//         ).andExpect(status().isForbidden());
//     }

//     @Test
//     @DisplayName("관리자는 /admin 으로 시작하는 url 접근시 접근 허용")
//     void givenAdmin_whenRequestToAdminPath_thenAccessSuccess() throws Exception {

//         mockMvc
//                 .perform(get("/admin/test")
//                         .with(oauth2Login()
//                                 .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
//                         )
//                 ).andExpect(status().isOk());
//     }
// }