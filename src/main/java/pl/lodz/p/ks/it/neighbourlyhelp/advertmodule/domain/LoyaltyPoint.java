package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.LoyaltyPointStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;


@Getter
@Table(name = LoyaltyPoint.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = LoyaltyPoint.CONTRACT_ID_CONSTRAINT, columnNames = {"contract_id"})
},
        indexes = {
                @Index(name = "ix_loyalty_point_contract_id", columnList = "contract_id"),
                @Index(name = "ix_loyalty_point_created_by", columnList = "created_by"),
                @Index(name = "ix_loyalty_point_modified_by", columnList = "modified_by"),
        })
@NoArgsConstructor
@Entity
public class LoyaltyPoint extends AbstractEntity {

    public static final String TABLE_NAME = "loyalty_point";

    public static final String CONTRACT_ID_CONSTRAINT = "uk_loyalty_point_contract_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_loyalty_point_id")
    @SequenceGenerator(name = "seq_loyalty_point_id", allocationSize = 1)
    @Column(updatable = false)
    private Long id;

    @Getter
    @Setter
    @OneToOne(optional = false)
    private Contract contract;

    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private LoyaltyPointStatus status;

    @Setter
    @Size(min = 4, max = 255)
    @Column(name = "comment")
    private String comment;
}