package me.mason.config.autoconfig;

import lombok.extern.slf4j.Slf4j;
import me.mason.fake.FakeInventoryRepository;
import me.mason.fake.StubAutoConfiguration;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Slf4j
@StubAutoConfiguration
public class InventoryRepositoryConfig {

    @Bean(name = "fakeInventoryRepository")
    @ConditionalOnProperty(name = "fake.repository", havingValue = "true")
    public ModifyInventoryStateOutput fakeInventoryRepository() {
        log.info("================================");

        return new FakeInventoryRepository();
    }
}
