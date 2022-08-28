package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import io.github.bonigarcia.wdm.WebDriverManager;
import static com.codeborne.selenide.Selenide.*;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

public class TestMode {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll () {
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        var login = registeredUser.getLogin();
        var password =registeredUser.getPassword();
        $("[data-test-id='login'] .input__box .input__control").setValue(login);
        $("[data-test-id='password'] .input__box .input__control").setValue(password);
        $("[data-test-id='action-login'] .button__text").click();
        $(".heading")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        var login = notRegisteredUser.getLogin();
        var password =notRegisteredUser.getPassword();
        $("[data-test-id='login'] .input__box .input__control").setValue(login);
        $("[data-test-id='password'] .input__box .input__control").setValue(password);
        $("[data-test-id='action-login'] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        var login = blockedUser.getLogin();
        var password =blockedUser.getPassword();
        $("[data-test-id='login'] .input__box .input__control").setValue(login);
        $("[data-test-id='password'] .input__box .input__control").setValue(password);
        $("[data-test-id='action-login'] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        var password =registeredUser.getPassword();
        $("[data-test-id='login'] .input__box .input__control").setValue(wrongLogin);
        $("[data-test-id='password'] .input__box .input__control").setValue(password);
        $("[data-test-id='action-login'] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        var login = registeredUser.getLogin();

        $("[data-test-id='login'] .input__box .input__control").setValue(login);
        $("[data-test-id='password'] .input__box .input__control").setValue(wrongPassword);
        $("[data-test-id='action-login'] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(Condition.exactText("Ошибка! " + "Неверно указан логин или пароль"));

    }
}
