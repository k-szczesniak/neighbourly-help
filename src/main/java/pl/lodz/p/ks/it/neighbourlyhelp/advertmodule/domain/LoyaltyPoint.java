package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ClientData;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigInteger;


@Getter
@Table(name = LoyaltyPoint.TABLE_NAME)
@NoArgsConstructor
@Entity
public class LoyaltyPoint extends AbstractEntity {

    public static final String TABLE_NAME = "loyalty_point";

    public static final String CLIENT_ID_CONSTRAINT = "uk_loyalty_point_client_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_loyalty_point_id")
    @SequenceGenerator(name = "seq_loyalty_point_id", allocationSize = 1)
    @Column(updatable = false)
    private Long id;

    @Setter
    @Column(name = "total_points")
    private BigInteger totalPoints;

    @Setter
    @Column(name = "blocked_points")
    private BigInteger blockedPoints;

    @Setter
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "loyaltyPoint")
    private ClientData client;

    @Builder
    public LoyaltyPoint(BigInteger totalPoints, BigInteger blockedPoints, ClientData client) {
        this.totalPoints = totalPoints;
        this.blockedPoints = blockedPoints;
        this.client = client;
    }
}