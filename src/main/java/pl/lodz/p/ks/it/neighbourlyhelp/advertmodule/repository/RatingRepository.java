package pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Rating;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT DISTINCT r FROM Rating r, Contract c WHERE r.contract.id = c.id AND (c.executor.id = :accountId OR c.advert.publisher.id = :accountId)")
    List<Rating> findAllByAccountId(Long accountId);
}