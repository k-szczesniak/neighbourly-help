package pl.lodz.p.ks.it.neighbourlyhelp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.AccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.dto.RegisterAccountDto;

@Mapper
public interface IAccountMapper {

//    @Mapping(source = "accountRole", target = "accountRole")
    AccountDto toAccountDto(Account account);

    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);
}
