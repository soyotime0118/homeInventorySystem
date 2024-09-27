package me.mason.config.autoconfig;

import lombok.extern.slf4j.Slf4j;
import me.mason.fake.FakeInventoryRepository;
import me.mason.config.StubAutoConfiguration;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import org.springframework.context.annotation.Bean;

@Slf4j
@StubAutoConfiguration
public class InventoryRepositoryConfig {

    @Bean(name = "fakeInventoryRepository")
    public ModifyInventoryStateOutput fakeInventoryRepository() {
        log.info("================================");

        return new FakeInventoryRepository();
    }
}
