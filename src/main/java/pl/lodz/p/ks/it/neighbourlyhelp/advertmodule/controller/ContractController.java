package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.ContractHelper;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractHelper contractHelper;

    @PostMapping
    @Secured({"ROLE_CLIENT"})
    public void createContract(@NotNull @Valid @RequestBody NewContractRequestDto newContract) throws AppBaseException {
        contractHelper.createContract(newContract);
    }

}