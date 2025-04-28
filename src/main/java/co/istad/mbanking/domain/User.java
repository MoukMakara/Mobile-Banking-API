package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 6)
    private String pin;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 50)
    private String nationalCardId;
    @Column(nullable = false, unique = true, length = 50)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private LocalDate dob;
    @Column(nullable = false, length = 10)
    private String gender;
    private String profileImage; // dara.png
    @Column(unique = true, length = 50)
    private String studentCardId;

    private String position;
    private String companyName;
    private String employeeType;
    private String cityOrProvince;
    private String khanOrDistrict;
    private String sangkatOrCommune;
    private String street;
    private String village;
    private BigDecimal monthlyIncomeRange;
    private String mainSourceOfIncome;

    private LocalDateTime createdAt;
    private Boolean isVerified;
    private Boolean isBlocked;

    // Security
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<UserAccount> userAccounts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

}
