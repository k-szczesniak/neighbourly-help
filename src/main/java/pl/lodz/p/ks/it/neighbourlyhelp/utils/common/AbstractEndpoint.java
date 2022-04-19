package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.Signable;

public abstract class AbstractEndpoint {

    @Autowired
    private MessageSigner signer;

    public boolean verifyIntegrity(Signable signable, String eTag) {
        String valueFromSigner = signer.sign(signable);
        return valueFromSigner.equals(eTag);
    }
}