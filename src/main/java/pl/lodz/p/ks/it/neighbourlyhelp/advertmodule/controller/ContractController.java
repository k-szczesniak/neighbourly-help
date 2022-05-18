package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.ApproveFinishedRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response.DetailContractDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.endpoint.ContractHelper;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractHelper contractHelper;

    private final MessageSigner messageSigner;

    @GetMapping("{id}")
    @Secured({"ROLE_CLIENT"})
    public ResponseEntity<DetailContractDto> get(@PathVariable("id") Long contractId) throws AppBaseException {
        DetailContractDto contractDto = contractHelper.get(contractId);
        return ResponseEntity.ok()
                .eTag(messageSigner.sign(contractDto))
                .body(contractDto);
    }

    @PostMapping
    @Secured({"ROLE_CLIENT"})
    public void createContract(@NotNull @Valid @RequestBody NewContractRequestDto newContract) throws AppBaseException {
        contractHelper.createContract(newContract);
    }

    @PatchMapping("/cancel/{contractId}")
    @Secured({"ROLE_CLIENT"})
    public void cancelContract(@RequestHeader("If-Match") String ifMatch,
                                   @PathVariable("contractId") Long contractId)
            throws AppBaseException {
        contractHelper.cancelContract(contractId, ifMatch);
    }

    @PatchMapping("/start/{contractId}")
    @Secured({"ROLE_CLIENT"})
    public void startContract(@RequestHeader("If-Match") String ifMatch,
                               @PathVariable("contractId") Long contractId)
            throws AppBaseException {
        contractHelper.startContract(contractId, ifMatch);
    }

    @PatchMapping("/end/{contractId}")
    @Secured({"ROLE_CLIENT"})
    public void endContract(@RequestHeader("If-Match") String ifMatch,
                              @PathVariable("contractId") Long contractId)
            throws AppBaseException {
        contractHelper.endContract(contractId, ifMatch);
    }

    @PatchMapping("/approve")
    @Secured({"ROLE_CLIENT"})
    public void approveFinishedContract(@RequestHeader("If-Match") String ifMatch,
                                        @NotNull @Valid @RequestBody ApproveFinishedRequestDto approveFinishedRequest)
            throws AppBaseException {
        contractHelper.approveFinishedContract(approveFinishedRequest, ifMatch);
    }
}