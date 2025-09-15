package com.example.datn_meta.AppConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ✅ Chỉ giữ 1 bean PasswordEncoder
    @Bean

    public PasswordEncoder passwordEncoder() {
        // CHANGED: dùng BCrypt thay vì NoOp
        return NoOpPasswordEncoder.getInstance();
    }


    // ✅ Cấu hình security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/login", "/register").permitAll() // nếu bạn có route khác
                        .anyRequest().authenticated()
                ).formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")         // POST -> Spring xử lý
                        .defaultSuccessUrl("/homePage", true)     // login thành công -> /home
                        .failureUrl("/auth/login?error=true")// đường dẫn tới login.html
                        .permitAll())
                .oauth2Login(oauth->oauth
                        .loginPage("/auth/login")//form login
                        .defaultSuccessUrl("/homePage", true)
                        .failureUrl("/auth/login?error=true")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService()))// xử lý dữ liệu đăng nhập vào gg/ fb

                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                );


        return http.build();
    }

    // ✅ AuthenticationManager cho login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return userRequest -> {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); // Google/ facebook
            String email = oAuth2User.getAttribute("email");
            String password = oAuth2User.getAttribute("password");
            return oAuth2User;
        };

    }
}
