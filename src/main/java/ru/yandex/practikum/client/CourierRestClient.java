package ru.yandex.practikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;

import static io.restassured.RestAssured.given;

public class CourierRestClient extends RestClient {
    private static final String COURIER_CREATE = "courier";
    private static final String COURIER_LOGIN = "courier/login";
    private static final String COURIER_DELETE = "courier/{id}";


    /**
     * create courier
     */
    @Step
    public ValidatableResponse createCourier(CourierRequest courierRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(courierRequest)
                .post(COURIER_CREATE)
                .then();
    }

    /**
     * login courier
     */
    @Step
    public ValidatableResponse loginCourier(LoginRequest loginRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post(COURIER_LOGIN)
                .then();
    }

    /**
     * delete courier
     */
    @Step
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(getDefaultRequestSpec())
                .delete(COURIER_DELETE, id)
                .then();
    }
}
