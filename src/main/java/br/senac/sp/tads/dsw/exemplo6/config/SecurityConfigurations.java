package br.senac.sp.tads.dsw.exemplo6.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    
    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // Necessário para permitir que o console do H2 renderize sua interface em
                // frames
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                    //Rotas públicas (Documentação, H2 e autenticação)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/h2-console/**").permitAll()
                                .requestMatchers(HttpMethod.POST,
                                                    "/auth/login",
                                                    "/auth/register").permitAll()
                    // Regras de Autorização: Apenas ADMIN pode cadastrar, alterar ou execluir
                                .requestMatchers(HttpMethod.POST, 
                                                    "/api/produtos/**",
                                                    "/api/categorias/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, 
                                                    "/api/produtos/**",
                                                    "/api/categorias/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, 
                                                    "/api/produtos/**",
                                                    "/api/categorias/**").hasRole("ADMIN") 
                    // Qualquer outra rota (exemplo: GET) exige apenas estar autenticado(USER ou ADMIN)                                                                       
                                .anyRequest().authenticated()
                            ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
