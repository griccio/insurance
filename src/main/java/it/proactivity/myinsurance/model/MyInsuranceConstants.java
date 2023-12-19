package it.proactivity.myinsurance.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyInsuranceConstants {
    public static final GregorianCalendar STARTING_NEW_TARIFF_DATE = new GregorianCalendar(2020, Calendar.JANUARY,01);
    public static final int NEW_TARIFF = 1000;
    public static final int OLD_TARIFF = 750;

    public static final int WORTH_CAR_LIMIT = 10000;

    public static final int EXTRA_COST_BEFORE_2020 = 35;

    public static final int EXTRA_COST_AFTER_2020 = 45;
    public static final String KASKO_CODE ="K";

    public static final BigDecimal KASKO_DISCOUNT = new BigDecimal(0.35);



}
