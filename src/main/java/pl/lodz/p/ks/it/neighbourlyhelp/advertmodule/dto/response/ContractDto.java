package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {

    private Long id;

    private Long executorId;

    private Long publisherId;

    private Long advertId;

    private Date startDate;

    private Date finishDate;

    private String contractStatus;

    private Long version;
}