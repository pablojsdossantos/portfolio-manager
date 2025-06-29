package pd.santos.portfoliomanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for simplicity in this example
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/users", "/users/**", "/assets", "/assets/**").permitAll()  // Allow access to user and asset endpoints
                .anyRequest().authenticated()  // Require authentication for all other endpoints
            );

        return http.build();
    }
}
