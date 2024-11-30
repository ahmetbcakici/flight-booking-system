package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.FlightCreateRequest;
import com.iyzico.challenge.model.FlightResponseDTO;
import com.iyzico.challenge.model.FlightUpdateRequest;
import com.iyzico.challenge.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FlightControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();
    }

    @Test
    void getFlightWithSeats_ShouldReturnFlight() throws Exception {
        Long flightId = 1L;
        FlightResponseDTO flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setId(flightId);
        flightResponseDTO.setName("Test Flight");

        when(flightService.getFlightWithSeats(flightId)).thenReturn(flightResponseDTO);

        mockMvc.perform(get("/flights/{flightId}", flightId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(flightId))
                .andExpect(jsonPath("$.name").value("Test Flight"));

        verify(flightService, times(1)).getFlightWithSeats(flightId);
    }

    @Test
    void createFlight_ShouldCallService() throws Exception {
        String requestBody = "{\"name\":\"New Flight\", \"description\":\"New Flight Description\", \"price\":199.99}";

        mockMvc.perform(post("/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(flightService, times(1)).createFlight(any(FlightCreateRequest.class));
    }

    @Test
    void updateFlight_ShouldCallService() throws Exception {
        Long flightId = 1L;

        String requestBody = "{\"name\":\"Updated Flight\", \"description\":\"Updated Description\", \"price\":199.99}";

        mockMvc.perform(put("/flights/{flightId}", flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(flightService, times(1)).updateFlightById(eq(flightId), any(FlightUpdateRequest.class));
    }

    @Test
    void removeFlightById_ShouldCallService() throws Exception {
        Long flightId = 1L;

        mockMvc.perform(delete("/flights/{flightId}", flightId))
                .andExpect(status().isOk());

        verify(flightService, times(1)).removeFlightById(flightId);
    }
}