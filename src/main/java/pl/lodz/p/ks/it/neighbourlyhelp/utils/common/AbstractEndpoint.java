package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.Signable;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppOptimisticLockException;

public abstract class AbstractEndpoint {

    @Autowired
    private MessageSigner signer;

    public void verifyIntegrity(Signable signable, String eTag) throws AppOptimisticLockException {
        String valueFromSigner = signer.sign(signable);
        if(!valueFromSigner.equals(eTag)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
    }
}