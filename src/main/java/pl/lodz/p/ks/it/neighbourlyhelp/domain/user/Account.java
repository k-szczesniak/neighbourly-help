package pl.lodz.p.ks.it.neighbourlyhelp.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Lastname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Password;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = Account.TABLE_NAME, indexes = {
        @Index(columnList = "id", name = Account.IX_ACCOUNT_ID, unique = true),
        @Index(columnList = "email", name = Account.IX_UQ_EMAIL, unique = true),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = Account.IX_UQ_EMAIL)
})
@Entity
public class Account implements UserDetails {

    public static final String TABLE_NAME = "account";
    public static final String IX_ACCOUNT_ID = "ix_account_id";
    public static final String IX_UQ_EMAIL = "ix_uq_email";

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;
    @Firstname
    private String firstName;
    @Lastname
    private String lastName;
    @Email
    private String email;
    @Password
    private String password;
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    public Account(String firstName, String lastName, String email, String password, AccountRole accountRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountRole = accountRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(accountRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}