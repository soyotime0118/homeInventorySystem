package me.mason.management.config.aop;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.exception.InventoryManagementException;
import me.mason.management.config.RedissonTransactional;
import me.mason.management.domain.Inventory.InventoryId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;


    @Autowired
    public RedissonLockAspect(
            RedissonClient redissonClient,
            PlatformTransactionManager transactionManager) {
        this.redissonClient = redissonClient;
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(me.mason.management.config.RedissonTransactional) && args(arg,..)")
    public Object execute(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonTransactional redissonTransactional = method.getAnnotation(RedissonTransactional.class);

        RLock lock = null;
        if (arg instanceof InventoryId) {
            lock = redissonClient.getLock(String.valueOf(((InventoryId) arg).getValue()));
//            log.info("lock name : " + lock.getName());
        }

        long leaseTime = redissonTransactional.leaseTimeSecond();
        long waitTime = redissonTransactional.waitTime();

        try {
            //락 획득을 시도하되, waitTime 만큼 대기하고, 한번 잡으면 최대 leaseTime 만큼 점유한다. leaseTime을 넘어가면, 자동 해제한다.
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)) {
                return executeWithTransaction(joinPoint);
            } else {
                //여기 오면, 아예 DB실행쿼리가 동작 안한다
//                log.info("락 획득실패");
                throw new InventoryManagementException("락 획득에 실패했습니다, 재시도하세요");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("락 인터럽트");
            throw new RuntimeException("락 획득에 실패했습니다");
        } finally {
            //여기왔을때, 반드시 락이 걸렸다는 보장이 없다
//            try {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
//            } catch (IllegalMonitorStateException e) {
//                //적절한 로그 처리
//                // forceUnlock 하면 안된다. rLock.unlock에서 이미 락이 풀린상태다
//                e.printStackTrace();
//                log.info("락 발생!! " + e.getMessage());
//            }
        }
    }

    private Object executeWithTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Throwable e) {
            log.info("롤백발생");
            transactionManager.rollback(status);
            throw e;
        }
    }
}
