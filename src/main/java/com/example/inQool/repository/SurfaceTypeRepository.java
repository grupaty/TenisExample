package com.example.inQool.repository;

import com.example.inQool.model.SurfaceType;
import com.example.inQool.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public class SurfaceTypeRepository{
    @PersistenceContext
    private EntityManager entityManager;

    public void save(SurfaceType surfaceType) {
        entityManager.persist(surfaceType);
    }
    public SurfaceType findByName(String name) {
        TypedQuery<SurfaceType> query = entityManager.createQuery(
                "SELECT s FROM SurfaceType s WHERE s.name = :name", SurfaceType.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}