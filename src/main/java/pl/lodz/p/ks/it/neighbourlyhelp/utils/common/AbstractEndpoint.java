package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.Signable;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractEndpoint {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MessageSigner signer;

    public boolean verifyIntegrity(Signable signable) {
        String valueFromHeader = request.getHeader("If-Match");
        String valueFromSigner = signer.sign(signable);
        return valueFromSigner.equals(valueFromHeader);
    }
}