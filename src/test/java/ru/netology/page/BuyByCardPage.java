package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class BuyByCardPage {

    public BuyByCardPage() {
        SelenideElement heading = $("h3").shouldHave(text("Оплата по карте"));
        heading.shouldBe(visible);
    }
}
