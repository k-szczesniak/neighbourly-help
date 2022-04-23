package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RolesDto implements Signable {
    
    private Long userId;
    
    private List<SingleRoleDto> rolesRevoked;

    @Getter // TODO: 18.02.2022 necessary getter and setter? 
    @Setter
    private List<SingleRoleDto> rolesGranted;
    
    @Override
    public String getMessageToSign() {
        return String.format("%d;%s;%s", userId, stringify(rolesGranted), stringify(rolesRevoked));
    }

    private static String stringify(List<SingleRoleDto> list) {
        return list.stream().map(x -> x.roleName + ":" + x.version).collect(Collectors.joining("|"));
    }

    /**
     * DTO class which represent single role assign to application user
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SingleRoleDto {
        private String roleName;
        private Long version;
    }
}