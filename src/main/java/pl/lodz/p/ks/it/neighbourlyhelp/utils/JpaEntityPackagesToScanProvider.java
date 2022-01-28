package pl.lodz.p.ks.it.neighbourlyhelp.utils;

public class JpaEntityPackagesToScanProvider {

    public String[] provide() {
        return new String[] {
                "pl.lodz.p.ks.it.neighbourlyhelp.domain"
        };
    }
}
