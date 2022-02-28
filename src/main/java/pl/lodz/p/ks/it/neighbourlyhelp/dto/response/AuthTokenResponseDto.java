package pl.lodz.p.ks.it.neighbourlyhelp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}