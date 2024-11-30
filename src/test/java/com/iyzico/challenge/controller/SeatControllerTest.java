package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.model.SeatCreateRequest;
import com.iyzico.challenge.model.SeatUpdateRequest;
import com.iyzico.challenge.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper objectMapper;

    private SeatCreateRequest seatCreateRequest;
    private SeatUpdateRequest seatUpdateRequest;

    @BeforeEach
    void setUp() {
        seatCreateRequest = new SeatCreateRequest();
        seatCreateRequest.setSeatNumber("A1");
        seatCreateRequest.setBooked(false);

        seatUpdateRequest = new SeatUpdateRequest();
        seatUpdateRequest.setSeatNumber("B2");
        seatUpdateRequest.setBooked(true);
    }

    @Test
    void addSeatToFlight_ShouldReturnStatusOk() throws Exception {
        mockMvc.perform(post("/flights/{flightId}/seats", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatCreateRequest)))
                .andExpect(status().isOk());

        verify(seatService, times(1)).addSeatToFlight(eq(1L), any(SeatCreateRequest.class));
    }

    @Test
    void updateSeatById_ShouldReturnStatusOk() throws Exception {
        mockMvc.perform(put("/flights/{flightId}/seats/{seatId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatUpdateRequest)))
                .andExpect(status().isOk());

        verify(seatService, times(1)).updateSeatById(eq(1L), eq(2L), any(SeatUpdateRequest.class));
    }

    @Test
    void removeSeatById_ShouldReturnStatusOk() throws Exception {
        mockMvc.perform(delete("/flights/{flightId}/seats/{seatId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(seatService, times(1)).removeSeatById(1L, 2L);
    }
}