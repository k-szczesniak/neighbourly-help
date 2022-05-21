package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.ApproveFinishedRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.ContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import java.util.List;

public interface ContractHelper {
    void createContract(NewContractRequestDto newContract) throws AppBaseException;

    void cancelContract(Long contractId, String ifMatch) throws AppBaseException;

    DetailContractDto get(Long contractId) throws AppBaseException;

    void startContract(Long contractId, String ifMatch) throws AppBaseException;

    void endContract(Long contractId, String ifMatch) throws AppBaseException;

    void approveFinishedContract(ApproveFinishedRequestDto approveFinishedRequestDto, String ifMatch) throws AppBaseException;

    List<ContractDto> getMyActiveContract() throws AppBaseException;

    List<ContractDto> getDelegateActiveContracts() throws AppBaseException;

    List<ContractDto> getMyFinishedContract() throws AppBaseException;

    List<ContractDto> getDelegateFinishedContracts() throws AppBaseException;

    List<ContractDto> getUnratedFinishedContracts() throws AppBaseException;

    List<ContractDto> getMyUnpaidFinishedContracts() throws AppBaseException;

    List<ContractDto> getDelegateUnpaidFinishedContracts() throws AppBaseException;
}