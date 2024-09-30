package com.example.inQool.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surface_type_id")
    private SurfaceType surfaceType;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}