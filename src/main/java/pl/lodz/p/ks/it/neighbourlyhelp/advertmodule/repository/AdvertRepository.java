package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {
}