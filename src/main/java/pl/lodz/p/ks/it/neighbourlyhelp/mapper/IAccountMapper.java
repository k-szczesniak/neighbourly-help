package pl.lodz.p.ks.it.neighbourlyhelp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.AccountPersonalDetailsDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

@Mapper
public interface IAccountMapper {

    AccountDto toAccountDto(Account account);

    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);

    void toAccount(AccountPersonalDetailsDto detailsDto, @MappingTarget Account account) throws AppBaseException;
}
