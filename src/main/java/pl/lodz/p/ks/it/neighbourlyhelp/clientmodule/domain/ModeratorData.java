package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "moderator_data")
//@Table(name = "manager_data", indexes = {
//        @Index(name = "ix_manager_data_hotel_id", columnList = "hotel_id")
//})
@DiscriminatorValue("MODERATOR")
@NoArgsConstructor
public class ModeratorData extends Role {

    @Getter
    @Setter
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private City city;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this)
                .append(super.toString());
        if (city != null) {
            toStringBuilder.append("hotel", city.getId());
        }
        return toStringBuilder.toString();
    }
}