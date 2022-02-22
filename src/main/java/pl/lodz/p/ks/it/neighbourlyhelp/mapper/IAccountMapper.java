package pl.lodz.p.ks.it.neighbourlyhelp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

@Mapper
public interface IAccountMapper {

    AccountDto toAccountDto(Account account);

    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);

    void toAccount(AccountPersonalDetailsDto detailsDto, @MappingTarget Account account) throws AppBaseException;
}
