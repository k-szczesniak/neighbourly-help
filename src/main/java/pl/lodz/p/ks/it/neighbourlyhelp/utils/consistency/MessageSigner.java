package pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class MessageSigner {

    private final SecretKey secretKey;

    public String sign(Signable signable) {
        return Jwts.builder()
                .setPayload(signable.getMessageToSign())
                .signWith(secretKey)
                .compact();
    }
}
