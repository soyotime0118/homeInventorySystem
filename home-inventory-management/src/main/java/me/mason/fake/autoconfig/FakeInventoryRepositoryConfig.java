package me.mason.fake.autoconfig;

import lombok.extern.slf4j.Slf4j;
import me.mason.fake.FakeInventoryRepository;
import me.mason.fake.StubAutoConfiguration;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.context.annotation.Bean;

@Slf4j
@StubAutoConfiguration
public class FakeInventoryRepositoryConfig {

    @Bean
    public ModifyInventoryStateOutput fakeInventoryRepository() {
        log.info("================================");

        return new FakeInventoryRepository();

    }
}
