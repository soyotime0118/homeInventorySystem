package me.mason.fake.autoconfig;

import me.mason.config.EnableStubAutoConfiguration;
import me.mason.fake.FakeInventoryRepository;
import me.mason.management.adapter.out.persistent.InventoryPersistentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class StubAutoConfigurationTest {

    @Configuration
    @EnableStubAutoConfiguration(enableStub = true)
    static class EnableStubAutoTRUEConfiguration {
    }
    @Configuration
    @Import(InventoryPersistentRepository.class)
    @EnableStubAutoConfiguration
    static class EnableStubAutoFalseConfiguration {
    }

    @DisplayName("설정을 true로 하면 FakeInventoryRepository 가 설정된다")
    @Test
    void testFakeRepositoryTrue() {
        new ApplicationContextRunner()
                .withPropertyValues("fake.repository:true")
                .withUserConfiguration(EnableStubAutoTRUEConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(FakeInventoryRepository.class);
                    assertThat(context).doesNotHaveBean(InventoryPersistentRepository.class);
                });
    }

    @DisplayName("설정을 false로 하면 FakeInventoryRepository 가 설정 안된다")
    @Test
    void testFakeRepositoryFalse() {
        new ApplicationContextRunner()
                .withPropertyValues("fake.repository:false")
                .withUserConfiguration(EnableStubAutoFalseConfiguration.class)
                .run(context -> {
                    assertThat(context).doesNotHaveBean(FakeInventoryRepository.class);
                    assertThat(context).hasSingleBean(InventoryPersistentRepository.class);
                });
    }

}
