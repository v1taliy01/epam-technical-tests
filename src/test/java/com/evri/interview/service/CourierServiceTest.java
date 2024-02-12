package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    CourierService courierService;
    @Mock
    CourierRepository repository;
    @Mock
    CourierTransformer transformer;

    @BeforeEach
    void setup() {
        courierService = new CourierService(transformer, repository);
    }

    @Test
    void getAllActiveCouriers() {
        courierService.getAllCouriers(true);

        verify(repository).findAllByActive(true);
    }

    @Test
    void getAllCouriers() {
        courierService.getAllCouriers(false);

        verify(repository).findAll();
    }

    @Test
    void getCourierById() {
        when(repository.findById(1L)).thenReturn(Optional.of(new CourierEntity()));

        courierService.getCourierById(1L);

        verify(repository).findById(1L);
    }

    @Test
    void getExceptionWhenGetCourierById() {
        CourierNotFoundException thrown =
                Assertions.assertThrows(CourierNotFoundException.class, () -> courierService.getCourierById(1L));

        Assertions.assertEquals("Courier does not exist in db. ID: 1", thrown.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void updateCourierById() {
        when(repository.existsById(1L)).thenReturn(true);
        Courier courier = Courier.builder().build();
        CourierEntity entity = new CourierEntity();
        when(transformer.toCourierEntity(courier)).thenReturn(entity);

        courierService.updateCourierById(1L, courier);

        verify(repository).existsById(1L);
        verify(repository).save(entity);
    }

    @Test
    void getExceptionWhenUpdateCourierById() {
        CourierNotFoundException thrown =
                Assertions.assertThrows(CourierNotFoundException.class, () ->
                        courierService.updateCourierById(1L, Courier.builder().build()));

        Assertions.assertEquals("Courier does not exist in db. ID: 1", thrown.getMessage());

        verify(repository).existsById(1L);
    }
}