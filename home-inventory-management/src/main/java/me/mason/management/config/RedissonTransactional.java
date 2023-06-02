package me.mason.management.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonTransactional {


    /**
     * 락을 점유할 수 있는 최대 시간, 단위는 초(Second)이다
     */
    long leaseTimeSecond() default 3;

    /**
     * 락을 점유하기 위해 대기할 수 있는 최대 시간. 단위는 초(Second)이다
     */
    long waitTime() default 1;

    /**
     * 단일 Lock과 멀티락 설정 여부
     */
    boolean isMultiLock() default false;

    /**
     * 설정할 키 이름
     */
    String keyName();

//    String lockKeyMethod();

}