package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.ModeratorData;

@Repository
@Transactional(readOnly = true)
public interface ModeratorDataRepository extends JpaRepository<ModeratorData, Long> {
}