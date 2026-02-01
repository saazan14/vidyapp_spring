package com.vidyapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
public abstract class BaseProfile {
    // Note: No @Id here, child classes will define it with @MapsId

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

    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}