package com.example.inQool;

import com.example.inQool.controller.ReservationController;
import com.example.inQool.model.Reservation;
import com.example.inQool.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation_Success() throws Exception {
        Reservation reservation = new Reservation();

        ResponseEntity<?> response = reservationController.createReservation(reservation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
        verify(reservationService, times(1)).createReservation(reservation);
    }

    @Test
    public void testCreateReservation_Failure() throws Exception {
        Reservation reservation = new Reservation();

        doThrow(new Exception("Time overlap")).when(reservationService).createReservation(reservation);

        ResponseEntity<?> response = reservationController.createReservation(reservation);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Time overlap", response.getBody());
        verify(reservationService, times(1)).createReservation(reservation);
    }

    @Test
    public void testGetReservationById_Found() {
        Reservation reservation = new Reservation();
        when(reservationService.findById(1L)).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
        verify(reservationService, times(1)).findById(1L);
    }

    @Test
    public void testGetReservationById_NotFound() {
        when(reservationService.findById(1L)).thenReturn(null);

        ResponseEntity<Reservation> response = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).findById(1L);
    }

    @Test
    public void testGetReservationsByCourtId() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationService.getReservationsByCourtId(1L)).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByCourtId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(reservationService, times(1)).getReservationsByCourtId(1L);
    }

    @Test
    public void testDeleteReservation_Success() {
        Reservation reservation = new Reservation();
        when(reservationService.findById(1L)).thenReturn(reservation);

        ResponseEntity<Void> response = reservationController.deleteReservation(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, times(1)).softDeleteReservation(reservation);
    }

    @Test
    public void testDeleteReservation_NotFound() {
        when(reservationService.findById(1L)).thenReturn(null);

        ResponseEntity<Void> response = reservationController.deleteReservation(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, never()).softDeleteReservation(any(Reservation.class));
    }
}