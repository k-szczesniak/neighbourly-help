package pl.lodz.p.ks.it.neighbourlyhelp.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.ConfirmationToken;
import pl.lodz.p.ks.it.neighbourlyhelp.entities.Role;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.ContactNumber;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Email;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Firstname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Language;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Lastname;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.Password;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = Account.TABLE_NAME, indexes = {
        @Index(columnList = "id", name = Account.IX_ACCOUNT_ID, unique = true),
        @Index(columnList = "email", name = Account.IX_UQ_EMAIL, unique = true),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}, name = Account.IX_UQ_EMAIL)
})
@Entity
public class Account extends AbstractEntity implements UserDetails {

    public static final String TABLE_NAME = "account";
    public static final String IX_ACCOUNT_ID = "ix_account_id";
    public static final String IX_UQ_EMAIL = "ix_uq_email";
    public static final String ROLE_PREFIX = "ROLE_";

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

    @Email
    @Setter
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Firstname
    @Setter
    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Lastname
    @Setter
    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Password
    @Setter
    @NotNull
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Setter
    @Language
    @Column(name = "language")
    private String language;

    @Setter
    @ContactNumber
    @Column(name = "contact_number")
    private String contactNumber;

    @Setter
    @NotNull
    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Setter
    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_successful_login_date")
    private Date lastSuccessfulLoginDate;

    @Setter
    @Column(name = "last_successful_login_ip_address")
    private String lastSuccessfulLoginIpAddress;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_failed_login_date")
    private Date lastFailedLoginDate;

    @Setter
    @Column(name = "last_failed_login_ip_address")
    private String lastFailedLoginIpAddress;

    @Setter
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "account", fetch = FetchType.EAGER) //TODO: fetchType changed to EAGER
    private Set<Role> roleList = new HashSet<>();

    @Setter
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
            mappedBy = "account", orphanRemoval = true)
    private Set<ConfirmationToken> confirmationTokenList = new HashSet<>();

    @Setter
    @Min(value = 0)
    @Column(name = "failed_login_attempts_counter", columnDefinition = "integer default 0")
    private Integer failedLoginAttemptsCounter = 0;

    public Account(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roleList.stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getAccessLevel().name()));
        });
        return authorities;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }

    @XmlTransient
    public Set<Role> getRoleList() {
        return roleList;
    }

    @XmlTransient
    public Set<ConfirmationToken> getConfirmationTokenList() {
        return confirmationTokenList;
    }
}