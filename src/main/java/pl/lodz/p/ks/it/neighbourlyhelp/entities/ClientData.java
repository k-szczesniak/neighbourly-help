package pl.lodz.p.ks.it.neighbourlyhelp.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_data")
@DiscriminatorValue("CLIENT")
@NoArgsConstructor
@ToString(callSuper = true)
public class ClientData extends Role {
}