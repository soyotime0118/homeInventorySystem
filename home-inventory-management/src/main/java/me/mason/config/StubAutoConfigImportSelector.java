package me.mason.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class StubAutoConfigImportSelector implements DeferredImportSelector {

    private final ClassLoader classLoader;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Boolean enableStub = (Boolean) importingClassMetadata.getAnnotationAttributes(EnableStubAutoConfiguration.class.getName())
                .get("enableStub");
        if (enableStub) {
            ImportCandidates candidates = ImportCandidates.load(StubAutoConfiguration.class, classLoader);
            return StreamSupport.stream(candidates.spliterator(), false).toArray(String[]::new);
        } else {
            return new String[]{};
        }
    }
}
