package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourierTransformerTest {

    final CourierTransformer transformer = new CourierTransformer();

    @Test
    void toCourier() {
        CourierEntity entity = CourierEntity.builder().id(1L).firstName("Name").lastName("Lastname").active(true).build();
        Courier courier = transformer.toCourier(entity);

        assertEquals("Name Lastname", courier.getName());
        assertEquals(1L, courier.getId());
        assertTrue(courier.getActive());
    }

    @Test
    void toCourierEntity() {
        Courier courier = Courier.builder().id(1L).name("Name Lastname").active(true).build();
        CourierEntity entity = transformer.toCourierEntity(courier);

        assertEquals("Name", entity.getFirstName());
        assertEquals("Lastname", entity.getLastName());
        assertEquals(1L, entity.getId());
        assertTrue(entity.isActive());
    }

    @Test
    void toCourierEntityWithMiddleName() {
        Courier courier = Courier.builder().id(1L).name("   Name   Middlename  Lastname   ").active(true).build();
        CourierEntity entity = transformer.toCourierEntity(courier);

        assertEquals("Name Middlename", entity.getFirstName());
        assertEquals("Lastname", entity.getLastName());
        assertEquals(1L, entity.getId());
        assertTrue(entity.isActive());
    }
}