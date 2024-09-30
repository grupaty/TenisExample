package com.example.inQool.repository;

import com.example.inQool.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(User user) {
        entityManager.persist(user);
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.deleted = false", User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult();
    }

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.deleted = false", User.class).getResultList();
    }

    public void delete(User user) {
        user.setDeleted(true);
        entityManager.merge(user);
    }
}