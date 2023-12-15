package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.PolicyType;
import it.proactivity.myinsurance.model.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootTest
public class QuoteRepositoryTest {
    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    HolderRepository holderRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuoteRepositoryTest.class);

    /**
     * INSERT INTO quote(
     *     id, holder_id, registration_mark, registration_car_date, worth, policy_type, cost, quote_number)
     * VALUES (100, 100, 'DR1234A', '2010-01-01',10000, 'RCA6', 10, '100-DR1234A-100'),
     *        (101, 101, 'CD2222A', '2022-01-01',30000, 'RCA6', 30, '100-CD2222A-101'),
     *        (102, 101, 'CD2222A', '2022-01-01',30000, 'RCA12', 50, '100-CD2222A-102'),
     *        (103, 103, 'EE1111EA', '2019-01-01',30000, 'RCA6', 20, '100-CD2222A-102'),
     *        (104, 103, 'EF2222WD', '2023-01-01',30000, 'RCA50', 30, '100-CD2222A-102');
     */
    @BeforeEach
    public void initTable() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");
    }

//    @AfterAll
    public static void restoreDb() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");
    }

    @Test
    public void findAll() {

        List<Quote> list = quoteRepository.findAll();
        Assertions.assertEquals(5, list.size());
        list.forEach(quote -> logger.debug(quote.toString()));

    }


    /**
     * return the list of quotes and for each quote the holder's data
     * Ebean is lazy by default
     */
    @Test
    public void findAllWithHolderData() {
        List<Quote> list = quoteRepository.findAllWithHolderData();
        Assertions.assertEquals(5, list.size());
        list.forEach(quote -> logger.debug(quote.toString()));

    }

    @Test
    public void findById() {
        Quote quote = quoteRepository.findById(102L);
        Assertions.assertNotNull(quote);
        logger.debug(quote.toString());

    }

    @Test
    public void findByIdNotExist() {
        Quote quote = quoteRepository.findById(1092L);
        Assertions.assertNull(quote);
    }


    @Test
    public void findByRegistrationMark() {
        List<Quote> list = quoteRepository.findByRegistrationMark("CD2222A");
        Assertions.assertEquals(2, list.size());
        list.forEach(quote -> logger.debug(quote.toString()));
    }

    @Test
    public void findByRegistrationMarkNotExist() {
        List<Quote> list = quoteRepository.findByRegistrationMark("PP2222A");
        Assertions.assertNotNull( list);
        Assertions.assertEquals(0, list.size());
    }

    @Test
    public void findByHolderId() {
        List<Quote> list = quoteRepository.findByHolderId(101L);
        Assertions.assertEquals(2, list.size());
        list.forEach(quote -> logger.debug(quote.toString()));
    }



    @Test
    public void findByHolderIdAndRegistrationMark() {
        List<Quote> list = quoteRepository.findByHolderIdAndRegistrationMark(103L,"EE1111EA");
        Assertions.assertEquals(1, list.size());
        list.forEach(quote -> logger.debug(quote.toString()));
    }

    @Test
    public void createQuote() {
        int quotesBeforeInsert = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(102L); // this holder has no quotes
        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setRegistrationMark("AA1234BB");
        quote.setQuoteNumber("123456");
        quote.setRegistrationDateCar(new GregorianCalendar(2018,01,01).getTime());
        quote.setWorth(BigDecimal.valueOf(20000));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeInsert + 1, quoteRepository.findAll().size());
    }


    @Test
    public void createQuoteWithErrorBecauseNullHolder() {
        int quotesBeforeInsert = quoteRepository.findAll().size();
        try {
            Holder holder = new Holder(); // this holder is empty
            Quote quote = new Quote();
            quote.setHolder(holder);
            quote.setRegistrationMark("AA1234BB");
            quote.setQuoteNumber("123456"); //this value is overwritten by DB with the trigger updateQuoteNumber
            quote.setRegistrationDateCar(new GregorianCalendar(2018, 01, 01).getTime());
            quote.setWorth(BigDecimal.valueOf(20000));
            quote.setPolicyType(PolicyType.RCA12);
            quote.setCost(BigDecimal.valueOf(50));
            quote.setDate(new Date(System.currentTimeMillis()));
            quote = quoteRepository.save(quote);
            Assertions.fail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(quotesBeforeInsert, quoteRepository.findAll().size());
        }

    }

        @Test
        public void createQuoteWithErrorBecauseEmptyRegistrationMark() {
            int quotesBeforeInsert = quoteRepository.findAll().size();
            try {
                Holder holder = holderRepository.findById(102L); // this holder has no quotes
                Quote quote = new Quote();
                quote.setHolder(holder);
//                quote.setRegistrationMark("AA1234BB");
                quote.setQuoteNumber("123456"); //this value is overwritten by DB with the trigger updateQuoteNumber
                quote.setRegistrationDateCar(new GregorianCalendar(2018, 01, 01).getTime());
                quote.setWorth(BigDecimal.valueOf(20000));
                quote.setPolicyType(PolicyType.RCA12);
                quote.setCost(BigDecimal.valueOf(50));
                quote.setDate(new Date(System.currentTimeMillis()));
                quote = quoteRepository.save(quote);
                Assertions.fail();
            }catch(Exception e){
                logger.error(e.getMessage());
                Assertions.assertEquals(quotesBeforeInsert, quoteRepository.findAll().size());
            }

        }


    @Test
    public void updateQuote() {

        Holder holder = holderRepository.findById(102L); // this holder has no quotes

        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setRegistrationMark("AA1234BB");
        quote.setQuoteNumber("123456");
        quote.setRegistrationDateCar(new GregorianCalendar(2018,01,01).getTime());
        quote.setWorth(BigDecimal.valueOf(20000));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote = quoteRepository.save(quote);


        Quote quote2 = quoteRepository.findById(quote.getId());
        quote2.setRegistrationMark("BB111AA");
        quote2.setQuoteNumber("AABBCCDD");
        quote2.setRegistrationDateCar(new GregorianCalendar(2020,01,01).getTime());
        quote2.setWorth(BigDecimal.valueOf(25555));
        quote2.setPolicyType(PolicyType.RCA50);
        quote2.setCost(BigDecimal.valueOf(100.89));
        quote2.setDate(new Date(System.currentTimeMillis()));
        quote2 = quoteRepository.save(quote2);

        Quote quote3 = quoteRepository.findById(quote2.getId());
        Assertions.assertEquals("BB111AA",quote3.getRegistrationMark());
        Assertions.assertEquals("AABBCCDD",quote3.getQuoteNumber());
        Assertions.assertEquals(new GregorianCalendar(2020,01,01).getTime(), quote3.getRegistrationDateCar());
        Assertions.assertEquals(PolicyType.RCA50,quote3.getPolicyType());
        Assertions.assertEquals(BigDecimal.valueOf(100.89),quote3.getCost());
    }


    @Test
    public void updateQuoteErrorBecauseRegistrationMark() {

        Holder holder = holderRepository.findById(102L); // this holder has no quotes
        try {
            Quote quote = new Quote();
            quote.setHolder(holder);
            quote.setRegistrationMark("AA1234BB");
            quote.setQuoteNumber("123456");
            quote.setRegistrationDateCar(new GregorianCalendar(2018, 01, 01).getTime());
            quote.setWorth(BigDecimal.valueOf(20000));
            quote.setPolicyType(PolicyType.RCA12);
            quote.setCost(BigDecimal.valueOf(50));
            quote.setDate(new Date(System.currentTimeMillis()));
            quote = quoteRepository.save(quote);


            Quote quote2 = quoteRepository.findById(quote.getId());
            quote2.setRegistrationMark("BB111AAASASASASASASDSDSDSDSDSDSDSDSDSDSDSDSDSDSDSD");
            quote2.setQuoteNumber("AABBCCDD");
            quote2.setRegistrationDateCar(new GregorianCalendar(2020, 01, 01).getTime());
            quote2.setWorth(BigDecimal.valueOf(25555));
            quote2.setPolicyType(PolicyType.RCA50);
            quote2.setCost(BigDecimal.valueOf(100.89));
            quote2.setDate(new Date(System.currentTimeMillis()));
            quote2 = quoteRepository.save(quote2);
            Assertions.fail();

        } catch (Exception e) {
            logger.error(e.toString());
            Assertions.assertTrue(true);
        }
    }


    @Test
    public void deleteQuote() {
        int quotesBeforeTest = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(102L); // this holder has no quotes
        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setRegistrationMark("AA1234BB");
        quote.setQuoteNumber("123456");
        quote.setRegistrationDateCar(new GregorianCalendar(2018,01,01).getTime());
        quote.setWorth(BigDecimal.valueOf(20000));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeTest + 1, quoteRepository.findAll().size());

        Assertions.assertTrue(quoteRepository.delete(quote));

    }
    @Test
    public void deleteQuoteErrorBecauseNotExist() {
        int quotesBeforeTest = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(102L); // this holder has no quotes
        Quote quote  = new Quote();
        quote.setId(1234L);
        quote.setHolder(holder);
        quote.setRegistrationMark("AA1234BB");
        quote.setQuoteNumber("123456");
        quote.setRegistrationDateCar(new GregorianCalendar(2018,01,01).getTime());
        quote.setWorth(BigDecimal.valueOf(20000));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));

        Assertions.assertFalse(quoteRepository.delete(quote));

    }

    @Test
    public void getCarsListByHolder(){
        List<String> registrationsMark = quoteRepository.getCarsListOwnedByHolder(103L);
        Assertions.assertNotNull(registrationsMark);
        registrationsMark.forEach(reg -> logger.debug(reg));

    }

}
