package example.greetings.config;

import example.greetings.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder(9);
//    }

        @Autowired
        private UserService userService;


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                        .antMatchers("/", "/register","/static/**","/activate/*").permitAll()
                        .antMatchers("/main","/chat").authenticated()
                        .anyRequest().authenticated()
                    .and()
                        .rememberMe()
                    .and()
                        .formLogin()
                        .loginPage("/login")
                        .permitAll()
                    .and()
                        .logout()
                        .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception{

            auth.userDetailsService(userService)
                    .passwordEncoder(NoOpPasswordEncoder.getInstance());//passwordEncoder
//                    .usersByUsernameQuery("select username,password,active " +
//                            "from usr " +
//                            "where username=?")
//                    .authoritiesByUsernameQuery("select u.username, ur.roles" +
//                            " from usr u " +
//                            "inner join user_role ur " +
//                            "on u.id=user_id " +
//                            "where u.username=?");

        }
//        @Bean
//        @Override
//        public UserDetailsService userDetailsService() {
//            UserDetails user =
//                    User.withDefaultPasswordEncoder()
//                            .username("u")
//                            .password("l")
//                            .roles("USER")
//                            .build();
//
//            return new InMemoryUserDetailsManager(user);
//        }
    }

