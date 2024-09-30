package com.example.inQool;

import com.example.inQool.controller.CourtController;
import com.example.inQool.model.Court;
import com.example.inQool.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CourtControllerTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCourt() {
        Court court = new Court(1L, "Court 1", null, false);

        ResponseEntity<Court> response = courtController.createCourt(court);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(court, response.getBody());
        verify(courtService, times(1)).createCourt(court);
    }

    @Test
    public void testGetAllCourts() {
        Court court1 = new Court(1L, "Court 1", null, false);
        Court court2 = new Court(2L, "Court 2", null, false);

        List<Court> courtList = Arrays.asList(court1, court2);
        when(courtService.getAllCourts()).thenReturn(courtList);

        ResponseEntity<List<Court>> response = courtController.getAllCourts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(courtService, times(1)).getAllCourts();
    }

    @Test
    public void testGetCourtById_Found() {
        Court court = new Court(1L, "Court 1", null, false);
        when(courtService.findById(1L)).thenReturn(court);

        ResponseEntity<Court> response = courtController.getCourtById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(court, response.getBody());
        verify(courtService, times(1)).findById(1L);
    }

    @Test
    public void testGetCourtById_NotFound() {
        when(courtService.findById(1L)).thenReturn(null);

        ResponseEntity<Court> response = courtController.getCourtById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(courtService, times(1)).findById(1L);
    }

    @Test
    public void testUpdateCourt() {
        Court existingCourt = new Court(1L, "Court 1", null, false);
        Court updatedCourt = new Court(1L, "Updated Court", null, false);

        when(courtService.findById(1L)).thenReturn(existingCourt);

        ResponseEntity<Court> response = courtController.updateCourt(1L, updatedCourt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourt, response.getBody());
        verify(courtService, times(1)).findById(1L);
        verify(courtService, times(1)).createCourt(updatedCourt);
    }

    @Test
    public void testDeleteCourt_Success() {
        Court court = new Court(1L, "Court 1", null, false);
        when(courtService.findById(1L)).thenReturn(court);

        ResponseEntity<Void> response = courtController.deleteCourt(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courtService, times(1)).findById(1L);
        verify(courtService, times(1)).softDeleteCourt(court);
    }

    @Test
    public void testDeleteCourt_NotFound() {
        when(courtService.findById(1L)).thenReturn(null);

        ResponseEntity<Void> response = courtController.deleteCourt(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(courtService, times(1)).findById(1L);
        verify(courtService, never()).softDeleteCourt(any(Court.class));
    }
}
