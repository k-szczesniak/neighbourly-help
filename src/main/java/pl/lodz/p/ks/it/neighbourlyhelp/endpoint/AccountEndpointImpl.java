package pl.lodz.p.ks.it.neighbourlyhelp.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.PasswordResetRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppOptimisticLockException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.IAccountMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountEndpointImpl extends AbstractEndpoint implements AccountEndpoint {

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
    @PermitAll
    public void confirmAccount(String token) throws AppBaseException {
        accountService.confirmToken(token);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<AccountDto> getAllAccounts() throws AppBaseException {
        List<Account> accounts = accountService.getAllAccounts();

        return accounts.stream()
                .map(account -> Mappers.getMapper(IAccountMapper.class).toAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
//    @PreAuthorize("isAnonymous()")
    // TODO: 16.02.2022 repair security annotation
    public void updateInvalidAuth(String email, String ipAddress, Date authDate) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        accountService.updateInvalidAuth(account, ipAddress, authDate);
    }

    @Override
    public void updateValidAuth(String email, String ipAddress, Date authDate) {
        String lang = servletRequest.getLocale().toString();
        Account account = accountService.getAccountByEmail(email);
        accountService.updateValidAuth(account, ipAddress, authDate, lang);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void blockAccount(String email) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(account);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountService.blockAccount(account);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void unblockAccount(String email) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(account);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountService.unblockAccount(account);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public AccountDto getOwnAccountInfo() {
        return Mappers.getMapper(IAccountMapper.class)
                .toAccountDto(accountService.getExecutorAccount());
    }

    @Override
    @Secured("ROLE_ADMIN")
    public AccountDto getAccountInfo(String email) {
        return Mappers.getMapper(IAccountMapper.class)
                .toAccountDto(accountService.getAccountByEmail(email));
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        Account editAccount = accountService.getExecutorAccount();
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        Mappers.getMapper(IAccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountService.editAccountDetails(editAccount);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void editOtherAccountDetails(String email, AccountPersonalDetailsDto accountPersonalDetailsDto) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        Mappers.getMapper(IAccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountService.editAccountDetails(editAccount);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changePassword(PasswordChangeRequestDto passwordChangeDto) throws AppOptimisticLockException {
        Account editAccount = accountService.getExecutorAccount();
        AccountDto accountIntegrity = Mappers.getMapper(IAccountMapper.class).toAccountDto(editAccount);
        if (!verifyIntegrity(accountIntegrity)) {
            throw AppOptimisticLockException.optimisticLockException();
        }
        accountService.changePassword(editAccount, passwordChangeDto);
    }

    @Override
    @PermitAll
    public void resetPassword(PasswordResetRequestDto passwordResetDto) throws AppBaseException {
        String password = passwordResetDto.getPassword();
        String token = passwordResetDto.getResetToken();
        accountService.resetPassword(password, token);
    }
}