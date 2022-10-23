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
import static ru.yandex.practikum.generator.CourierRequestGenerator.*;

public class CourierCreateTest {
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
    @DisplayName("Проверка, что курьера можно создать; запрос возвращает правильный код ответа; успешный запрос возвращает ok: true; чтобы создать курьера, нужно передать в ручку все обязательные поля.")
    public void createCourierSuccessTest() {
        // Arrange
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        // Act
        ValidatableResponse createCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        // Assert
        createCourierResponse
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        // login courier
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
    @DisplayName("Проверка, что нельзя создать двух одинаковых курьеров")
    public void UnableToCreateSameCourierTest() {
        // Arrange
        CourierRequest randomCourierRequest = getRandomCourierRequest();
        // Act
        ValidatableResponse createCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        ValidatableResponse createSameCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        // Assert
        createCourierResponse
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        createSameCourierResponse
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка, что если одного из полей нет, запрос возвращает ошибку")
    public void createCourierWithoutLoginFailedTest() {
        // Arrange
        CourierRequest randomCourierRequest = getCourierWithoutLoginRequest();
        // Act
        ValidatableResponse createCourierResponse = courierRestClient.createCourier(randomCourierRequest);
        // Assert
        createCourierResponse
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка, что если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierByLoginFailedTest() {
        // Arrange
        CourierRequest courierRequest1 = getRandomCourierByLoginRequest("helks");
        CourierRequest courierRequest2 = getRandomCourierByLoginRequest("helks");
        // Act
        ValidatableResponse createCourierResponse1 = courierRestClient.createCourier(courierRequest1);
        ValidatableResponse createCourierResponse2 = courierRestClient.createCourier(courierRequest2);
        // Assert
        createCourierResponse1
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        createCourierResponse2
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        // login courier
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest1);
        id = courierRestClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
    }
}
