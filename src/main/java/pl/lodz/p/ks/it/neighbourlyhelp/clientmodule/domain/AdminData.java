package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin_data")
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
@ToString(callSuper = true)
public class AdminData extends Role {
}