package com.example.inQool.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inQool.model.Reservation;
import com.example.inQool.model.User;
import com.example.inQool.model.Role;
import com.example.inQool.repository.ReservationRepository;
import com.example.inQool.repository.UserRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    public void createReservation(Reservation reservation) throws Exception {
        String phoneNumber = reservation.getUser().getPhoneNumber();
        //Pri zakladaní rezervácie sa zároveň vytvorí nový užívateľ, pokiaľ v systéme zatiaľ neexistuje užívateľ s daným telefónnym číslom.
        //This doesn't make sense to me since we can "register" and login as users this would create issue that i can make reservations on others people behalf and they dont know they have to show up.
        //but to meet the requirements i still implemented this functionality 
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setFirstName(reservation.getUser().getFirstName());
            user.setSecondName(reservation.getUser().getSecondName());
            user.setRole(Role.USER);  
            userRepository.save(user);
        }
        reservation.setUser(user);
        
        validateReservation(reservation);
        reservation.calculateTotalPrice();
        reservationRepository.save(reservation);
    }

    private void validateReservation(Reservation reservation) throws Exception {
        List<Reservation> existingReservations = reservationRepository.findByCourtId(reservation.getCourt().getId());
        for (Reservation existingReservation : existingReservations) {
            if (isTimeOverlap(reservation.getStartTime(), reservation.getEndTime(), existingReservation.getStartTime(), existingReservation.getEndTime())) {
                throw new Exception("Reservation time overlaps with an existing reservation.");
            }
        }
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByCourtId(Long courtId) {
        return reservationRepository.findByCourtId(courtId);
    }

    public List<Reservation> getReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Reservation> getFutureReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findFutureReservationsByPhoneNumber(phoneNumber);
    }

    public void softDeleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}