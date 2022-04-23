package pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency;

import java.beans.Transient;

public interface Signable {

    @Transient
    String getMessageToSign();
}