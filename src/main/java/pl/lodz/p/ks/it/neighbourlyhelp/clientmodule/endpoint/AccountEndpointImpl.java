package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.ThemeColor;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordChangeOtherRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordChangeRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.PasswordResetRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.BasicAccountInformationDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.AccountService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.mapper.AccountMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
@Log
public class AccountEndpointImpl extends AbstractEndpoint implements AccountEndpoint {

    private final HttpServletRequest servletRequest;

    private final AccountService accountService;

    @Override
    @PermitAll
    public void registerAccount(RegisterAccountDto registerAccountDto) throws AppBaseException {
        Account account = new Account();
        Mappers.getMapper(AccountMapper.class).toAccount(registerAccountDto, account);
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
                .map(account -> Mappers.getMapper(AccountMapper.class).toAccountDto(account))
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
    public void updateValidAuth(String email, String ipAddress, Date authDate) throws AppBaseException {
        String lang = servletRequest.getLocale().toString();
        Account account = accountService.getAccountByEmail(email);
        accountService.updateValidAuth(account, ipAddress, authDate, lang);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void blockAccount(String email, String ifMatch) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(account);
        verifyIntegrity(accountIntegrity, ifMatch);
        accountService.blockAccount(account);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void unblockAccount(String email, String ifMatch) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(account);
        verifyIntegrity(accountIntegrity, ifMatch);
        accountService.unblockAccount(account);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public AccountDto getOwnAccountInfo() throws AppBaseException {
        return Mappers.getMapper(AccountMapper.class)
                .toAccountDto(accountService.getExecutorAccount());
    }

    @Override
    @Secured("ROLE_ADMIN")
    public AccountDto getAccountInfo(String email) throws AppBaseException {
        return Mappers.getMapper(AccountMapper.class)
                .toAccountDto(accountService.getAccountByEmail(email));
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public BasicAccountInformationDto get(Long accountId) throws AppBaseException {
        return Mappers.getMapper(AccountMapper.class)
                .toBasicAccountInformationDto(accountService.getAccountById(accountId));
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editOwnAccountDetails(AccountPersonalDetailsDto accountPersonalDetailsDto, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getExecutorAccount();
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(editAccount);
        verifyIntegrity(accountIntegrity, ifMatch);
        Mappers.getMapper(AccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountService.editAccountDetails(editAccount);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void editOtherAccountDetails(String email, AccountPersonalDetailsDto accountPersonalDetailsDto, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(email);
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(editAccount);
        verifyIntegrity(accountIntegrity, ifMatch);
        Mappers.getMapper(AccountMapper.class).toAccount(accountPersonalDetailsDto, editAccount);
        accountService.editAccountDetails(editAccount);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changePassword(PasswordChangeRequestDto passwordChangeDto, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getExecutorAccount();
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(editAccount);
        verifyIntegrity(accountIntegrity, ifMatch);

        accountService.changePassword(editAccount, passwordChangeDto);
    }

    @Override
    @PermitAll
    public void resetPassword(PasswordResetRequestDto passwordResetDto) throws AppBaseException {
        String password = passwordResetDto.getPassword();
        String token = passwordResetDto.getResetToken();
        accountService.resetPassword(password, token);
    }

    @Override
    @PermitAll
    public void sendResetPasswordRequest(String email) throws AppBaseException {
        accountService.sendResetPasswordRequest(email);
    }

    @Override
    public void changeOtherPassword(PasswordChangeOtherRequestDto passwordChangeOtherDto, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getAccountByEmail(passwordChangeOtherDto.getEmail());
        AccountDto accountIntegrity = Mappers.getMapper(AccountMapper.class).toAccountDto(editAccount);
        verifyIntegrity(accountIntegrity, ifMatch);

        accountService.changeOtherPassword(editAccount, passwordChangeOtherDto.getGivenPassword());
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void editOwnLanguage(String language, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getExecutorAccount();

        accountService.changeAccountLanguage(editAccount, language);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changeThemeColor(ThemeColor themeColor, String ifMatch) throws AppBaseException {
        Account editAccount = accountService.getExecutorAccount();

        accountService.changeThemeColor(editAccount, themeColor);
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public void changeOwnAccessLevel(AccessLevel accessLevel) throws AppBaseException {
        log.log(Level.INFO, String.format("User %s change access level on: %s",
                accountService.getExecutorAccount().getEmail(),
                accessLevel.toString()));
    }
}