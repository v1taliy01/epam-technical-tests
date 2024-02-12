package com.evri.interview.validation;

import com.evri.interview.model.Courier;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.service.CourierTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;

@Component
@AllArgsConstructor
public class CourierValidator  {

    private static final String IDS_NOT_MATCH_MESSAGE = "ids must be the same";
    private static final String INVALID_NAME_MESSAGE = "name must contain first and last name divided by space";
    private static final String FIRST_NAME_TOO_LONG_MESSAGE = "first name size must be shorter than 65";
    private static final String LAST_NAME_TOO_LONG_MESSAGE = "last name size must be shorter than 65";

    private CourierTransformer courierTransformer;

    public void validate(long id, Courier courier) {
        if (courier.getId() != id) {
            throw new ValidationException(IDS_NOT_MATCH_MESSAGE);
        }
        if (!courier.getName().trim().matches("[a-zA-Z']+(\\s+[a-zA-Z']+)+")) {
            throw new ValidationException(INVALID_NAME_MESSAGE);
        }
        CourierEntity courierEntity = courierTransformer.toCourierEntity(courier);
        if (courierEntity.getFirstName().length() > 64) {
            throw new ValidationException(FIRST_NAME_TOO_LONG_MESSAGE);
        }
        if (courierEntity.getLastName().length() > 64) {
            throw new ValidationException(LAST_NAME_TOO_LONG_MESSAGE);
        }
    }

}