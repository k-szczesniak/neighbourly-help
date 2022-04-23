package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppOptimisticLockException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

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