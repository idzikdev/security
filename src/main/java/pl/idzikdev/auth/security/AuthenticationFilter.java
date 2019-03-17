package pl.idzikdev.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.idzikdev.auth.models.MyUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static pl.idzikdev.auth.constans.SecurityConstans.*;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager manager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.manager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            MyUser myUser = new ObjectMapper().readValue(request.getInputStream(), MyUser.class);
            return manager.authenticate(new UsernamePasswordAuthenticationToken(myUser.getUsername(), myUser.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String token= JWT.create().withSubject(
                ((User)authResult.getPrincipal())
                .getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.setHeader("Authorization",PREFIX+token);
        response.setHeader("Access-Control_Allow-Origin","");
        response.setHeader("Access-Control-Methods","POST,GET,OPTIONS,DELETE,PUT");
        logger.info(PREFIX+token);
    }
}
