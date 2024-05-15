package me.mason.fake.autoconfig;

import me.mason.config.autoconfig.InventoryRepositoryConfig;
import me.mason.fake.FakeInventoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class StubAutoConfigurationTest {

    @DisplayName("설정을 true로 하면 FakeInventoryRepository 가 설정된다")
    @Test
    void fake_repository_set_true() {
        new ApplicationContextRunner()
                .withPropertyValues("fake.repository:true")
                .withUserConfiguration(InventoryRepositoryConfig.class)
                .run(context -> Assertions.assertThat(context).hasSingleBean(FakeInventoryRepository.class));
    }

    @DisplayName("설정을 false로 하면 FakeInventoryRepository 가 설정 안된다")
    @Test
    void fake_repository_set_false() {
        new ApplicationContextRunner()
                .withPropertyValues("fake.repository:false")
                .withUserConfiguration(InventoryRepositoryConfig.class)
                .run(context -> Assertions.assertThat(context).doesNotHaveBean(FakeInventoryRepository.class));
    }

}
