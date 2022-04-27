package pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.domain.Account;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByContactNumber(String contactNumber);

    @Query("SELECT a FROM Account a WHERE a.enabled = false AND a.creationDate < :expirationDate")
    List<Account> findUnverifiedBefore(Date expirationDate);
}