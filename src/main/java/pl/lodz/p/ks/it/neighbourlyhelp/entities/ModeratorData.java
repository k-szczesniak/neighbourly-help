package pl.lodz.p.ks.it.neighbourlyhelp.entities;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "moderator_data")
//@Table(name = "manager_data", indexes = {
//        @Index(name = "ix_manager_data_hotel_id", columnList = "hotel_id")
//})
@DiscriminatorValue("MODERATOR")
@NoArgsConstructor
public class ModeratorData extends Role {

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this)
                .append(super.toString());
//        if (hotel != null) {
//            toStringBuilder.append("hotel", hotel.getId());
//        }
        return toStringBuilder.toString();
    }
}