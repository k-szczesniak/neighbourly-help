package pl.lodz.p.ks.it.neighbourlyhelp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.ks.it.neighbourlyhelp.domain.user.Account;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Account a SET a.accountRole = ADMIN WHERE a.email = ?1")
//    int addAdminPermissions(String email);
}
