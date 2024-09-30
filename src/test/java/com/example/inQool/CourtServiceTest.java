package com.example.inQool;

import com.example.inQool.model.Court;
import com.example.inQool.repository.CourtRepository;
import com.example.inQool.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourtServiceTest {

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private CourtService courtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCourt() {
        Court court = new Court(1L, "Court 1", null, false);


        courtService.createCourt(court);

        verify(courtRepository, times(1)).save(court);
    }

    @Test
    public void testFindById() {
        Court court = new Court(1L, "Court 1", null, false);
        when(courtRepository.findById(1L)).thenReturn(court);

        Court result = courtService.findById(1L);

        assertEquals(court, result);
        verify(courtRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllCourts() {
        Court court1 = new Court(1L, "Court 1", null, false);
        Court court2 = new Court(2L, "Court 2", null, false);
        List<Court> courtList = Arrays.asList(court1, court2);
        when(courtRepository.findAll()).thenReturn(courtList);

        List<Court> result = courtService.getAllCourts();

        assertEquals(2, result.size());
        verify(courtRepository, times(1)).findAll();
    }

    @Test
    public void testSoftDeleteCourt() {
        Court court = new Court(1L, "Court 1", null, false);

        courtService.softDeleteCourt(court);

        verify(courtRepository, times(1)).delete(court);
    }
}