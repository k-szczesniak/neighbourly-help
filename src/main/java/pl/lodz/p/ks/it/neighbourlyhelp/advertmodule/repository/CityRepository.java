package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);
}