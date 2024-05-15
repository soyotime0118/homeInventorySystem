package me.mason.config;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(StubAutoConfigImportSelector.class)
public @interface EnableStubAutoConfiguration {

    @AliasFor("enableStub")
    boolean value() default false;

    @AliasFor("value")
    boolean enableStub() default false;

}
