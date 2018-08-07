package com.leonds.config;

import com.leonds.blog.console.service.SysResourceService;
import com.leonds.core.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private CustomSecurityContextRepository customSecurityContextRepository;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .anyRequest().permitAll();
                .antMatchers("/login*").permitAll()
                .antMatchers("/logout*").permitAll()
                .antMatchers("/file/**").permitAll()
                .antMatchers("/api/**").authenticated();

        // Custom JWT based security filter
//        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);
//        httpSecurity
//            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // custom filter
        FilterInvocationSecurityMetadataSource metadataSource = new CustomSecurityMetadataSource(sysResourceService);
        CustomSecurityFilter customSecurityFilter = new CustomSecurityFilter(metadataSource);

        CustomAccessDecisionManager customAccessDecisionManager = new CustomAccessDecisionManager();
        customSecurityFilter.setAccessDecisionManager(customAccessDecisionManager);

        httpSecurity.addFilterBefore(customSecurityFilter, FilterSecurityInterceptor.class);

        // context repository
        httpSecurity.securityContext().securityContextRepository(customSecurityContextRepository);

        httpSecurity.logout().logoutSuccessHandler(new CustomLogoutSuccessHandler());

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/login*",
                "/file/**"
        );
    }
}
