package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.mapper.ContractMapper;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.service.ContractService;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.ContractException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.common.AbstractEndpoint;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AppBaseException.class)
public class ContractHelperImpl extends AbstractEndpoint implements ContractHelper {

    private final ContractService contractService;

    @Override
    @Secured({"ROLE_CLIENT"})
    public DetailContractDto get(Long contractId) throws AppBaseException {
        Contract contract = contractService.get(contractId);

        if (getEmail().equals(contract.getExecutor().getEmail())) {
            return Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
        } else {
            throw ContractException.accessDenied();
        }
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void createContract(NewContractRequestDto newContract) throws AppBaseException {
        contractService.createContract(newContract);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void cancelContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);

        if (getEmail().equals(contract.getExecutor().getEmail())) {
            DetailContractDto contractIntegrity = Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
            verifyIntegrity(contractIntegrity, ifMatch);
            contractService.cancelContract(contract);
        } else {
            throw ContractException.accessDenied();
        }
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void startContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);
        verifyPrivilegesAndIntegrity(() -> contractService.startContract(contract), contract, ifMatch);
    }

    @Override
    @Secured({"ROLE_CLIENT"})
    public void endContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);
        verifyPrivilegesAndIntegrity(() -> contractService.endContract(contract), contract, ifMatch);
    }

    @Override
    public void approveFinishedContract(Long contractId, String ifMatch) throws AppBaseException {
        Contract contract = contractService.get(contractId);
        verifyPrivilegesAndIntegrity(() -> contractService.approveFinishedContract(contract), contract, ifMatch);
    }

    private void verifyPrivilegesAndIntegrity(VoidMethodExecutor executor, Contract contract, String ifMatch) throws AppBaseException {
        if (getEmail().equals(contract.getExecutor().getEmail()) ||
                getEmail().equals(contract.getAdvert().getPublisher().getEmail())) {
            DetailContractDto contractIntegrity = Mappers.getMapper(ContractMapper.class).toDetailContractDto(contract);
            verifyIntegrity(contractIntegrity, ifMatch);
            executor.run();
        } else {
            throw ContractException.accessDenied();
        }
    }

    @FunctionalInterface
    public interface VoidMethodExecutor {
        void run() throws AppBaseException;
    }
}