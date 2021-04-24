package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.page.BuyByCardPage;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class BuyByCardTest {

    MainPage mainPage;
    BuyByCardPage buyByCardPage;
    CardInfo validCard = getApprovedCard();
    CardInfo invalidCard = getDeclinedCard();
    String validNumber = getValidNumber();
    String validMonth = getMonth();
    String validYear = getYear(3);
    String validOwner = getOwner();
    String validCvcCvv = getCvcCvv();

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
        buyByCardPage.sendData(validCard);
        buyByCardPage.checkSuccess();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/fullFields.csv", numLinesToSkip = 1)
    void shouldPayWithApprovedCardOtherFields(String num, String month, String year, String owner, String cvcCvv) {
        buyByCardPage.sendData(num,month,year,owner,cvcCvv);
        buyByCardPage.checkSuccess();
    }

    @Test
    void shouldGetErrorWithDeclinedCard() {
        buyByCardPage.sendData(invalidCard);
        buyByCardPage.checkError();
    }

    @Test
    void shouldGetErrorIfEmptyForm() {
        buyByCardPage.sendData("", "", "", "", "");
        String[] actual = {buyByCardPage.getNumberUnderField(),
                buyByCardPage.getMonthUnderField(),
                buyByCardPage.getYearUnderField(),
                buyByCardPage.getOwnerUnderField(),
                buyByCardPage.getCvcCvcUnderField()};
        String[] expected = {"Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения"};
        assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/numberField.csv", numLinesToSkip = 1)
    void shouldGetErrorIfInvalidNumber(String num, String expected) {
        buyByCardPage.sendData(num,validMonth,validYear,validOwner,validCvcCvv);
        val actual = buyByCardPage.getNumberUnderField();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/monthField.csv", numLinesToSkip = 1)
    void shouldGetErrorIfInvalidMonth(String month, String expected) {
        buyByCardPage.sendData(validNumber,month,validYear,validOwner,validCvcCvv);
        val actual = buyByCardPage.getMonthUnderField();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/yearField.csv", numLinesToSkip = 1)
    void shouldGetErrorIfInvalidYear(String year, String expected) {
        buyByCardPage.sendData(validNumber,validMonth,year,validOwner,validCvcCvv);
        val actual = buyByCardPage.getYearUnderField();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ownerField.csv", numLinesToSkip = 1)
    void shouldGetErrorIfInvalidOwner(String owner, String expected) {
        buyByCardPage.sendData(validNumber,validMonth,validYear,owner,validCvcCvv);
        val actual = buyByCardPage.getOwnerUnderField();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/cvcCvvField.csv", numLinesToSkip = 1)
    void shouldGetErrorIfInvalidCvcCvv(String cvcCvv, String expected) {
        buyByCardPage.sendData(validNumber,validMonth,validYear,validOwner,cvcCvv);
        val actual = buyByCardPage.getCvcCvcUnderField();
        assertEquals(expected, actual);
    }
}