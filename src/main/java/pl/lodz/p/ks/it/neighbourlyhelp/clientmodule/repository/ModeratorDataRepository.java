package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.City;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ModeratorDataRepository extends JpaRepository<ModeratorData, Long> {

    @Query("SELECT m.city FROM ModeratorData m WHERE m.account.email = :email")
    Optional<City> findCityByModeratorEmail(String email);
}