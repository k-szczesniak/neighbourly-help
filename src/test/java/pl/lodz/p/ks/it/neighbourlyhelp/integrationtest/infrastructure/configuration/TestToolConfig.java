package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.IntegrationTestTool;

@Lazy // allows for initialization after the server port is known
@TestConfiguration(proxyBeanMethods = false)
public class TestToolConfig {
    @Bean
    public IntegrationTestTool integrationTestTool(@Value("${local.server.port}") int port) {
        return new IntegrationTestTool(port);
    }
}