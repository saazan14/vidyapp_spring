package com.vidyapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "student_profiles")
public class StudentProfile extends BaseProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId // Hibernate takes the User's ID and assigns it here
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private Long registration;

    private String bloodGroup;
    private String house;

    @Enumerated(EnumType.STRING)
    private AppTransport transport;

    private String busStop;
}