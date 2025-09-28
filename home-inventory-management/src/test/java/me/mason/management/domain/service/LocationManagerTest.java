package me.mason.management.domain.service;

import me.mason.management.application.ports.out.LocationOutput;
import me.mason.management.domain.model.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationManagerTest {

    @Mock
    private LocationOutput locationOutput;

    @InjectMocks
    private LocationManager sut;

    @Test
    @DisplayName("이름이 이미 존재하면 DuplicateLocationNameException 발생")
    void registerLocation_shouldThrow_whenNameExists() {
        // given
        String name = "주방";
        when(locationOutput.existByLocationName(name)).thenReturn(true);

        // when and then
        assertThrows(DuplicateLocationNameException.class, () -> sut.registerLocation(name));
        verify(locationOutput, times(1)).existByLocationName(name);
        verify(locationOutput, never()).registerLocation(anyString());
    }

    @Test
    @DisplayName("정상 등록 시 Location 반환")
    void registerLocation_shouldReturnLocation_whenNotExists() {
        // given
        String name = "베란다";
        when(locationOutput.existByLocationName(name)).thenReturn(false);
        Location expected = Location.of(1L, name);
        when(locationOutput.registerLocation(name)).thenReturn(expected);

        // when
        Location result = sut.registerLocation(name);

        // then
        assertNotNull(result);
        assertEquals(expected.getLocationId().value(), result.getLocationId().value());
        assertEquals(expected.getName(), result.getName());
        verify(locationOutput, times(1)).existByLocationName(name);
        verify(locationOutput, times(1)).registerLocation(name);
    }

    @Test
    @DisplayName("저장 중 UNIQUE 위반(DataIntegrityViolationException) 시 DuplicateLocationNameException 변환")
    void registerLocation_shouldTranslate_whenDataIntegrityViolation() {
        // given
        String name = "냉장고";
        when(locationOutput.existByLocationName(name)).thenReturn(false);
        when(locationOutput.registerLocation(name)).thenThrow(new DataIntegrityViolationException("unique"));

        // when & then
        assertThrows(DuplicateLocationNameException.class, () -> sut.registerLocation(name));
        verify(locationOutput, times(1)).existByLocationName(name);
        verify(locationOutput, times(1)).registerLocation(name);
    }

    @Test
    @DisplayName("저장 중 PersistenceException 시 DuplicateLocationNameException 변환")
    void registerLocation_shouldTranslate_whenPersistenceException() {
        // given
        String name = "창고";
        when(locationOutput.existByLocationName(name)).thenReturn(false);
        when(locationOutput.registerLocation(name)).thenThrow(new javax.persistence.PersistenceException("unique"));

        // when & then
        assertThrows(DuplicateLocationNameException.class, () -> sut.registerLocation(name));
        verify(locationOutput, times(1)).existByLocationName(name);
        verify(locationOutput, times(1)).registerLocation(name);
    }
}