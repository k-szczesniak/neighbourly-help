package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Table(name = Advert.TABLE_NAME, indexes = {
        @Index(columnList = "id", name = "ix_advert_id", unique = true),
        @Index(columnList = "publisher_id", name = "ix_advert_publisher_id"),
        @Index(columnList = "city_id", name = "ix_advert_city_id")
})
@Entity
public class Advert extends AbstractEntity {

    public static final String TABLE_NAME = "advert";

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
    @JoinColumn(name = "publisher_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account publisher;

    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private City city;

    @Setter
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "advert")
    private Set<Contract> contractList = new HashSet<>();

    @Setter
    @NotNull
    @NonNull
    @Column(name = "prize")
    private BigInteger prize;

    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "approved")
    private boolean approved = false;
}