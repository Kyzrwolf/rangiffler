package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    public SelenideElement worldMap = $(".worldmap__figure-container");

    public MainPage checkMainPageIsOpen() {
        worldMap.shouldBe(visible);
        return this;
    }
}
