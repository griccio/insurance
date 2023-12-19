package it.proactivity.myinsurance.service;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.*;
import it.proactivity.myinsurance.repository.OptionalExtraRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 INSERT INTO quote(
 id, holder_id, car_id,  policy_type, cost, quote_number,date)
 VALUES
 (300, 100, 200, 'RCA6', 10, '100-DR1234A-1','2023-10-01 13:16:00'),
 (301, 101, 201, 'RCA6', 30, '100-CD2222A-1','2023-12-22 13:16:00'),
 (302, 101, 202, 'RCA12', 50, '100-CD2222A-2','2023-09-16 13:16:00'),
 (303, 103, 203, 'RCA6', 20, '100-CD2222A-1','2023-05-14 13:16:00'),
 (304, 103, 204, 'RCA50', 30, '100-CD2222A-2','2023-07-30 13:16:00'); */
@SpringBootTest
public class QuoteServiceTest {
    @Autowired
    QuoteService quoteService;

    @Autowired
    OptionalExtraRepository optionalExtraRepository;

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
    public void findByHolderIdAndCar(){
        QuoteByHolderAndCarDTO quoteByCarDTO = new QuoteByHolderAndCarDTO();
        quoteByCarDTO.setHolder_id(103L);

        List<QuoteByHolderAndCarDTO> quoteByCarDTOList = quoteService.findByHolderIdAndCar(quoteByCarDTO);
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
            QuoteDTO quoteDTO;
            quoteDTO = quoteService.save(quoteForCreateDTO);

            Assertions.assertNotNull(quoteDTO);
            Assertions.assertEquals(TOT_QUOTES_BEFORE_TEST +1, quoteService.findAll().size());
        }catch(QuoteException e){
            logger.error(e.getMessage());
            Assertions.fail();
        }
    }


//
//    @Test
//    public void update(){
//        //I prepare the test data creationg  a new quote
//        QuoteForCreateDTO quoteForCreateDTO =
//                new QuoteForCreateDTO(103L,"AA1234BB",
//                        new GregorianCalendar(2014, Calendar.MARCH,01).getTime(),
//                        new BigDecimal(15000),
//                        PolicyType.RCA6);
//        try {
//            QuoteDTO quoteDTO;
//            quoteDTO = quoteService.save(quoteForCreateDTO);
//
//            //start the update test
//            //update the quote, adding the optional extras
//            QuoteForUpdateDTO quoteForUpdateDTO = new QuoteForUpdateDTO();
//            quoteForUpdateDTO.setId(quoteDTO.getId());
//            quoteForUpdateDTO.setOptionalExtraByCodeList(Arrays.asList("C","FI","TL"));
//
//            QuoteWithOptionalExtraDTO quoteWithOptionalExtraDTO = quoteService.update(quoteForUpdateDTO);
//            logger.debug(quoteWithOptionalExtraDTO.toString());
//
//            Quote quote2 = quoteService.findById(quote.getId());
//            Assertions.assertNotNull(quote2);
//            Assertions.assertEquals(3, quote2.getOptionalExtras().size());
//
//        }catch(QuoteException e){
//            logger.error(e.getMessage());
//            Assertions.fail();
//        }
//    }

//
//    @Test
//    public void updateWithKasko(){
//        //I prepare the test data creationg  a new quote
//        QuoteForCreateDTO quoteForCreateDTO =
//                new QuoteForCreateDTO(103L,"AA1234BB",
//                        new GregorianCalendar(2014, Calendar.MARCH,01).getTime(),
//                        new BigDecimal(15000),
//                        PolicyType.RCA6);
//        try {
//            Quote quote;
//            quoteService.save(quoteForCreateDTO);
//            quote = quoteService.findBy
//
//            //start the update test
//            //update the quote, adding the optional extras
//            QuoteForUpdateDTO quoteForUpdateDTO = new QuoteForUpdateDTO();
//            quoteForUpdateDTO.setId(quoteDTO.);
//            quoteForUpdateDTO.setOptionalExtraByCodeList(Arrays.asList("C","FI","TL",MyInsuranceConstants.KASKO_CODE));
//
//            QuoteWithOptionalExtraDTO quoteWithOptionalExtraDTO = quoteService.update(quoteForUpdateDTO);
//            logger.debug(quoteWithOptionalExtraDTO.toString());
//
//            Quote quote2 = quoteService.findById(quoteDTO.getId());
//            Assertions.assertNotNull(quote2);
//            Assertions.assertEquals(1, quote2.getOptionalExtras().size());
//
//        }catch(QuoteException e){
//            logger.error(e.getMessage());
//            Assertions.fail();
//        }
//    }




//
//
//    @Test
//    public void updateWithErrorBecauseOptionalExtraNotExist(){
//        QuoteForCreateDTO quoteForCreateDTO =
//                new QuoteForCreateDTO(103L,"AA1234BB",
//                        new GregorianCalendar(2014, Calendar.MARCH,01).getTime(),
//                        new BigDecimal(15000),
//                        PolicyType.RCA6);
//        try {
//            Quote quote;
//
//            quote = quoteService.save(quoteForCreateDTO);
//
//            QuoteForUpdateDTO quoteForUpdateDTO = new QuoteForUpdateDTO();
//            quoteForUpdateDTO.setId(quote.getId());
//            quoteForUpdateDTO.setOptionalExtraByCodeList(Arrays.asList("C","FI","TL","PP"));
//
//            quoteService.update(quoteForUpdateDTO);
//            Assertions.fail();
//
//            Quote quote2 = quoteService.findById(quote.getId());
//            Assertions.assertNotNull(quote2);
//            Assertions.assertEquals(3, quote2.getOptionalExtras().size());
//
//        }catch(QuoteException e){
//            logger.error(e.getMessage());
//            Assertions.assertTrue(true);
//        }
//    }

    @Test
    public void calculateCostQuote01() {
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(10000),
                new GregorianCalendar(2020,01,01).getTime(),
                PolicyType.RCA12);
        logger.debug(cost.toString());

        BigDecimal expectedCost = new BigDecimal(10000)
                .divide(new BigDecimal(MyInsuranceConstants.NEW_TARIFF), RoundingMode.UP);

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
                .divide(new BigDecimal(MyInsuranceConstants.OLD_TARIFF), RoundingMode.UP);

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
                .divide(new BigDecimal(MyInsuranceConstants.OLD_TARIFF), RoundingMode.UP);

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
                .divide(new BigDecimal(MyInsuranceConstants.NEW_TARIFF), RoundingMode.UP);

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
                .divide(new BigDecimal(MyInsuranceConstants.NEW_TARIFF), RoundingMode.UP)
                .add(new BigDecimal(7000).multiply(new BigDecimal(0)));


        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }



    @Test
    public void calculateOptionalExtraCost() {

        List<OptionalExtra> optionalExtraList = new ArrayList<>();
        optionalExtraList.add(optionalExtraRepository.findByCode("C"));
        optionalExtraList.add(optionalExtraRepository.findByCode("FI"));
        optionalExtraList.add(optionalExtraRepository.findByCode("AS"));
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2022,01,01).getTime(), //registration date
                PolicyType.RCA6); //type of policy
        logger.debug(cost.toString());

       cost = cost.add(quoteService.addOptionalExtraCosts(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2022,01,01).getTime(), //registration date
                optionalExtraList.size())); //list of optional extras


        logger.debug("with optional extras" + cost.toString());




        BigDecimal expectedCost = new BigDecimal(70000)
                .divide(new BigDecimal(MyInsuranceConstants.NEW_TARIFF), RoundingMode.UP)
                .add(new BigDecimal(70000).multiply(new BigDecimal(0))).add(new BigDecimal(135));//extra = 45*3=135



        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }




    @Test
    public void calculateOptionalExtraCost_02() {

        List<OptionalExtra> optionalExtraList = new ArrayList<>();
        optionalExtraList.add(optionalExtraRepository.findByCode("C"));
        optionalExtraList.add(optionalExtraRepository.findByCode("FI"));
        optionalExtraList.add(optionalExtraRepository.findByCode("AS"));
        BigDecimal cost= quoteService.calculateQuoteCost(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2000,01,01).getTime(), //registration date
                PolicyType.RCA6); //type of policy
        logger.debug(cost.toString());

        cost = cost.add(quoteService.addOptionalExtraCosts(
                new BigDecimal(70000),//value of the car
                new GregorianCalendar(2000,01,01).getTime(), //registration date
                optionalExtraList.size())); //tot of optional extras

        logger.debug("with optional extras" + cost.toString());


        BigDecimal expectedCost = new BigDecimal(70000)
                .divide(new BigDecimal(MyInsuranceConstants.OLD_TARIFF), RoundingMode.UP)
                .add(new BigDecimal(70000).multiply(new BigDecimal(0))).add(new BigDecimal(105));//extra = 35*3=105

        Assertions.assertEquals(expectedCost.setScale(2, RoundingMode.CEILING), cost);
    }



}
