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

    private ContractDto_Executor executor;

    private ContractDto_Publisher publisher;

    private ContractDto_Advert advert;

    private Date startDate;

    private Date finishDate;

    private String contractStatus;

    private Long version;

    @Data
    @AllArgsConstructor
    public static class ContractDto_Publisher {
        private Long id;
        private String firstName;
        private String lastName;
    }

    @Data
    @AllArgsConstructor
    public static class ContractDto_Executor {
        private Long id;
        private String firstName;
        private String lastName;
    }

    @Data
    @AllArgsConstructor
    public static class ContractDto_Advert {
        private Long id;
        private String title;
    }
}