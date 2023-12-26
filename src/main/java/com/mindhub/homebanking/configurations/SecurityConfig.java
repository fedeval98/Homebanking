package com.mindhub.homebanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration // notacion para decir que el metodo tiene mas de 1 o mas @bean
@EnableWebSecurity // activamos la seguridad para poder crear filtros personalizados
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
// filtro por autoridad, todos, cliente o admin. Con cada requestMatchers lo que hacemos es habilitar
// rutas o peticiones a los usuarios de nuestro programa, salvo el anyrequest que al ser denyall
// las bloquea a todas las peticiones que no es habilitadas arriba de eso.
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/web/assets/**","index.html").permitAll()
                .requestMatchers("/web/*","/api/clients/current").hasAnyAuthority("CLIENT","ADMIN")
                .requestMatchers("/h2-console/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/clients/current/accounts","/api/clients/current/cards").hasAuthority("CLIENT")
                .anyRequest().denyAll());

// proteccion CROSS SITE REQUEST FORGERY, la deshabilitamos para poder acceder al h2-console, porque es una api de terceros
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
// se desactiva los frameoptions porque el h2 puede intentar cargarse como un iframe
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                frameOptionsConfig -> frameOptionsConfig.disable()));
// modificacion del formulario de login
        http.formLogin(formLogin -> formLogin
                .loginPage("/index.html") // cambio a la ruta de la pagina de login
                .loginProcessingUrl("/api/login") // endpoint que escuchar el login y su queryparam
                .usernameParameter("email") //queryparam de email
                .passwordParameter("password") //queryparam de password
                .successHandler((request, response, authentication) -> { // eventos por login correcto
                    if(request.isUserInRole("ADMIN")){ //la redireccion no funciona.
                        response.sendRedirect("/h2-console/");
                        clearAuthenticationAttributes(request);
                    } else if (request.isUserInRole("CLIENT")){
                        response.sendRedirect("/web/accounts.html");
                        clearAuthenticationAttributes(request);
                    }
                })
                //devolucion de error si falla el login
                .failureHandler((request, response, exception) -> response.sendError(401))
                .permitAll()
        );
// devolucion de error si algo falla en alguna solicitud o ingreso no autorizado
        http.exceptionHandling( exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> response.sendError(403)));
// endpoint para hacer logout y eliminar la cookie.
        http.logout(httpSecurityLogoutConfigurer ->
                httpSecurityLogoutConfigurer
                        .logoutUrl("/api/logout")//endpoint de logout para hacer un post
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID"));  // eliminacion de la cookie

        http.rememberMe(Customizer.withDefaults());


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
