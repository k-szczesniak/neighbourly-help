package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.enums.AccessLevel;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.ModeratorDataDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.RolesDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.endpoint.RoleEndpoint;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.MessageSigner;
import pl.lodz.p.ks.it.neighbourlyhelp.validator.clientmodule.Email;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleEndpoint roleEndpoint;

    private final MessageSigner messageSigner;

    @PatchMapping("/user/{email}/grant/{accessLevel}")
    @Secured("ROLE_ADMIN")
    public void grantAccessLevel(@RequestHeader("If-Match") String ifMatch,
                                 @NotNull @Email @PathVariable(name = "email") @Valid String email,
                                 @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.grantAccessLevel(email, accessLevel, ifMatch);
    }

    @PatchMapping("/user/{email}/revoke/{accessLevel}")
    @Secured("ROLE_ADMIN")
    public void revokeAccessLevel(@RequestHeader("If-Match") String ifMatch,
                                  @NotNull @Email @PathVariable(name = "email") @Valid String email,
                                  @NotNull @PathVariable(name = "accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.revokeAccessLevel(email, accessLevel, ifMatch);
    }

    @GetMapping("/self")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_CLIENT"})
    public ResponseEntity<RolesDto> getSelfRole() throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole();

        return ResponseEntity.ok()
                .eTag(messageSigner.sign(rolesDto))
                .body(rolesDto);
    }

    @GetMapping("/{email}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RolesDto> getUserRole(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole(email);

        return ResponseEntity.ok()
                .eTag(messageSigner.sign(rolesDto))
                .body(rolesDto);
    }

    @GetMapping("/moderator/{email}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ModeratorDataDto> getModeratorData(@NotNull @Email @PathVariable("email") @Valid String email) throws AppBaseException {
        ModeratorDataDto moderatorDataDto = roleEndpoint.getModeratorData(email);

        return ResponseEntity.ok()
                .eTag(messageSigner.sign(moderatorDataDto))
                .body(moderatorDataDto);
    }
}