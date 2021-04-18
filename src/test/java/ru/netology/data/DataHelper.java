package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataHelper {

    @Value
    public static class CardInfo {
        public String number;
        public String month;
        public String year;
        public String owner;
        public String cvcCvv;
    }

//    public static CardInfo getCardInfo(String number, String month, String year, String owner, String cvcCvv) {
//        return new CardInfo(number, month, year, owner, cvcCvv);
//    }

    static Faker faker = new Faker(new Locale("en"));
    static Calendar calendar = new GregorianCalendar();

    public static CardInfo getApprovedCard() {
        return new CardInfo("4444 4444 4444 4441",
                getMoth(),
                getYear(5),
                faker.name().lastName() + " " + faker.name().firstName(),
                faker.numerify("###"));
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("4444 4444 4444 4442",
                getMoth(),
                getYear(5),
                faker.name().lastName() + " " + faker.name().firstName(),
                faker.numerify("###"));
    }

    public static String getMoth() {
        return String.format("%02d", calendar.get(Calendar.MONTH));
    }

    public static String getYear(int shift) {
        return String.valueOf(calendar.get(Calendar.YEAR) + shift).substring(2);
    }
}
