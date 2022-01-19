package pl.lodz.p.ks.it.neighbourlyhelp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;

@Mapper
public interface IAccountMapper {

    @Mapping(source = "accountRole", target = "accountRole")
    AccountDto toAccountDto(Account account);
}
