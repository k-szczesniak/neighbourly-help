package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Table(name = Contract.TABLE_NAME, indexes = {
        @Index(name = "ix_contract_executor_id", columnList = "executor_id"),
        @Index(name = "ix_contract_created_by", columnList = "created_by"),
        @Index(name = "ix_contract_advert_id", columnList = "advert_id")
})
@Check(constraints = "start_date < finish_date")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Contract extends AbstractEntity {

    public static final String TABLE_NAME = "contract";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contract_id")
    @SequenceGenerator(name = "seq_contract_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Advert advert;

    @Setter
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Setter
    @Basic(optional = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finish_date")
    private Date finishDate;

    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Setter
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "contract")
    private Rating rating;

    @Setter
    @JoinColumn(name = "executor_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account executor;

    @Setter
    @OneToOne
    @JoinColumn(name = "contract_ended_by")
    private Account contractEndedBy;

}