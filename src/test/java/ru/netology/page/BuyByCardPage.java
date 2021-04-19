package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class BuyByCardPage {

    private final SelenideElement numberField = $(byText("Номер карты")).parent().$(".input__control");
    private final SelenideElement monthField = $(byText("Месяц")).parent().$(".input__control");
    private final SelenideElement yearField = $(byText("Год")).parent().$(".input__control");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cvcCvcField = $(byText("CVC/CVV")).parent().$(".input__control");
    private final SelenideElement continueButton = $$(".button__content").find(exactText("Продолжить"));
    private final SelenideElement success = $(withText("Успешно"));
    private final SelenideElement error = $(withText("Ошибка"));

    public BuyByCardPage() {
        SelenideElement heading = $$("h3").find(exactText("Оплата по карте"));
        heading.shouldBe(visible);
    }

    public void sendData(String number, String month, String year, String owner, String cvcCvv) {
        numberField.setValue(number);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(owner);
        cvcCvcField.setValue(cvcCvv);
        continueButton.click();
    }

    public void sendData(DataHelper.CardInfo cardInfo) {
        numberField.setValue(cardInfo.getNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcCvcField.setValue(cardInfo.getCvcCvv());
        continueButton.click();
    }

    public void checkSuccess() {
        success.waitUntil(visible, 15000);
        error.shouldBe(hidden);
    }

    public void checkError() {
        error.waitUntil(visible, 15000);
        success.shouldBe(hidden);
    }
}
