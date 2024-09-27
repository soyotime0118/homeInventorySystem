package me.mason.management;

import me.mason.fake.FakeInventoryRepository;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MyTestConfiguration {

    @Bean
    public ModifyInventoryStateOutput fakeInventoryRepository() {
        return new FakeInventoryRepository();
    }
}
