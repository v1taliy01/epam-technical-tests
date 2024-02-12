package com.evri.interview.service;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CourierTransformer {

    public Courier toCourier(CourierEntity entity) {
        return Courier.builder()
                .id(entity.getId())
                .name(String.format("%s %s", entity.getFirstName(), entity.getLastName()))
                .active(entity.isActive())
                .build();
    }

    public CourierEntity toCourierEntity(Courier courier) {
        String[] strings = courier.getName().trim().split("\\s+");
        String firstName = Arrays.stream(strings).limit(strings.length - 1).collect(Collectors.joining(" "));
        String lastName = strings[strings.length - 1];
        return CourierEntity.builder()
                .id(courier.getId())
                .firstName(firstName)
                .lastName(lastName)
                .active(courier.getActive())
                .build();
    }

}
