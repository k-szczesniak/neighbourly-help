package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.AdvertStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;

    private String title;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publication_date")
    private Date publicationDate;

    private String description;

    private AdvertStatus status;

    @Setter
    @JoinColumn(name = "account_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account publisher;


    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private City city;
}