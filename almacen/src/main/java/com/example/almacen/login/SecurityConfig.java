package com.example.almacen.login;

import com.example.almacen.usuarios.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public SecurityConfig(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        // ADMINISTRADO UNICAMENTE
                        .requestMatchers(HttpMethod.POST, "/api/v1/productos").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/*/entrada").hasRole("Administrador")
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/*/entrada").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/*/estatus").hasRole("Administrador")
                        .requestMatchers(HttpMethod.GET, "/api/v1/movimientos/**").hasRole("Administrador")
                        // ALMACENISTA UNICAMENTE
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/*/salida").hasRole("Almacenista")
                        .requestMatchers(HttpMethod.GET, "/api/v1/productos/*/salida").hasRole("Administrador")
                        // RESTO DE ACCIONES NECESITAN SOLO ESTAR LOGUEADOS
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }
}
