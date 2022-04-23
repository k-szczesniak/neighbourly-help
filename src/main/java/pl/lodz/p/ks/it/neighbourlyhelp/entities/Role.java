package pl.lodz.p.ks.it.neighbourlyhelp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(name = Role.ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT, columnNames = {"access_level", "account_id"})
}, indexes = {
        @Index(name = "ix_role_account_id", columnList = "account_id"),
        @Index(name = "ix_role_created_by", columnList = "created_by"),
        @Index(name = "ix_role_modified_by", columnList = "modified_by")
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@XmlRootElement
@NoArgsConstructor
public abstract class Role extends AbstractEntity {

    public static final String ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT = "uk_role_access_level_account_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role_id")
    @SequenceGenerator(name = "seq_role_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Column(name = "access_level", updatable = false, nullable = false, insertable = false)
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @Getter
    @Setter
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled = true;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    private Account account;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}