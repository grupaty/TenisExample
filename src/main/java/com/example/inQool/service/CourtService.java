package com.example.inQool.service;

import com.example.inQool.model.Court;
import com.example.inQool.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public void createCourt(Court court) {
        courtRepository.save(court);
    }

    public Court findById(Long id) {
        return courtRepository.findById(id);
    }

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public void softDeleteCourt(Court court) {
        courtRepository.delete(court);
    }
}