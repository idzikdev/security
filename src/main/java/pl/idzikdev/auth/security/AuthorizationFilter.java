package pl.idzikdev.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.idzikdev.auth.constans.SecurityConstans;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static pl.idzikdev.auth.constans.SecurityConstans.*;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private AuthenticationManager manager;

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        getHeaderInfo(request,response);
        if (header == null || header.startsWith(PREFIX)) {
            chain.doFilter(request, response);
        } else {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
            if (authenticationToken != null) {
                logger.info(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            }
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String userToken = null;
        String token = request.getHeader(PREFIX);
        if (token != null) {
            try {
                userToken = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(PREFIX, ""))
                        .getSubject();
            }
            catch (JWTVerificationException e){
                logger.error("Błąd weryfikacji");
            }
            logger.info(token);
            if (userToken != null) {
                return new UsernamePasswordAuthenticationToken(userToken, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    private void getHeaderInfo(HttpServletRequest request,HttpServletResponse response){
        Map<String,String> header=new HashMap<>();
        Enumeration headerNames=request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key=headerNames.nextElement().toString();
            String value=request.getHeader(key);
            header.put(key,value);
        }
        header.forEach((k,v)-> System.out.println(k+" "+v));
    }
}

