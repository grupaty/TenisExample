package com.example.inQool;

import com.example.inQool.model.Court;
import com.example.inQool.model.Reservation;
import com.example.inQool.model.SurfaceType;
import com.example.inQool.model.User;
import com.example.inQool.repository.ReservationRepository;
import com.example.inQool.repository.UserRepository;
import com.example.inQool.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation_NewUser() throws Exception {
        Reservation reservation = new Reservation();
        User newUser = new User();
        newUser.setPhoneNumber("1234567890");

        SurfaceType surfaceType = new SurfaceType(null, "clay", 2);
        Court court = new Court();
        court.setSurfaceType(surfaceType);
        reservation.setCourt(court);

        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        reservation.setUser(newUser);

        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(null);

        reservationService.createReservation(reservation);

        verify(userRepository, times(1)).save(any(User.class));  // Verify that a new user was saved
        verify(reservationRepository, times(1)).save(reservation);  // Verify that the reservation was saved
    }

    @Test
    public void testCreateReservation_ExistingUser() throws Exception {
        User existingUser = new User();
        existingUser.setPhoneNumber("1234567890");

        Reservation reservation = new Reservation();
        reservation.setUser(existingUser);

        SurfaceType surfaceType = new SurfaceType(null, "clay", 2);
        Court court = new Court();
        court.setSurfaceType(surfaceType);
        reservation.setCourt(court);

        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(existingUser);

        reservationService.createReservation(reservation);

        verify(userRepository, never()).save(any(User.class));
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testFindById() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(1L)).thenReturn(reservation);

        Reservation result = reservationService.findById(1L);

        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetReservationsByCourtId() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);

        when(reservationRepository.findByCourtId(1L)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsByCourtId(1L);

        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findByCourtId(1L);
    }

    @Test
    public void testSoftDeleteReservation() {
        Reservation reservation = new Reservation();

        reservationService.softDeleteReservation(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
    }
}
