package pl.idzikdev.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.idzikdev.auth.services.UserService;
import static pl.idzikdev.auth.constans.SecurityConstans.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService service;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public WebSecurity(UserService service, BCryptPasswordEncoder encoder) {
        this.service=service;
        this.encoder=encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // h2 console
        http.
                cors().disable()
                .csrf().disable();
        //h2 console
        http.headers().frameOptions().sameOrigin().cacheControl();
                //.disable();


        http
                .authorizeRequests()
                .antMatchers(HOME,SIGN_UP_URL)
                .permitAll()
                .anyRequest()
                .authenticated()
//                .permitAll()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().loginProcessingUrl("/login")
                .and()
                .logout().deleteCookies("JSESSIONID");
    }
}