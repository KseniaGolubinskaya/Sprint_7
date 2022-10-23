package ru.yandex.practikum.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practikum.dto.CourierRequest;

public class CourierRequestGenerator {
    public static CourierRequest getRandomCourierRequest() {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setLogin(RandomStringUtils.randomAlphabetic(10));
        courierRequest.setPassword(RandomStringUtils.randomAlphanumeric(10));
        courierRequest.setFirstName(RandomStringUtils.randomAlphabetic(10));
        return courierRequest;
    }

    public static CourierRequest getCourierWithoutLoginRequest() {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setPassword(RandomStringUtils.randomAlphanumeric(10));
        courierRequest.setFirstName(RandomStringUtils.randomAlphabetic(10));
        return courierRequest;
    }

    public static CourierRequest getRandomCourierByLoginRequest(String login) {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setLogin(login);
        courierRequest.setPassword(RandomStringUtils.randomAlphanumeric(10));
        courierRequest.setFirstName(RandomStringUtils.randomAlphabetic(10));
        return courierRequest;
    }
}
