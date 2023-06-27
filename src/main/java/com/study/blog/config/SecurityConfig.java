package com.study.blog.config;

import com.study.blog.oauth2.CustomOAuth2UserService;
import com.study.blog.oauth2.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests((auth)-> {
                    auth.antMatchers("/","/css/**","/error","/js/**","/post","/image/**/*","/oauth2-login").permitAll()
                            .anyRequest().authenticated();
                })
//                .authorizeRequests()//URL별 권한 관리를 설정하는 옵션의 시작점
//                .antMatchers("/","/css/**","/error",
//                        "/js/**","/post").permitAll()
//                .anyRequest().authenticated()
                .logout()
                .logoutSuccessUrl("/post")
                .and()
                .oauth2Login()
                .loginPage("/oauth2-login")
                .defaultSuccessUrl("/post")
                .failureUrl("/oauth2-login")
                .userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
    }
}
