package com.example.inQool.repository;

import com.example.inQool.model.Reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    public Reservation findById(Long id) {
        return entityManager.find(Reservation.class, id);
    }

    public List<Reservation> findByCourtId(Long courtId) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.court.id = :courtId AND r.deleted = false ORDER BY r.startTime", Reservation.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<Reservation> findByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.user.phoneNumber = :phoneNumber AND r.deleted = false ORDER BY r.startTime", Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public List<Reservation> findFutureReservationsByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery("SELECT r FROM Reservation r WHERE r.user.phoneNumber = :phoneNumber AND r.startTime > CURRENT_TIMESTAMP AND r.deleted = false ORDER BY r.startTime", Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    public void delete(Reservation reservation) {
        reservation.setDeleted(true);
        entityManager.merge(reservation);
    }
}