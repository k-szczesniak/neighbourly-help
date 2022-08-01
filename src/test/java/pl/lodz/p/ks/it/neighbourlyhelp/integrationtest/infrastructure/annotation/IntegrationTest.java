package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.configuration.TestConfig;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.configuration.TestToolConfig;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container.PostgresqlExtension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(PostgresqlExtension.class)
@SpringBootTest(classes = {TestConfig.class, TestToolConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Inherited
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/test-init.sql")
public @interface IntegrationTest {
}