package it.proactivity.myinsurance.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyInsuranceConstants {
    public static final GregorianCalendar STARTING_NEW_TARIFF_DATE = new GregorianCalendar(2020, Calendar.JANUARY,01);
    public static final int NEW_TARIFF = 1000;
    public static final int OLD_TARIFF = 750;

    public static final int WORTH_CAR_LIMIT = 10000;

    public static final int EXTRACOST_BEFORE_2020 = 35;

    public static final int EXTRACOST_AFTER_2020 = 45;
}
