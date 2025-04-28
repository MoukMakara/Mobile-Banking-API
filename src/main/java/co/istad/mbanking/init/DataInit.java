package co.istad.mbanking.init;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.account.AccountTypeRepository;
import co.istad.mbanking.features.auth.RoleRepository;
import co.istad.mbanking.features.card.CardTypeRepository;
import co.istad.mbanking.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;
    private final CardTypeRepository cardTypeRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        initCardTypeData();
        initAccountTypeData();
        initRoleData();
        initUserData();
    }

    private void initRoleData() {
        // ADMIN, MANAGER, STAFF, CUSTOMER, USER
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("USER").build());
        roles.add(Role.builder().name("CUSTOMER").build());
        roles.add(Role.builder().name("STAFF").build());
        roles.add(Role.builder().name("MANAGER").build());
        roles.add(Role.builder().name("ADMIN").build());
        roleRepository.saveAll(roles);
    }

    private void initUserData() {
        User user1 = new User();
        user1.setUuid(UUID.randomUUID().toString());
        user1.setEmail("admin@gmail.com");
        user1.setPhoneNumber("096111222");
        user1.setPassword(passwordEncoder.encode("Qwer123@#$"));
        user1.setPin("1234");
        user1.setProfileImage("profile-admin.jpg");
        user1.setNationalCardId("11112222");
        user1.setGender("Male");
        user1.setName("ADMIN");
        user1.setDob(LocalDate.of(1999, 12, 31));
        user1.setCreatedAt(LocalDateTime.now());
        user1.setIsDeleted(false);
        user1.setIsBlocked(false);
        user1.setIsVerified(true);
        user1.setIsAccountNonExpired(true);
        user1.setIsAccountNonLocked(true);
        user1.setIsCredentialsNonExpired(true);

        User user2 = new User();
        user2.setUuid(UUID.randomUUID().toString());
        user2.setEmail("manager@gmail.com");
        user2.setPhoneNumber("097111222");
        user2.setPassword(passwordEncoder.encode("Qwer123@#$"));
        user2.setPin("1234");
        user2.setProfileImage("profile-manager.jpg");
        user2.setNationalCardId("22223333");
        user2.setGender("Male");
        user2.setName("MANAGER");
        user2.setDob(LocalDate.of(2000, 12, 12));
        user2.setCreatedAt(LocalDateTime.now());
        user2.setIsDeleted(false);
        user2.setIsBlocked(false);
        user2.setIsVerified(true);
        user2.setIsAccountNonExpired(true);
        user2.setIsAccountNonLocked(true);
        user2.setIsCredentialsNonExpired(true);

        User user3 = new User();
        user3.setUuid(UUID.randomUUID().toString());
        user3.setEmail("staff@gmail.com");
        user3.setPhoneNumber("098111222");
        user3.setPassword(passwordEncoder.encode("Qwer123@#$"));
        user3.setPin("1234");
        user3.setProfileImage("profile-staff.jpg");
        user3.setNationalCardId("33334444");
        user3.setGender("Female");
        user3.setName("STAFF");
        user3.setDob(LocalDate.of(2001, 5, 20));
        user3.setCreatedAt(LocalDateTime.now());
        user3.setIsDeleted(false);
        user3.setIsBlocked(false);
        user3.setIsVerified(true);
        user3.setIsAccountNonExpired(true);
        user3.setIsAccountNonLocked(true);
        user3.setIsCredentialsNonExpired(true);

        User user4 = new User();
        user4.setUuid(UUID.randomUUID().toString());
        user4.setEmail("customer@gmail.com");
        user4.setPhoneNumber("099111222");
        user4.setPassword(passwordEncoder.encode("Qwer123@#$"));
        user4.setPin("1234");
        user4.setProfileImage("profile-customer.jpg");
        user4.setNationalCardId("44445555");
        user4.setGender("Female");
        user4.setName("CUSTOMER");
        user4.setDob(LocalDate.of(2002, 8, 15));
        user4.setCreatedAt(LocalDateTime.now());
        user4.setIsDeleted(false);
        user4.setIsBlocked(false);
        user4.setIsVerified(true);
        user4.setIsAccountNonExpired(true);
        user4.setIsAccountNonLocked(true);
        user4.setIsCredentialsNonExpired(true);

        User user5 = new User();
        user5.setUuid(UUID.randomUUID().toString());
        user5.setEmail("user@gmail.com");
        user5.setPhoneNumber("095111222");
        user5.setPassword(passwordEncoder.encode("Qwer123@#$"));
        user5.setPin("1234");
        user5.setProfileImage("profile-user.jpg");
        user5.setNationalCardId("55556666");
        user5.setGender("Male");
        user5.setName("USER");
        user5.setDob(LocalDate.of(2003, 3, 10));
        user5.setCreatedAt(LocalDateTime.now());
        user5.setIsDeleted(false);
        user5.setIsBlocked(false);
        user5.setIsVerified(true);
        user5.setIsAccountNonExpired(true);
        user5.setIsAccountNonLocked(true);
        user5.setIsCredentialsNonExpired(true);

        // Assign roles to each user
        user1.setRoles(roleRepository.findAll()); // ADMIN gets all roles

        user2.setRoles(List.of(
                roleRepository.findById(4).orElseThrow(), // MANAGER
                roleRepository.findById(3).orElseThrow(), // STAFF
                roleRepository.findById(2).orElseThrow(), // CUSTOMER
                roleRepository.findById(1).orElseThrow()  // USER
        ));

        user3.setRoles(List.of(
                roleRepository.findById(3).orElseThrow(), // STAFF
                roleRepository.findById(2).orElseThrow(), // CUSTOMER
                roleRepository.findById(1).orElseThrow()  // USER
        ));

        user4.setRoles(List.of(
                roleRepository.findById(2).orElseThrow(), // CUSTOMER
                roleRepository.findById(1).orElseThrow()  // USER
        ));

        user5.setRoles(List.of(
                roleRepository.findById(1).orElseThrow() // USER
        ));


        // Save all users
        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
    }


    private void initAccountTypeData() {
        AccountType saving = new AccountType();
        saving.setName("Saving Account");
        saving.setAlias("saving-account");
        saving.setDescription("Saving Account");
        saving.setIsDeleted(false);
        AccountType payroll = new AccountType();
        payroll.setName("Payroll Account");
        payroll.setAlias("payroll-account");
        payroll.setDescription("Payroll Account");
        payroll.setIsDeleted(false);
        AccountType current = new AccountType();
        current.setName("Current Account");
        current.setAlias("current-account");
        current.setDescription("Current Account");
        current.setIsDeleted(true);

        accountTypeRepository.saveAll(List.of(saving, payroll, current));
    }

    private void initCardTypeData() {
        CardType visa = new CardType();
        visa.setName("Visa");
        visa.setAlias("visa");
        visa.setIsDeleted(false);
        CardType mastercard = new CardType();
        mastercard.setName("Mastercard");
        mastercard.setAlias("mastercard");
        mastercard.setIsDeleted(false);

        /*cardTypeRepository.save(visa);
        cardTypeRepository.save(mastercard);*/
        cardTypeRepository.saveAll(List.of(visa, mastercard));
    }

}
