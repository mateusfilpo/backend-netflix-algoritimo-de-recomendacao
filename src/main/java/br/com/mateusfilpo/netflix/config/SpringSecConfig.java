//package br.com.mateusfilpo.netflix.config;
//
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtDecoders;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SpringSecConfig {
//
//    @Bean
//    @Profile("test")
//    @Order(1)
//    public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {
//
//        http.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
//                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
//        return http.build();
//    }
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests(authorize -> {
////                    authorize.anyRequest().authenticated();
////                })
////                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> {
////                    httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults());
////                });
////
////        return http.build();
////    }
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.authorizeHttpRequests(authorize -> {
////                    authorize.requestMatchers("/validate-credentials").permitAll();
////                });
////
////        http.csrf(csrf -> csrf.disable());
////        return http.build();
////    }
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        // Configura acesso aos endpoints
////        http.authorizeHttpRequests(authorize -> {
////                    // Endpoints públicos
////                    authorize.requestMatchers("/validate-credentials").permitAll();
////                    // Endpoints protegidos
////                    authorize.anyRequest().authenticated();
////                })
////                // Configuração do OAuth2 Resource Server para JWT
////                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
////
////        http.csrf(csrf -> csrf.disable());  // Desabilita CSRF
////
////        return http.build();
////    }
//
//}