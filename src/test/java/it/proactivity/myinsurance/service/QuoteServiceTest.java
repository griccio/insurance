package it.proactivity.myinsurance.service;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * INSERT INTO quote(
 *     id, holder_id, registration_mark, registration_date_car, worth, policy_type, cost, quote_number,date)
 * VALUES (100, 100, 'DR1234A', '2010-01-01',10000, 'RCA6', 10, '100-DR1234A-100','2023-10-01 13:16:00'),
 *        (101, 101, 'CD2222A', '2022-01-01',30000, 'RCA6', 30, '100-CD2222A-101','2023-12-22 13:16:00'),
 *        (102, 101, 'CD2222A', '2022-01-01',30000, 'RCA12', 50, '100-CD2222A-102','2023-09-16 13:16:00'),
 *        (103, 103, 'EE1111EA', '2019-01-01',30000, 'RCA6', 20, '100-CD2222A-102','2023-05-14 13:16:00'),
 *        (104, 103, 'EF2222WD', '2023-01-01',30000, 'RCA50', 30, '100-CD2222A-102','2023-07-30 13:16:00');
 */
@SpringBootTest
public class QuoteServiceTest {
    @Autowired
    QuoteService quoteService;

    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceTest.class);
    final int TOT_QUOTES_BEFORE_TEST = 5; //this is the tot of records into the quote table before each test

    @BeforeEach
    public void initTable() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");
    }

    @Test
    public void findAll() {
        List<Quote> list = quoteService.findAll();
        Assertions.assertEquals(TOT_QUOTES_BEFORE_TEST, list.size());
    }

    @Test
    public void findById() {
        Quote quote = quoteService.findById(100L);
        Assertions.assertNotNull(quote);
    }

    @Test
    public void findByIdErrorBecauseIdNotPresent() {
        Quote quote = quoteService.findById(999L);
        Assertions.assertNull(quote);
    }

    @Test
    public void findByIdErrorBecauseIdNegative() {
        Quote quote = quoteService.findById(-100L);
        Assertions.assertNull(quote);
    }

    @Test
    public void findByHolderIdGroupByCar(){
        QuoteByHolderAndCarDTO quoteByCarDTO = new QuoteByHolderAndCarDTO();
        quoteByCarDTO.setHolder_id(103L);

        List<QuoteByHolderAndCarDTO> quoteByCarDTOList = quoteService.findByHolderIdGroupByCar(quoteByCarDTO);
        Assertions.assertEquals(2, quoteByCarDTOList.size());
        quoteByCarDTOList.forEach(quote -> logger.debug(quote.toString()));
    }


    @Test
    public void save(){
        QuoteForCreateDTO quoteForCreateDTO =
                new QuoteForCreateDTO(103L,"AA1234BB",
                        new GregorianCalendar(2014, Calendar.MARCH,01).getTime(),
                        new BigDecimal(15000),
                        PolicyType.RCA6);
        try {
            Quote quote;
            quote = quoteService.save(quoteForCreateDTO);

            Assertions.assertNotNull(quote);
            Assertions.assertEquals(TOT_QUOTES_BEFORE_TEST +1, quoteService.findAll().size());
        }catch(QuoteException e){
            logger.error(e.getMessage());
            Assertions.fail();
        }
    }

    @Test
    public void calculateCostQuote01() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(10000),
                new GregorianCalendar(2020,01,01).getTime(),
                PolicyType.RCA12);
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(10000)
                .divide(new BigDecimal(Quote.NEW_TARIFF), RoundingMode.UP);

        expectedCost = expectedCost.add(expectedCost.multiply(new BigDecimal(0.12)));


        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }


    @Test
    public void calculateCostQuote02() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(7000),
                new GregorianCalendar(2021,01,01).getTime(),
                PolicyType.RCA12);
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(7000)
                .divide(new BigDecimal(Quote.OLD_TARIFF), RoundingMode.UP);

        expectedCost = expectedCost.add(expectedCost.multiply(new BigDecimal(0.12)));


        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }



    @Test
    public void calculateCostQuote03() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2010,01,01).getTime(), //registration date
                PolicyType.RCA50); //type of policy
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(70000)
                .divide(new BigDecimal(Quote.OLD_TARIFF), RoundingMode.UP);

        expectedCost = expectedCost.add(expectedCost.multiply(new BigDecimal(0.25)));

        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }

    @Test
    public void calculateCostQuote04() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2022,01,01).getTime(), //registration date
                PolicyType.RCA50); //type of policy
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(70000)
                .divide(new BigDecimal(Quote.NEW_TARIFF), RoundingMode.UP);

        expectedCost = expectedCost.add(expectedCost.multiply(new BigDecimal(0.25)));

        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }

    @Test
    public void calculateCostQuote05() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2022,01,01).getTime(), //registration date
                PolicyType.RCA6); //type of policy
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(70000)
                .divide(new BigDecimal(Quote.NEW_TARIFF), RoundingMode.UP)
                .add(new BigDecimal(7000).multiply(new BigDecimal(0)));


        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }



}
