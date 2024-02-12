package com.evri.interview.service;

import com.evri.interview.exception.CourierNotFoundException;
import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourierService {

    public static final String COURIER_DOES_NOT_EXIST_MESSAGE = "Courier does not exist in db. ID: %d";

    private CourierTransformer courierTransformer;
    private CourierRepository repository;

    public List<Courier> getAllCouriers(Boolean active) {
        List<CourierEntity> courierEntities;
        if (null != active && active) {
            courierEntities = repository.findAllByActive(active);
        } else {
            courierEntities = repository.findAll();
        }
        return courierEntities.stream()
                .map(courierTransformer::toCourier)
                .collect(Collectors.toList());
    }

    public Courier getCourierById(long id) {
        return courierTransformer.toCourier(getCourierEntityById(id));
    }

    public Courier updateCourierById(long id, Courier courier) {

        if (!repository.existsById(id)) {
            throw new CourierNotFoundException(String.format(COURIER_DOES_NOT_EXIST_MESSAGE, id));
        }
        CourierEntity courierEntity = courierTransformer.toCourierEntity(courier);
        courierEntity = repository.save(courierEntity);
        return courierTransformer.toCourier(courierEntity);
    }

    private CourierEntity getCourierEntityById(long id) {
        return repository.findById(id).orElseThrow(
                () -> new CourierNotFoundException(String.format(COURIER_DOES_NOT_EXIST_MESSAGE, id)));
    }

}
