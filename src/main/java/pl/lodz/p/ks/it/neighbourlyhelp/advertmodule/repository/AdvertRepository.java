package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AdvertRepository extends JpaRepository<Advert, Long> {
}