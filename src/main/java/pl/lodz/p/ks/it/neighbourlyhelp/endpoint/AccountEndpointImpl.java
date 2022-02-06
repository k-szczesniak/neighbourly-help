package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.IAccountMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.service.AccountService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountEndpointImpl implements AccountEndpoint {

    private final HttpServletRequest servletRequest;

    private final AccountService accountService;

    @Override
    @PermitAll
    public void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException {
        Account account = new Account();
        Mappers.getMapper(IAccountMapper.class).toAccount(registerAccountDto, account);
        account.setLanguage(servletRequest.getLocale().getLanguage().toLowerCase());
        accountService.register(account);
    }

    @Override
    public void confirmAccount(String token) throws AppBaseException {
        accountService.confirmToken(token);
    }
}