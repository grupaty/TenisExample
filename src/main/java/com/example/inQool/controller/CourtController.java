package com.example.inQool.controller;

import com.example.inQool.model.Court;
import com.example.inQool.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @PostMapping
    public ResponseEntity<Court> createCourt(@RequestBody Court court) {
        courtService.createCourt(court);
        return ResponseEntity.ok(court);
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts() {
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long id) {
        Court court = courtService.findById(id);
        return court != null ? ResponseEntity.ok(court) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long id, @RequestBody Court updatedCourt) {
        Court court = courtService.findById(id);
        if (court == null) {
            return ResponseEntity.notFound().build();
        }
        updatedCourt.setId(id);
        courtService.createCourt(updatedCourt);
        return ResponseEntity.ok(updatedCourt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        Court court = courtService.findById(id);
        if (court == null) {
            return ResponseEntity.notFound().build();
        }
        courtService.softDeleteCourt(court);
        return ResponseEntity.noContent().build();
    }
}