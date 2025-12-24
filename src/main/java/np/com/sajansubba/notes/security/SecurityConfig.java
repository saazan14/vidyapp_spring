package np.com.sajansubba.notes.security;

import np.com.sajansubba.notes.models.AppRole;
import np.com.sajansubba.notes.models.Role;
import np.com.sajansubba.notes.models.User;
import np.com.sajansubba.notes.repositories.RoleRepository;
import np.com.sajansubba.notes.repositories.UserRepository;
import np.com.sajansubba.notes.security.jwt.AuthEntryPointJwt;
import np.com.sajansubba.notes.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    private RoleRepository  roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthEntryPointJwt  unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/auth/public/**")
        );
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/csrf-token").permitAll()
                .requestMatchers("/api/auth/public/**").permitAll()
                .anyRequest().authenticated());
        http.exceptionHandling(exception->
            exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
//     http.csrf(AbstractHttpConfigurer::disable);
//    .requestMatchers("/contact").permitAll()
//    .requestMatchers("/public/**").permitAll()
//    .requestMatchers("/images/**").permitAll()
//    .requestMatchers("/admin/**").hasRole("ADMIN")
//    .requestMatchers("/user/**").hasRole("USER")
//    .requestMatchers("/api/admin/**").hasRole("ADMIN")

//       http.addFilterBefore(new CustomLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
//       http.addFilterAfter(new RequestValidationFilter(), CustomLoggingFilter.class);

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Seed for dev env
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user")) {
                User user1 = new User("user", "user1@example.com",  passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole((np.com.sajansubba.notes.models.Role) userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole((np.com.sajansubba.notes.models.Role) adminRole);
                userRepository.save(admin);
            }
        };
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager  manager = new JdbcUserDetailsManager(dataSource);
//        if(!manager.userExists("user1")){
//            manager.createUser(
//                    User.withUsername("user1").password("{noop}password").roles("USER").build()
//            );
//        }
//        if(!manager.userExists("admin1")){
//            manager.createUser(
//                    User.withUsername("admin1").password("{noop}password").roles("ADMIN").build()
//            );
//        }
//        return manager;
//    }


}
