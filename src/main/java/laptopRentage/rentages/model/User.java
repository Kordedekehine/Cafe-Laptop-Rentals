package laptopRentage.rentages.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -3492266417550204992L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "confirm_password",nullable = false)
    private String confirmPassword;

    @Column(name = "email_verified", nullable = false)
    private boolean isEmailVerified;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;



    @PrePersist
    void setUp() {
        this.id = UUID.randomUUID();
        // this.createdAt = LocalDateTime.now(ZoneId.of("+00:00"));
        this.createdAt = LocalDateTime.now();
    }
}
