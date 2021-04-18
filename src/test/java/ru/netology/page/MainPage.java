package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class MainPage {

    private final String mailUrl = "http://127.0.0.1:8080";
    private final SelenideElement byCardButton = $(byText("Купить"));
    private final SelenideElement inCreditButton = $(byText("Купить в кредит"));

    public MainPage() {
        open(mailUrl);
        SelenideElement heading = $("h2").shouldHave(text("Путешествие дня"));
        heading.shouldBe(visible);
    }

    public BuyByCardPage getBuyByCardPage(){
        byCardButton.click();
        return new BuyByCardPage();
    }

    public BuyInCreditPage getBuyInCreditPage(){
        inCreditButton.click();
        return new BuyInCreditPage();
    }
}
