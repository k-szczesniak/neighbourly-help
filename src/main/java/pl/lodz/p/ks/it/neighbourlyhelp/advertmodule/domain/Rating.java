package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Rating.CONTRACT_ID_CONSTRAINT;

@Getter
@Table(name = Rating.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = CONTRACT_ID_CONSTRAINT, columnNames = {"contract_id"})
},
        indexes = {
                @Index(name = "ix_rating_contract_id", columnList = "contract_id"),
                @Index(name = "ix_rating_created_by", columnList = "created_by"),
                @Index(name = "ix_rating_modified_by", columnList = "modified_by"),
        })
@NoArgsConstructor
@Entity
public class Rating extends AbstractEntity {

    public static final String TABLE_NAME = "rating";

    public static final String CONTRACT_ID_CONSTRAINT = "uk_rating_contract_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rating_id")
    @SequenceGenerator(name = "seq_rating_id", allocationSize = 1)
    @Column(updatable = false)
    private Long id;

    @Min(1)
    @Max(5)
    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "rate")
    private short rate;

    @Setter
    @Size(min = 4, max = 255)
    @Column(name = "comment")
    private String comment;

    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "hidden")
    private boolean hidden = false;

    @Getter
    @Setter
    @OneToOne(optional = false)
    private Contract contract;
}