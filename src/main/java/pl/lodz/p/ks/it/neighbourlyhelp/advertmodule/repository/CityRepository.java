package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);
}