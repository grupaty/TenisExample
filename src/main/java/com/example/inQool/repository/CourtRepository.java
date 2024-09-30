package com.example.inQool.repository;

import com.example.inQool.model.Court;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class CourtRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Court court) {
        entityManager.persist(court);
    }

    public Court findById(Long id) {
        return entityManager.find(Court.class, id);
    }

    public List<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.deleted = false", Court.class).getResultList();
    }

    public void delete(Court court) {
        court.setDeleted(true);
        entityManager.merge(court);
    }
}