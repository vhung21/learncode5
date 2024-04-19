package com.example.learncode5.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try
        {
            String token = getJwt(httpServletRequest);
            if (token != null && jwtProvider.validateToken(token)) {
                String username=jwtProvider.getUserNameFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                // Tạo một đối tượng đại diện cho việc xác thực người dùng
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                // Thiết lập thông tin chi tiết của việc xác thực
                // HttpServletRequest: cho phép truy xuất các thông tin của yêu cầu HTTP
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // Cho biết rằng người dùng đã được xác thực và có quyền truy cập vào các phần của ứng dụng theo quy định
            }
        }
        catch (Exception e)
        {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwt(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer"))
            return authHeader.replace("Bearer", "");
        return null;
    }
}
