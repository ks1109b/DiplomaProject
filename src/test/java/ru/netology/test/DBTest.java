package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DBUtils;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.BuyInCreditPage;
import ru.netology.page.MainPage;

import java.sql.SQLException;

import static ru.netology.data.DBUtils.*;

public class DBTest {

    MainPage mainPage;
    BuyByCardPage buyByCardPage;
    BuyInCreditPage buyInCreditPage;
    DataHelper.CardInfo validCard = DataHelper.getApprovedCard();
    DataHelper.CardInfo invalidCard = DataHelper.getDeclinedCard();
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
        clearDBTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldPayWithApprovedCard() throws SQLException {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
        checkRowPaymentNotNull();
        compareExpectedAmountWithActual(4500000);
        checkLastPaymentStatus(approved);
        compareIDsPaymentAndOrder();
    }

    @Test
    void shouldPayWithDeclinedCard() throws SQLException {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        buyByCardPage.sendData(invalidCard);
//        buyByCardPage.checkError();
        checkRowPaymentIsNull();
//        checkRowPaymentNotNull();
        compareExpectedAmountWithActual(4500000);
        checkLastPaymentStatus(declined);
        compareIDsPaymentAndOrder();
    }

    @Test
    void shouldGetCreditWithApprovedCard() throws SQLException {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(validCard);
        buyInCreditPage.checkSuccess();
        checkRowCreditNotNull();
        checkLastCreditStatus(approved);
        compareIDsCreditAndOrder();
    }

    @Test
    void shouldGetCreditWithDeclinedCard() throws SQLException {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(invalidCard);
//        buyInCreditPage.checkError();
        checkRowCreditIsNull();
//        checkRowCreditNotNull();
        checkLastCreditStatus(declined);
        compareIDsCreditAndOrder();
    }
}
