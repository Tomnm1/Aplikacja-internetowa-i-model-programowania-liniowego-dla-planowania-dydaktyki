package pl.poznan.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AdminCheckFilter adminCheckFilter;
    private final JwtVerifierFilter jwtVerifierFilter;

    public SecurityConfig(AdminCheckFilter adminCheckFilter, JwtVerifierFilter jwtVerifierFilter) {
        this.adminCheckFilter = adminCheckFilter;
        this.jwtVerifierFilter = jwtVerifierFilter;
        logger.info("SecurityConfig zainicjalizowany z AdminCheckFilter i JwtVerifierFilter.");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Konfiguracja łańcucha filtrów bezpieczeństwa.");

        http
                .authorizeHttpRequests(auth -> auth
                        // Endpointy, które mają być dostępne tylko dla "ROLE_TEACHER"
                        //.requestMatchers("/teachers/**").hasRole("TEACHER")
                        // Endpointy, które mają być dostępne tylko dla "ROLE_ADMIN"
                        .requestMatchers("/**").hasRole("ADMIN")
                        // Albo jakaś inna reguła
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(adminCheckFilter, JwtVerifierFilter.class);

        logger.debug("Dodano JwtVerifierFilter przed UsernamePasswordAuthenticationFilter " +
                "i AdminCheckFilter po JwtVerifierFilter.");

        return http.build();
    }
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll() // Pozwala na dostęp do wszystkich endpointów
//                )
//                .csrf().disable(); // Wyłącza ochronę CSRF
//        return http.build();
//    }

}


