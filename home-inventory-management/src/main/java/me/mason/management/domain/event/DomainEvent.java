package me.mason.management.domain.event;

import java.time.LocalDateTime;

/**
 * 모든 도메인 이벤트의 기본 인터페이스
 */
public interface DomainEvent {
    /**
     * 이벤트 발생 시각
     */
    LocalDateTime occurredOn();

    /**
     * 이벤트 타입
     */
    String eventType();
}
