package com.evri.interview;

import com.evri.interview.model.Courier;
import com.evri.interview.model.ErrorResponse;
import com.evri.interview.repository.CourierEntity;
import com.evri.interview.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTest {

    @LocalServerPort
    private int port;
    private final String baseUrl = "http://localhost:%d/api/couriers";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CourierRepository repository;

    @Test
    @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
    void shouldUpdateCourier() {
        Courier courier = Courier.builder().id(1L).active(false).name("Robert Sponge").build();
        RequestEntity<Courier> request = new RequestEntity(courier, PUT, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<Courier> response = restTemplate.exchange(request, Courier.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getName()).isEqualTo("Robert Sponge");
        assertThat(response.getBody().getActive()).isFalse();

        CourierEntity courierEntity = repository.findById(1L).get();
        assertThat(courierEntity.getFirstName()).isEqualTo("Robert");
        assertThat(courierEntity.getLastName()).isEqualTo("Sponge");
        assertThat(courierEntity.isActive()).isFalse();
    }

    @Test
    void shouldReturnValidationException() {
        Courier courier = Courier.builder().name("u o").build();
        RequestEntity<Courier> request = new RequestEntity(courier, PUT, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("{\"name\":\"size must be between 5 and 129\"," +
                        "\"active\":\"must not be null\"," +
                        "\"id\":\"must not be null\"}");
    }

    @Test
    void shouldReturnInvalidNameValidationException() {
        Courier courier = Courier.builder().id(1L).name("invalid").active(true).build();
        RequestEntity<Courier> request = new RequestEntity(courier, PUT, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(request, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo(
                "name must contain first and last name divided by space");
    }

    @Test
    void shouldReturnTooLongNameValidationException() {
        Courier courier = Courier.builder().id(1L).name("looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong name").active(true).build();
        RequestEntity<Courier> request = new RequestEntity(courier, PUT, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(request, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo(
                "first name size must be shorter than 65");
    }

    @Test
    void shouldReturnInvalidIdValidationException() {
        Courier courier = Courier.builder().id(100L).name("valid name").active(true).build();
        RequestEntity<Courier> request = new RequestEntity(courier, PUT, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(request, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo(
                "ids must be the same");
    }

    @Test
    void shouldReturnCourier() {
        RequestEntity request = new RequestEntity(GET, URI.create(String.format(baseUrl, port) + "/1"));
        ResponseEntity<Courier> response = restTemplate.exchange(request, Courier.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getName()).isEqualTo("Bob Sponge");
        assertThat(response.getBody().getActive()).isTrue();
    }

    @Test
    void shouldReturnException() {
        RequestEntity request = new RequestEntity(GET, URI.create(String.format(baseUrl, port) + "/4"));
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(request, ErrorResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("Courier not found");
    }

    @Test
    void shouldReturnAllCouriers() {
        RequestEntity request = new RequestEntity(GET, URI.create(String.format(baseUrl, port)));
        ResponseEntity<Courier[]> response = restTemplate.exchange(request, Courier[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().length).isEqualTo(2);
        assertThat(response.getBody()[0].getId()).isEqualTo(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Bob Sponge");
        assertThat(response.getBody()[0].getActive()).isTrue();
        assertThat(response.getBody()[1].getId()).isEqualTo(2);
        assertThat(response.getBody()[1].getName()).isEqualTo("Patrick Star");
        assertThat(response.getBody()[1].getActive()).isFalse();
    }

    @Test
    void shouldReturnActiveCouriers() {
        RequestEntity request = new RequestEntity(GET, URI.create(String.format(baseUrl, port) + "?isActive=true"));
        ResponseEntity<Courier[]> response = restTemplate.exchange(request, Courier[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getId()).isEqualTo(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Bob Sponge");
        assertThat(response.getBody()[0].getActive()).isTrue();
    }

}
