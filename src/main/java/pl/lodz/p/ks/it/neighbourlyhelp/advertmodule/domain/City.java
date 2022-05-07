package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.CityName;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.advertmodule.SimplyCityName;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Table(name = City.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = City.CITY_CONSTRAINT, columnNames = {"name"})
})
@NoArgsConstructor
@Getter
@Entity
public class City extends AbstractEntity {

    public static final String TABLE_NAME = "city";
    public static final String CITY_CONSTRAINT = "uk_city_name";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_city_id")
    @SequenceGenerator(name = "seq_city_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Setter
    @NotNull
    @NonNull
    @Basic(optional = false)
    @Size(min = 1, max = 31)
    @Column(name = "name")
    @CityName
    private String name;

    @Setter
    @NotNull
    @NonNull
    @Basic(optional = false)
    @Size(min = 1, max = 31)
    @Column(name = "simply_name")
    @SimplyCityName
    private String simplyName;

    @Setter
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "city")
    private Set<Advert> advertList = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "city", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<ModeratorData> moderatorDataList = new HashSet<>();
}