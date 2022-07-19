package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.ks.it.neighbourlyhelp.NeighbourlyHelpApplication;

@Configuration
@Import(NeighbourlyHelpApplication.class)
public class TestConfig {
}