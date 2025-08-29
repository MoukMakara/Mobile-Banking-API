package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;

    private String city;
    private String province;
    private Double latitude;
    private Double longitude;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    // Branch working hours
    @Column(nullable = false)
    private LocalTime openTime; // Example: 08:00 AM

    @Column(nullable = false)
    private LocalTime closeTime; // Example: 05:00 PM

    @Column(length = 50)
    private String workDays; // Example: "Mon-Fri" or "Mon-Sat"

    private Boolean isDeleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "branch")
    private List<Account> accounts;

    @OneToMany(mappedBy = "preferredBranch")
    private List<User> users;
}
