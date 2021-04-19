package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DBUtils;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.MainPage;

import java.sql.SQLException;

public class DBTest {

    MainPage mainPage;
    BuyByCardPage buyByCardPage;
    private final static String approved = "APPROVED";
    private final static String declined = "DECLINED";

    @BeforeEach
    void setUp() {
        mainPage = new MainPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void clearAll() throws SQLException {
        DBUtils.clearDBTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void payWithApprovedCard() throws SQLException {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        val validCard = DataHelper.getApprovedCard();
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
        DBUtils.checkRowPaymentNotNull();
        DBUtils.compareExpectedAmountWithActual(4500000);
        DBUtils.checkLastPaymentStatus(approved);
        DBUtils.compareIDsPaymentAndOrder();
    }
}
