package pl.lodz.p.ks.it.neighbourlyhelp.consistency;

import java.beans.Transient;

public interface Signable {

    @Transient
    String getMessageToSign();
}