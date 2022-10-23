package ru.yandex.practikum.generator;

import ru.yandex.practikum.dto.LoginRequest;
import ru.yandex.practikum.dto.CourierRequest;

public class LoginRequestGenerator {
    public static LoginRequest from(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;
    }

    public static LoginRequest byLogin(String login) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(login);
        loginRequest.setPassword("985674");
        return loginRequest;
    }
}
