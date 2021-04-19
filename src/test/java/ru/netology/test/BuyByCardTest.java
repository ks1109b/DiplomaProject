package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.MainPage;

public class BuyByCardTest {

    MainPage mainPage;
    BuyByCardPage buyByCardPage;

    @BeforeEach
    void setUp() {
        mainPage = new MainPage();
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldPayWithApprovedCard() {
        val validCard = DataHelper.getApprovedCard();
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
    }

    @Test
    void shouldGetErrorWithDeclinedCard() {
        val validCard = DataHelper.getDeclinedCard();
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
    }


}
