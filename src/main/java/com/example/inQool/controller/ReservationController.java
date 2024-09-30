package com.example.inQool.controller;

import com.example.inQool.model.Reservation;
import com.example.inQool.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            reservationService.createReservation(reservation);
            return ResponseEntity.ok(reservation.getTotalPrice());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<Reservation>> getReservationsByCourtId(@PathVariable Long courtId) {
        List<Reservation> reservations = reservationService.getReservationsByCourtId(courtId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<Reservation>> getReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservations = reservationService.getReservationsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/phone/{phoneNumber}/future")
    public ResponseEntity<List<Reservation>> getFutureReservationsByPhoneNumber(@PathVariable String phoneNumber) {
        List<Reservation> reservations = reservationService.getFutureReservationsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        reservationService.softDeleteReservation(reservation);
        return ResponseEntity.noContent().build();
    }
}