package com.evri.interview.controller;

import com.evri.interview.model.Courier;
import com.evri.interview.service.CourierService;
import com.evri.interview.validation.CourierValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
@Slf4j
public class CourierController {

    private CourierService courierService;
    private CourierValidator courierValidator;

    @GetMapping
    public ResponseEntity<List<Courier>> getAllCouriers(@RequestParam(required = false) Boolean isActive) {

        log.info("Get all couriers called");

        return ResponseEntity.ok(courierService.getAllCouriers(isActive));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courier> getCourierById(@PathVariable("id") long id) {

        log.info("Get courier by ID {} called", id);

        return ResponseEntity.ok(courierService.getCourierById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Courier> updateCourierById(@PathVariable("id") long id,
                                                     @Valid @RequestBody Courier courier) {
        log.info("Update courier by ID {} called", id);

        courierValidator.validate(id, courier);

        return ResponseEntity.ok(courierService.updateCourierById(id, courier));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error(errors.toString(), ex);
        return errors;
    }

}
