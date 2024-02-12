package com.evri.interview.validation;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.service.CourierTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierValidatorTest {

    CourierValidator courierValidator;
    @Mock
    CourierTransformer transformer;

    @BeforeEach
    void setup() {
        courierValidator = new CourierValidator(transformer);
    }

    @Test
    void validate() {
        Courier courier = Courier.builder().id(1L).name("Name Lastname").active(true).build();
        CourierEntity entity = CourierEntity.builder().id(1L).firstName("Name").lastName("Lastname").active(true).build();
        when(transformer.toCourierEntity(courier)).thenReturn(entity);

        courierValidator.validate(1L, courier);

        verify(transformer).toCourierEntity(courier);
    }

    @Test
    void getIdsNotMatchWhenValidate() {
        ValidationException thrown =
                Assertions.assertThrows(ValidationException.class, () ->
                        courierValidator.validate(1L, Courier.builder().id(2L).name("Name Lastname").active(true).build()));

        Assertions.assertEquals("ids must be the same", thrown.getMessage());
    }

    @Test
    void getInvalidNameWhenValidate() {
        ValidationException thrown =
                Assertions.assertThrows(ValidationException.class, () ->
                        courierValidator.validate(1L, Courier.builder().id(1L).name("NameLastname").active(true).build()));

        Assertions.assertEquals("name must contain first and last name divided by space", thrown.getMessage());
    }

    @Test
    void getNameTooLongWhenValidate() {
        Courier courier = Courier.builder()
                .id(1L)
                .name("LooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooName Lastname")
                .build();
        CourierEntity entity = CourierEntity.builder()
                .id(1L)
                .firstName("LooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooName")
                .lastName("Lastname")
                .active(true)
                .build();
        when(transformer.toCourierEntity(courier)).thenReturn(entity);

        ValidationException thrown =
                Assertions.assertThrows(ValidationException.class, () ->
                        courierValidator.validate(1L, courier));

        Assertions.assertEquals("first name size must be shorter than 65", thrown.getMessage());
    }
}