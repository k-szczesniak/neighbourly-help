package pl.lodz.p.ks.it.neighbourlyhelp.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEntity;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = ConfirmationToken.TABLE_NAME, indexes = {
        @Index(columnList = "id", name = ConfirmationToken.IX_ACCOUNT_ID, unique = true),
        @Index(columnList = "token", name = ConfirmationToken.IX_UQ_TOKEN, unique = true),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"}, name = ConfirmationToken.IX_UQ_TOKEN)
})
@Entity
@ToString(callSuper = true)
public class ConfirmationToken extends AbstractEntity {

    public static final String TABLE_NAME = "confirmation_token";
    public static final String IX_ACCOUNT_ID = "ix_account_id";
    public static final String IX_UQ_TOKEN = "ix_uq_token";

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    @Setter
    @NotNull
//    @PenCode
    @Basic(optional = false)
    @Column(name = "token", nullable = false, updatable = false)
    private String token;

    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "used", nullable = false)
    private boolean used;

    @Setter
    @NotNull
    @Basic(optional = false)
    @Column(name = "send_attempt", nullable = false)
    private int sendAttempt = 0;

    @Setter
    @ManyToOne
//    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Account account;

    @Setter
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    @Setter
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Setter
    private LocalDateTime confirmedAt; // TODO: 06.02.2022 the same date as modificationDate from abstract -> maybe remove

    @Builder
    public ConfirmationToken(String token, boolean used, TokenType tokenType, Account account, Account createdBy, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.used = used;
        this.tokenType = tokenType;
        this.account = account;
        this.setCreatedBy(createdBy);
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("id", id)
                .toString();
    }
}