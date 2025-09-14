package gr.aueb.cf.tourapp.model;

import gr.aueb.cf.tourapp.core.enums.GenderType;
import gr.aueb.cf.tourapp.core.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @Email
    private String email;

    @Column(unique = true)
    @NotNull
    private String vat;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type")
    private GenderType genderType;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(mappedBy = "user")
    private Guide guide;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    public boolean isGuide() {
        return getGuide() != null;
    }

    public boolean isCustomer() {
        return getCustomer() != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (this.getIsActive());
    }
}
