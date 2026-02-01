package com.vidyapp.security;

import com.vidyapp.models.*;
import com.vidyapp.repositories.RoleRepository;
import com.vidyapp.repositories.UserRepository;
import com.vidyapp.security.jwt.AuthEntryPointJwt;
import com.vidyapp.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        http.csrf(AbstractHttpConfigurer::disable); // Usually disabled for Stateless JWT APIs

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

//    // Seed for dev env
//    @Bean
//    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
//                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_STUDENT)));
//
//            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
//
//            if (!userRepository.existsByUserName("user")) {
//                User user1 = new User("user", "user1@example.com",  passwordEncoder.encode("password1"));
//                user1.setAccountNonLocked(false);
//                user1.setAccountNonExpired(true);
//                user1.setCredentialsNonExpired(true);
//                user1.setEnabled(true);
//                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                user1.setTwoFactorEnabled(false);
//                user1.setSignUpMethod("email");
//                user1.setRole((Role) userRole);
//                userRepository.save(user1);
//            }
//
//            if (!userRepository.existsByUserName("admin")) {
//                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
//                admin.setAccountNonLocked(true);
//                admin.setAccountNonExpired(true);
//                admin.setCredentialsNonExpired(true);
//                admin.setEnabled(true);
//                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                admin.setTwoFactorEnabled(false);
//                admin.setSignUpMethod("email");
//                admin.setRole((Role) adminRole);
//                userRepository.save(admin);
//            }
//        };
//    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Ensure Roles exist
            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_STUDENT)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            // 2. Seed Student User
            if (!userRepository.existsByUserName("user")) {
                User user1 = new User("user", "user1@example.com", passwordEncoder.encode("password123"));
                configureDefaults(user1, studentRole);
                user1.setAccountNonLocked(true); // Changed to true so you can actually log in

                // Initialize Student Profile
                StudentProfile studentProfile = new StudentProfile();
                studentProfile.setUser(user1);
                studentProfile.setRegistration(1L);
                studentProfile.setAddress("123 Student Street");
                studentProfile.setPhone("555-0101");
                studentProfile.setTransport(AppTransport.Bus);
                studentProfile.setBloodGroup("O+");

                user1.setStudentProfile(studentProfile);

                userRepository.save(user1); // Saves both User and StudentProfile
            }

            // 3. Seed Admin User
            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"));
                configureDefaults(admin, adminRole);

                // Initialize Admin Profile
                AdminProfile adminProfile = new AdminProfile();
                adminProfile.setUser(admin);
                adminProfile.setAddress("456 Admin Headquarters");
                adminProfile.setPhone("555-9999");

                admin.setAdminProfile(adminProfile);

                userRepository.save(admin); // Saves both User and AdminProfile
            }
        };
    }

    /**
     * Helper to reduce boilerplate in the seeder
     */
    private void configureDefaults(User user, Role role) {
        user.setRole(role);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
    }
}
