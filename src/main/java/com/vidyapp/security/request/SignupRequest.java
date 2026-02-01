package com.vidyapp.security.request;

import java.util.Date;
import java.util.Set;

import com.vidyapp.models.AppTransport;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    // COMMON PROFILE FIELDS (From BaseProfile)
    private Date dob;
    private String phone;
    private String mobile;
    private String address;
    private String medicalIssue;
    private String disability;
    private String allergy;
    @Column(name = "parent_a")
    private String parentA;

    @Column(name = "parent_b")
    private String parentB;
    private String parentAContact;
    private String parentBContact;



    // STUDENT-SPECIFIC FIELDS
    private String bloodGroup;
    private String house;
    private AppTransport transport; // This will be null if an Admin is signing up
}