package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyInCreditPage {

    private final SelenideElement cardNumber = $(byText("Номер карты")).parent().$(".input__control");
    private final SelenideElement cardMonth = $(byText("Месяц")).parent().$(".input__control");
    private final SelenideElement cardYear = $(byText("Год")).parent().$(".input__control");
    private final SelenideElement cardOwner = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cardCvcCvv = $(byText("CVC/CVV")).parent().$(".input__control");
    private final SelenideElement continueButton = $$(".button__content").find(exactText("Продолжить"));
    private final SelenideElement success = $(withText("Успешно"));
    private final SelenideElement error = $(withText("Ошибка"));

    public BuyInCreditPage() {
        SelenideElement heading = $("h3").shouldHave(text("Кредит по данным карты"));
        heading.shouldBe(visible);
    }

    public BuyByCardPage sendData(String number, String month, String year, String owner, String cvcCvv) {
        cardNumber.setValue(number);
        cardMonth.setValue(month);
        cardYear.setValue(year);
        cardOwner.setValue(owner);
        cardCvcCvv.setValue(cvcCvv);
        continueButton.click();
        return new BuyByCardPage();
    }

    public BuyByCardPage sendData(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(cardInfo.getOwner());
        cardCvcCvv.setValue(cardInfo.getCvcCvv());
        continueButton.click();
        return new BuyByCardPage();
    }

    public void checkMessageSuccess() {
        success.waitUntil(visible, 15000);
        error.shouldBe(hidden);
    }

    public void checkMessageError() {
        error.waitUntil(visible, 15000);
        success.shouldBe(hidden);
    }
}
