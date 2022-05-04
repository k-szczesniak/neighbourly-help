package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.AdvertCategory;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.Description;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = Advert.TABLE_NAME, indexes = {
        @Index(columnList = "id", name = Advert.IX_ADVERT_ID, unique = true),
//        @Index(columnList = "email", name = Advert.IX_UQ_EMAIL, unique = true),
})
//, uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"email"}, name = Advert.IX_UQ_EMAIL),
//        @UniqueConstraint(columnNames = {"contact_number"}, name = Advert.IX_UQ_CONTACT_NUMBER)
//})
@Entity
public class Advert extends AbstractEntity {

    public static final String TABLE_NAME = "advert";
    public static final String IX_ADVERT_ID = "ix_advert_id";
//    public static final String IX_UQ_EMAIL = "ix_uq_email";
//    public static final String IX_UQ_CONTACT_NUMBER = "ix_uq_contact_number";
//    public static final String ROLE_PREFIX = "ROLE_";

    @Id
    @SequenceGenerator(
            name = "advert_sequence",
            sequenceName = "advert_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "advert_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Setter
    @NotNull
    @NonNull
    @Size(min = 1, max = 31)
    @Column(name = "title", length = 31, nullable = false)
    private String title;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publication_date")
    private Date publicationDate;

    @Setter
    @NotNull
    @NonNull
    @Size(min = 1, max = 255)
    @Column(name = "description", length = 255, nullable = false)
    @Description
    private String description;

    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", nullable = false)
    private AdvertCategory category;

    @Setter
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account publisher;

    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "delete")
    private boolean delete = false;

    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private City city;
}