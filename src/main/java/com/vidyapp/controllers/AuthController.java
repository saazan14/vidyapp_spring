package com.vidyapp.controllers;

import com.vidyapp.models.*;
import jakarta.validation.Valid;
import com.vidyapp.repositories.RoleRepository;
import com.vidyapp.repositories.UserRepository;
import com.vidyapp.security.jwt.JwtUtils;
import com.vidyapp.security.request.LoginRequest;
import com.vidyapp.security.request.SignupRequest;
import com.vidyapp.security.response.LoginResponse;
import com.vidyapp.security.response.MessageResponse;
import com.vidyapp.security.response.UserInfoResponse;
import com.vidyapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials="true")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;



    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        //      set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/public/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        Set<String> strRoles = signUpRequest.getRole();
//        Role role;
//
//        if (strRoles == null || strRoles.isEmpty()) {
//            role = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        } else {
//            String roleStr = strRoles.iterator().next();
//            if (roleStr.equals("admin")) {
//                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            } else {
//                role = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            }
//
//            user.setAccountNonLocked(true);
//            user.setAccountNonExpired(true);
//            user.setCredentialsNonExpired(true);
//            user.setEnabled(true);
//            user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//            user.setAccountExpiryDate(LocalDate.now().plusYears(1));
//            user.setTwoFactorEnabled(false);
//            user.setSignUpMethod("email");
//        }
//        user.setRole(role);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }


    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // 1. Validation checks
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // 2. Create User object (Auth info)
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        // 3. Determine Role and Map Profile Data
        Set<String> strRoles = signUpRequest.getRole();
        String roleName = (strRoles == null || strRoles.isEmpty()) ? "user" : strRoles.iterator().next();
        Role role;

        if (roleName.equalsIgnoreCase("admin")) {
            role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            AdminProfile adminProfile = new AdminProfile();
            adminProfile.setUser(user);
            // Map common fields from request to BaseProfile inheritance
            mapBaseProfileData(adminProfile, signUpRequest);

            user.setAdminProfile(adminProfile);

        } else {
            role = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setUser(user);
            // Map common fields
            mapBaseProfileData(studentProfile, signUpRequest);

            // Map Student-specific fields
            studentProfile.setTransport(AppTransport.Walker); // Default or from request
            studentProfile.setBloodGroup(signUpRequest.getBloodGroup());

            user.setStudentProfile(studentProfile);
        }

        // 4. Set User metadata
        user.setRole(role);
        configureUserAccountDefaults(user);

        // 5. Save (Cascades to the specific profile automatically)
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User and " + roleName + " profile registered successfully!"));
    }

    /**
     * Helper method to avoid code duplication for common profile fields
     */
    private void mapBaseProfileData(BaseProfile profile, SignupRequest request) {
        profile.setDob(request.getDob());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setMedicalIssue(request.getMedicalIssue());
        profile.setDisability(request.getDisability());
        profile.setAllergy(request.getAllergy());
        profile.setParentA(request.getParentA());
        profile.setParentB(request.getParentB());
        profile.setParentAContact(request.getParentAContact());
        profile.setParentBContact(request.getParentBContact());
    }

    private void configureUserAccountDefaults(User user) {
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),
                roles
        );
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }

}
