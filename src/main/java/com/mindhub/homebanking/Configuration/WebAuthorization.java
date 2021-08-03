package com.mindhub.homebanking.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin.html").hasAuthority("ADMIN")
                .antMatchers("/api/payment").permitAll()
                .antMatchers("/api/admin/**").hasAnyAuthority("ADMIN")
                //.antMatchers("/rest/**").hasAnyAuthority("ADMIN")
                //.antMatchers("/h2-console").hasAnyAuthority("ADMIN")
                .antMatchers("/index.html", "/js/**", "/css/**", "/img/**").permitAll()
                .antMatchers("/api/headers").permitAll()
                .antMatchers("/api/body").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/cards/delete").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/transfer").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/loans").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/clients/current").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/transaction/exportpdf").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/**").denyAll()
                .antMatchers("/**").hasAuthority("USER");
                //.antMatchers("/**").hasAnyAuthority("ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

}