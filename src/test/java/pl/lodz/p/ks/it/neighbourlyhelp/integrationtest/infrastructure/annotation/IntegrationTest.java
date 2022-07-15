package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.configuration.TestConfig;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container.PostgresqlExtension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(PostgresqlExtension.class)
@SpringBootTest(classes = TestConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Inherited
public @interface IntegrationTest {
}