package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.LoyaltyPoint;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = ClientData.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = ClientData.LOYALTY_POINT_ID_CONSTRAINT, columnNames = {"loyalty_point_id"})
})
@DiscriminatorValue("CLIENT")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientData extends Role {

    public static final String TABLE_NAME = "client_data";

    public static final String LOYALTY_POINT_ID_CONSTRAINT = "uk_client_data_loyalty_point_id";

    @Getter
    @Setter
    @OneToOne(optional = false)
    private LoyaltyPoint loyaltyPoint;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this)
                .append(super.toString());
        if (loyaltyPoint != null) {
            toStringBuilder.append("totalPoints", loyaltyPoint.getTotalPoints());
            toStringBuilder.append("blockedPoints", loyaltyPoint.getBlockedPoints());
        }
        return toStringBuilder.toString();
    }

}