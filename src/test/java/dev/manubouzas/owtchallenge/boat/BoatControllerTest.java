package dev.manubouzas.owtchallenge.boat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoatControllerTest {

    @Mock
    private BoatRepository boatRepository;

    @InjectMocks
    private BoatController boatController;

    private Boat testBoat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testBoat = new Boat();
        testBoat.setId(1L);
        testBoat.setName("Sea Spirit");
        testBoat.setType(BoatType.FERRY);
    }

    @Test
    void getAllBoats_ShouldReturnAllBoats() {
        // Arrange
        List<Boat> expectedBoats = Arrays.asList(testBoat);
        when(boatRepository.findAll()).thenReturn(expectedBoats);

        // Act
        List<Boat> result = boatController.getAllBoats();

        // Assert
        assertEquals(expectedBoats, result);
        verify(boatRepository, times(1)).findAll();
    }

    @Test
    void getBoatById_WhenBoatExists_ShouldReturnBoat() {
        // Arrange
        when(boatRepository.findById(1L)).thenReturn(Optional.of(testBoat));

        // Act
        ResponseEntity<Boat> response = boatController.getBoatById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBoat, response.getBody());
        verify(boatRepository, times(1)).findById(1L);
    }

    @Test
    void getBoatById_WhenBoatNotExists_ShouldReturnNotFound() {
        // Arrange
        when(boatRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Boat> response = boatController.getBoatById(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(boatRepository, times(1)).findById(99L);
    }

    @Test
    void createBoat_ShouldSaveAndReturnCreatedBoat() {
        // Arrange
        when(boatRepository.save(testBoat)).thenReturn(testBoat);

        // Act
        ResponseEntity<Boat> response = boatController.createBoat(testBoat);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testBoat, response.getBody());
        verify(boatRepository, times(1)).save(testBoat);
    }

    @Test
    void updateBoat_WhenBoatExists_ShouldUpdateAndReturnBoat() {
        // Arrange
        Boat updatedBoat = new Boat();
        updatedBoat.setName("Updated Name");
        updatedBoat.setType(BoatType.YACHT);

        when(boatRepository.findById(1L)).thenReturn(Optional.of(testBoat));
        when(boatRepository.save(testBoat)).thenReturn(testBoat);

        // Act
        ResponseEntity<Boat> response = boatController.updateBoat(1L, updatedBoat);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals(BoatType.YACHT, response.getBody().getType());
        verify(boatRepository, times(1)).findById(1L);
        verify(boatRepository, times(1)).save(testBoat);
    }

    @Test
    void updateBoat_WhenBoatNotExists_ShouldReturnNotFound() {
        // Arrange
        when(boatRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Boat> response = boatController.updateBoat(99L, testBoat);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(boatRepository, times(1)).findById(99L);
        verify(boatRepository, never()).save(any());
    }

    @Test
    void deleteBoat_WhenBoatExists_ShouldDeleteAndReturnNoContent() {
        // Arrange
        when(boatRepository.existsById(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = boatController.deleteBoat(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(boatRepository, times(1)).existsById(1L);
        verify(boatRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBoat_WhenBoatNotExists_ShouldReturnNotFound() {
        // Arrange
        when(boatRepository.existsById(99L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = boatController.deleteBoat(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(boatRepository, times(1)).existsById(99L);
        verify(boatRepository, never()).deleteById(any());
    }
}