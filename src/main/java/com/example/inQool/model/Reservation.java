package com.example.inQool.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isDoubles; // false for singles, true for doubles

    private double totalPrice;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;


    public void calculateTotalPrice() {
        double basePrice = court.getSurfaceType().getPricePerMinute();
        long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
        this.totalPrice = basePrice * minutes;

        if (isDoubles) {
            this.totalPrice *= 1.5;
        }
    }
}