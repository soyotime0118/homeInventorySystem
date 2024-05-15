package me.mason.config.autoconfig;

import lombok.extern.slf4j.Slf4j;
import me.mason.fake.FakeInventoryRepository;
import me.mason.fake.StubAutoConfiguration;
import me.mason.management.adapter.out.persistent.InventoryJpaRepository;
import me.mason.management.adapter.out.persistent.InventoryMapper;
import me.mason.management.adapter.out.persistent.InventoryPersistentRepository;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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
