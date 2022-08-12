package laptopRentage.rentages.security;

import laptopRentage.rentages.ApiPathExclusion;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerUserDetailsService customUserDetailService;

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(customUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and().
                sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(List.of(ApiPathExclusion.values())
                        .stream()
                        .map(apiPath -> apiPath.getPath())
                        .toArray(String[]::new))
                .permitAll().anyRequest().authenticated().and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    }

