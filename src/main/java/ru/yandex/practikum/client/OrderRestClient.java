package ru.yandex.practikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practikum.dto.CancelOrderRequest;
import ru.yandex.practikum.dto.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderRestClient extends RestClient{
    private static final String ORDER_CREATE = "orders";
    private static final String GET_LIST_ORDER = "orders";
    private static final String ORDER_CANCEL = "orders/cancel";

    /**
     * create order
     */
    @Step
    public ValidatableResponse createOrder(OrderRequest orderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequest)
                .post(ORDER_CREATE)
                .then();
    }

      /**
      * get order list
      */
    @Step
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(GET_LIST_ORDER)
                .then();
    }

    /**
     * cancel order
     */
    @Step
    public ValidatableResponse cancelOrder(CancelOrderRequest request) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(request)
                .put(ORDER_CANCEL)
                .then();
    }
}
