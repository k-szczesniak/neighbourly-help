package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.PreferableSettlement;
import pl.lodz.p.ks.it.neighbourlyhelp.utils.consistency.Signable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertDetailsResponseDto implements Signable {

    private Long id;

    private String title;

    private Date publicationDate;

    private Date modificationDate;

    private String description;

    private String category;

    private AdvertDetails_Publisher publisher;

    private AdvertDetails_City city;

    private boolean approved;

    private Set<PreferableSettlement> preferableSettlementList;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%s", id, version, category);
    }

    @Data
    @AllArgsConstructor
    public static class AdvertDetails_Publisher {
        private Long id;
        private String avatarSrc;
        private String firstName;
        private String lastName;
        private String email;
        private String contactNumber;
        private BigDecimal rating;
    }

    @Data
    @AllArgsConstructor
    public static class AdvertDetails_City {
        private Long id;
        private String name;
    }
}