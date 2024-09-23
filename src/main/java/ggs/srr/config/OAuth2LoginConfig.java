package ggs.srr.config;

import ggs.srr.service.oauth2.CustomOauth2userService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class OAuth2LoginConfig {

    @Value("${42.client-id}")
    private String CLIENT_ID;

    @Value("${42.client-secret}")
    private String CLIENT_SECRET;

    private final CustomOauth2userService userService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(userService)
                        ));
        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.ftClientRegistration());
    }

    private ClientRegistration ftClientRegistration() {

        return ClientRegistration.withRegistrationId("42")
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/42")
                //.redirectUri("http://118.67.134.143:8080/login/oauth2/code/42")
                .scope("public", "projects", "profile", "tig", "forum")
                .authorizationUri("https://api.intra.42.fr/oauth/authorize")
                .tokenUri("https://api.intra.42.fr/oauth/token")
                .userInfoUri("https://api.intra.42.fr/v2/me")
                .userNameAttributeName("id")
                .clientName("42")
                .build();
    }
}
