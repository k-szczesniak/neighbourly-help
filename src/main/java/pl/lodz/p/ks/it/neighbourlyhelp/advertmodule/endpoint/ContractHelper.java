package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

public interface ContractHelper {
    void createContract(NewContractRequestDto newContract) throws AppBaseException;

    void cancelContract(Long contractId, String ifMatch) throws AppBaseException;

    DetailContractDto get(Long contractId) throws AppBaseException;
}