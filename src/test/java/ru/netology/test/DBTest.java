package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.BuyInCreditPage;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertAll;
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
    void clearAll() {
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
        var amount = getLastPaymentRecord().getAmount();
        var status = getLastPaymentRecord().getStatus();
        var transactionId = getLastPaymentRecord().getTransaction_id();
        var paymentId = getLastOrderRecord().getPayment_id();
        assertAll(
                () -> assertEquals(tourAmount, amount),
                () -> assertEquals(APPROVED_STATUS, status),
                () -> assertEquals(transactionId, paymentId)
        );
    }

    @Test
    void shouldPayWithDeclinedCard() {
        mainPage.getBuyByCardPage();
        buyByCardPage = new BuyByCardPage();
        buyByCardPage.sendData(invalidCard);
//        buyByCardPage.checkError();
        var amount = getLastPaymentRecord().getAmount();
        var status = getLastPaymentRecord().getStatus();
        var transactionId = getLastPaymentRecord().getTransaction_id();
        var paymentId = getLastOrderRecord().getPayment_id();
        assertAll(
                () -> assertEquals(tourAmount, amount),
                () -> assertEquals(DECLINED_STATUS, status),
                () -> assertEquals(transactionId, paymentId)
        );
    }

    @Test
    void shouldGetCreditWithApprovedCard() {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(validCard);
        buyInCreditPage.checkSuccess();
        var status = getLastCreditRecord().getStatus();
        var bankId = getLastCreditRecord().getBank_id();
        var creditId = getLastOrderRecord().getCredit_id();
        assertAll(
                () -> assertEquals(APPROVED_STATUS, status),
                () -> assertEquals(bankId, creditId)
        );
    }

    @Test
    void shouldGetCreditWithDeclinedCard() {
        mainPage.getBuyInCreditPage();
        buyInCreditPage = new BuyInCreditPage();
        buyInCreditPage.sendData(invalidCard);
//        buyInCreditPage.checkError();
        var status = getLastCreditRecord().getStatus();
        var bankId = getLastCreditRecord().getBank_id();
        var creditId = getLastOrderRecord().getCredit_id();
        assertAll(
                () -> assertEquals(DECLINED_STATUS, status),
                () -> assertEquals(bankId, creditId)
        );
    }
}
