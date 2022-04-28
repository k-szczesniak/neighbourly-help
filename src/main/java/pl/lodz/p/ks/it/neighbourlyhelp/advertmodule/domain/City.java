package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = City.TABLE_NAME)
@NoArgsConstructor
@Getter
public class City {

    public static final String TABLE_NAME = "city";

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
}