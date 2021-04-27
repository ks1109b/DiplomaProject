package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.BuyInCreditPage;
import ru.netology.page.MainPage;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DBUtils.*;

public class DBTest {

    MainPage mainPage;
    BuyByCardPage buyByCardPage;
    BuyInCreditPage buyInCreditPage;
    DataHelper.CardInfo validCard = DataHelper.getApprovedCard();
    DataHelper.CardInfo invalidCard = DataHelper.getDeclinedCard();
    private final String APPROVED_STATUS = "APPROVED";
    private final String DECLINED_STATUS = "DECLINED";
    private final int tourAmount = 4500000;

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
    void shouldPayWithApprovedCard() {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
        assertEquals(tourAmount, getAmountPayment());
        assertEquals(APPROVED_STATUS, getLastPaymentStatus());
        assertEquals(getTransactionIdFromPayment(), getPaymentIdFromOrder());
    }

    @Test
    void shouldPayWithDeclinedCard() {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        buyByCardPage.sendData(invalidCard);
//        buyByCardPage.checkError();
        assertEquals(tourAmount, getAmountPayment());
        assertEquals(DECLINED_STATUS, getLastPaymentStatus());
        assertEquals(getTransactionIdFromPayment(), getPaymentIdFromOrder());
    }

    @Test
    void shouldGetCreditWithApprovedCard() {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(validCard);
        buyInCreditPage.checkSuccess();
        assertEquals(APPROVED_STATUS, getLastCreditStatus());
        assertEquals(getBankIdFromCredit(), getCreditIdFromOrder());
    }

    @Test
    void shouldGetCreditWithDeclinedCard() {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(invalidCard);
//        buyInCreditPage.checkError();
        assertEquals(DECLINED_STATUS, getLastCreditStatus());
        assertEquals(getBankIdFromCredit(), getCreditIdFromOrder());
    }
}
