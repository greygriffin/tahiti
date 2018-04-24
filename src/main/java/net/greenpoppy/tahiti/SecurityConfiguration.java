package net.greenpoppy.tahiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Good example for reference: https://github.com/spring-projects/spring-security-javaconfig/blob/master/samples-web.md#sample-multi-http-web-configuration
// But note that authorizeUrls has been renamed as authorizeRequests
// Also see https://stackoverflow.com/questions/18729752/basic-and-form-based-authentication-with-spring-security-javaconfig
// and https://stackoverflow.com/questions/33739359/combining-basic-authentication-and-form-login-for-the-same-rest-api


@Configuration
@EnableWebSecurity
public class SecurityConfiguration
    extends WebSecurityConfigurerAdapter {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("user").password(passwordEncoder.encode("password")).roles("USER").and()
            .withUser("admin").password(passwordEncoder.encode("secret")).roles("USER", "ADMIN");
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurerAdapter
        extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http)
            throws Exception {
            http
                .antMatcher("/api/**")
                .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, "/api/info").permitAll()
                .antMatchers(HttpMethod.GET, "/api/clubs").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/clubs").hasRole("ADMIN")
                    .and()
                .httpBasic()
                .and().csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class UiWebSecurityConfigurerAdapter
        extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http)
            throws Exception {
            http
                .antMatcher("**/*.html")
                .authorizeRequests()
                    .antMatchers("/css/**", "/js/**").permitAll()
                    .and()
                .formLogin()
                    .and()
                .logout().permitAll();
        }
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}

