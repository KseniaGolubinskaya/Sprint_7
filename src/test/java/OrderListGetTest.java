import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.client.OrderRestClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class OrderListGetTest {
    private OrderRestClient orderRestClient;

    @Before
    public void setUp() {
        orderRestClient = new OrderRestClient();
    }

    @Test
    @DisplayName("Проверка, что в тело ответа возвращается список заказов")
    public void getOrderListSuccessTest() {
        // Act
        ValidatableResponse getOrdersListResponse = orderRestClient.getOrderList();
        // Assert
        getOrdersListResponse
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", not(emptyArray()));
    }
}
