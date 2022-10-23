import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practikum.client.CourierRestClient;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;
import ru.yandex.practikum.generator.LoginRequestGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.yandex.practikum.generator.CourierRequestGenerator.getRandomCourierByLoginRequest;
import static ru.yandex.practikum.generator.CourierRequestGenerator.getRandomCourierRequest;

public class CourierLoginTest {
    private CourierRestClient courierRestClient;
    private Integer id;

    @Before
    public void setUp() {
        courierRestClient = new CourierRestClient();
        }

    @After
    public void tearDown() {
        if (id != null) {
            courierRestClient.deleteCourier(id)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }

    @Test
    @DisplayName("Проверка, что курьер может авторизоваться; для авторизации нужно передать все обязательные поля; успешный запрос возвращает id")
    public void loginCourierSuccessTest() {
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        ValidatableResponse createCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        createCourierResponse
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        id = courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Проверка, что если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void invalidLoginCourierFailedTest() {
        LoginRequest loginRequest = LoginRequestGenerator.byLogin("invalid login");
        courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка, что если какого-то поля нет, запрос возвращает ошибку")
    public void nullLoginCourierFailedTest() {
        LoginRequest loginRequest = LoginRequestGenerator.byLogin(null);
        courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка, что система вернёт ошибку, если неправильно указать логин или пароль")
    public void loginCourierWithInvalidPasswordTest() {
        CourierRequest randomCourierRequest = getRandomCourierByLoginRequest("helks");
        ValidatableResponse createCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        createCourierResponse
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        LoginRequest loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        id = courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");

        loginRequest = LoginRequestGenerator.from(randomCourierRequest);
        loginRequest.setPassword("invalid");
        courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
