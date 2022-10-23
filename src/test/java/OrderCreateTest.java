import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practikum.client.OrderRestClient;
import ru.yandex.practikum.dto.CancelOrderRequest;
import ru.yandex.practikum.dto.OrderRequest;


import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private OrderRestClient orderRestClient;
    private Integer track;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone,
                           Integer rentTime, String deliveryDate, String comment, String[] color) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }


    @Before
    public void setUp() {
        orderRestClient = new OrderRestClient();
    }

    @After
    public void tearDown() {
        if (track != null) {
            // Отмена заказа не работает - нужно создать баг.
            orderRestClient.cancelOrder(new CancelOrderRequest(track))
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {"Михаил", "Светлов", "г.Москва", "Беляево", "+7 988 777 67 57", 2, "2022-10-31", "Thanks", new String[] {"BLACK"}},
                {"Анна", "Иванова", "г.Москва", "Аннино", "+7 999 534 78 87", 3, "2022-10-25", "Hello", new String[] {"BLACK", "GREY"}},
                {"Ярослав", "Чернов", "г.Москва", "Пушкинская", "+7 967 543 68 90", 1, "2022-10-28", "Bye", new String[] {}},
        };
    }

    @Test
    @DisplayName ("Проверка создания заказа")
    public void createOrderTest() {
        // Arrange
        OrderRequest createOrderRequest = new OrderRequest();
        createOrderRequest.setFirstName(this.firstName);
        createOrderRequest.setLastName(this.lastName);
        createOrderRequest.setAddress(this.address);
        createOrderRequest.setMetroStation(this.metroStation);
        createOrderRequest.setPhone(this.phone);
        createOrderRequest.setRentTime(this.rentTime);
        createOrderRequest.setDeliveryDate(this.deliveryDate);
        createOrderRequest.setComment(this.comment);
        createOrderRequest.setColor(this.color);
        // Act
        ValidatableResponse createOrderResponse = orderRestClient.createOrder(createOrderRequest);
        // Assert
        track = createOrderResponse
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}