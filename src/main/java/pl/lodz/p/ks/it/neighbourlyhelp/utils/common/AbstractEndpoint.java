package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.lodz.p.ks.it.neighbourlyhelp.consistency.Signable;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractEndpoint {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private HttpServletRequest request;

    public boolean verifyIntegrity(Signable signable) { // TODO: 18.02.2022 change signer !!!
        String valueFromHeader = request.getHeader("If-Match");
        return encoder.matches(signable.getMessageToSign(), valueFromHeader);
    }
}