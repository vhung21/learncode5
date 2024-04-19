package com.example.learncode5.config;

import com.example.learncode5.jwt.JwtEntryPoint;
import com.example.learncode5.jwt.JwtTokenFilter;
import com.example.learncode5.service.Impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    JwtEntryPoint jwtEntryPoint;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()// cấu hình các cài đặt CORS cho các request HTTP được gửi đến ứng dụng
                .and()
                .csrf().disable() // tat csrf
                .authorizeRequests().antMatchers("/auth/login").permitAll()
                .and().authorizeRequests().antMatchers("/auth/registration").permitAll()
                .and().authorizeRequests().antMatchers("/users").permitAll()
                .and().exceptionHandling()
                // xử lý ngoại lệ khi xác thực thất bại
                .authenticationEntryPoint(jwtEntryPoint)
                // xử lý các yêu cầu từ người dùng chưa được xác thực hoặc không có quyền truy cập
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // không lưu trữ trạng thái phiên giữa các yêu cầu
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
