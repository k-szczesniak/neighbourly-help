package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain;

import lombok.Getter;
import lombok.Setter;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Entity
public class Contract extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contract_id")
    @SequenceGenerator(name = "seq_contract_id", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;



    @Setter
    @JoinColumn(name = "advert_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true)
    private Advert advert;


    @NotNull
    @Setter
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Setter
    @Basic(optional = false)
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
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "contract")
    private LoyaltyPoint loyaltyPoint;

    @Setter
    @JoinColumn(name = "executor_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private Account executor;


}