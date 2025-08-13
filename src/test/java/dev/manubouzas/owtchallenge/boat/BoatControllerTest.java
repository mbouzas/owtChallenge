package dev.manubouzas.owtchallenge.boat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoatController.class)
@Import(BoatControllerTest.TestConfig.class)
@WithMockUser(roles = "USER")
class BoatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoatRepository boatRepository; // This is our mock

    @Autowired
    private ObjectMapper objectMapper;

    static class TestConfig {
        @Bean
        public BoatRepository boatRepository() {
            return Mockito.mock(BoatRepository.class);
        }
    }

    @BeforeEach
    void setup() {
        Mockito.reset(boatRepository); // Reset mock before each test
    }

    @Test
    void testGetAllBoats() throws Exception {
        Boat boat1 = new Boat();
        Boat boat2 = new Boat();
        when(boatRepository.findAll()).thenReturn(List.of(boat1, boat2));

        mockMvc.perform(get("/api/boats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(boatRepository).findAll();
    }

    @Test
    void testGetBoatByIdFound() throws Exception {
        Boat boat = new Boat();
        boat.setId(1L);
        when(boatRepository.findById(1L)).thenReturn(Optional.of(boat));

        mockMvc.perform(get("/api/boats/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(boatRepository).findById(1L);
    }

    @Test
    void testGetBoatByIdNotFound() throws Exception {
        when(boatRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/boats/999"))
                .andExpect(status().isNotFound());

        verify(boatRepository).findById(999L);
    }

    @Test
    void testCreateBoat() throws Exception {
        Boat boat = new Boat();
        boat.setName("Titanic");
        boat.setDescription("Large passenger ship");
        boat.setCapacity(1000);
        boat.setSize(269);
        boat.setType(BoatType.SAILBOAT);

        when(boatRepository.save(any(Boat.class))).thenReturn(boat);

        String json = objectMapper.writeValueAsString(boat);

        mockMvc.perform(post("/api/boats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())) // Add CSRF token
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(boatRepository).save(any(Boat.class));
    }

    @Test
    void testUpdateBoatFound() throws Exception {
        Boat existingBoat = new Boat();
        existingBoat.setId(1L);
        existingBoat.setName("OldName");
        existingBoat.setDescription("Old description");
        existingBoat.setCapacity(500);
        existingBoat.setSize(200);
        existingBoat.setType(BoatType.MOTORBOAT);

        Boat updatedBoat = new Boat();
        updatedBoat.setName("NewName");
        updatedBoat.setDescription("Updated description");
        updatedBoat.setCapacity(600);
        updatedBoat.setSize(220);
        updatedBoat.setType(BoatType.SAILBOAT);

        when(boatRepository.findById(1L)).thenReturn(Optional.of(existingBoat));
        when(boatRepository.save(any(Boat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String json = objectMapper.writeValueAsString(updatedBoat);

        mockMvc.perform(put("/api/boats/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(boatRepository).findById(1L);
        verify(boatRepository).save(any(Boat.class));
    }

    @Test
    void testUpdateBoatNotFound() throws Exception {
        Boat updatedBoat = new Boat();
        updatedBoat.setName("NewName");
        updatedBoat.setDescription("Updated description");
        updatedBoat.setCapacity(600);
        updatedBoat.setSize(220);
        updatedBoat.setType(BoatType.SAILBOAT);

        when(boatRepository.findById(999L)).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(updatedBoat);

        mockMvc.perform(put("/api/boats/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(boatRepository).findById(999L);
    }

    @Test
    void testDeleteBoatFound() throws Exception {
        when(boatRepository.existsById(1L)).thenReturn(true);
        doNothing().when(boatRepository).deleteById(1L);

        mockMvc.perform(delete("/api/boats/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(boatRepository).existsById(1L);
        verify(boatRepository).deleteById(1L);
    }

    @Test
    void testDeleteBoatNotFound() throws Exception {
        when(boatRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/boats/999").with(csrf()))
                .andExpect(status().isNotFound());

        verify(boatRepository).existsById(999L);
    }

}
