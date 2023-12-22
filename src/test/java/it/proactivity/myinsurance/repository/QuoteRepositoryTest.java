package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.model.Car;
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

    @Autowired
    OptionalExtraRepository optionalExtraRepository;

    @Autowired
    CarRepository carRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuoteRepositoryTest.class);

    /**
     * INSERT INTO car(
     *     id, holder_id, registration_mark, registration_date, worth)
     * VALUES (200, 100, 'DR1234A', '2010-01-01',10000),
     *        (201, 101, 'CD2222A', '2022-01-01',30000),
     *        (202, 101, 'CD3344A', '2022-01-01',30000),
     *        (203, 103, 'EE1111EA', '2019-01-01',30000),
     *        (204, 103, 'EF2222WD', '2023-01-01',30000),
     *        (205, 102, 'AA333£WD', '2023-01-01',60000);
     *
     * INSERT INTO quote(
     *     id, holder_id, car_id,  policy_type, cost, quote_number,date)
     * VALUES (300, 100, 200, 'RCA6', 10, '100-DR1234A-1','2023-10-01 13:16:00'),
     *        (301, 101, 201, 'RCA6', 30, '100-CD2222A-1','2023-12-22 13:16:00'),
     *        (302, 101, 202, 'RCA12', 50, '100-CD2222A-2','2023-09-16 13:16:00'),
     *        (303, 103, 203, 'RCA6', 20, '100-CD2222A-1','2023-05-14 13:16:00'),
     *        (304, 103, 204, 'RCA50', 30, '100-CD2222A-2','2023-07-30 13:16:00');
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
     * return the list of quotes with holder info
     * Ebean is lazy by default
     */
    @Test
    public void findAllWithHolderAndData() {
        List<Quote> list = quoteRepository.findAllWithHolderAndCarData();
        Assertions.assertEquals(5, list.size());
        list.forEach(quote -> {logger.debug(quote.toString());
            logger.debug(quote.getHolder().toString());
            logger.debug(quote.getCar().toString());});

    }


    @Test
    public void findAllWithoutHolderData() {
        List<Quote> list = quoteRepository.findAll();
        Assertions.assertEquals(5, list.size());
        list.forEach(quote -> {logger.debug(quote.toString());
            logger.debug(quote.getHolder().toString());
            logger.debug(quote.getCar().toString());});

    }

    @Test
    public void findById() {
        Quote quote = quoteRepository.findById(302L);
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
        Assertions.assertEquals(1, list.size());
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
        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setCar(holder.getCarList().get(0));
        quote.setQuoteNumber("123456");
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
//            quote.setCar(holder.getCarList().get(0));
            quote.setQuoteNumber("123456"); //this value is overwritten by DB with the trigger updateQuoteNumber
            quote.setPolicyType(PolicyType.RCA12);
            quote.setCost(BigDecimal.valueOf(50));
            quote.setDate(new Date(System.currentTimeMillis()));
            quoteRepository.save(quote);
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
                Holder holder = holderRepository.findById(100L); // this holder has no quotes
                Quote quote = new Quote();
                quote.setHolder(holder);
                quote.setQuoteNumber("123456"); //this value is overwritten by DB with the trigger updateQuoteNumber
               // quote.setCar(holder.getCarList().get(0));
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

        Car car = new Car(holder,"123456",
                new GregorianCalendar(2018,01,01).getTime(),BigDecimal.valueOf(20000));
        carRepository.save(car);
        holder.addCar(car);
        holderRepository.update(holder);

        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setCar(holderRepository.getCar(holder.getId(),car.getRegistrationMark()));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote = quoteRepository.save(quote);


        Quote quote2 = quoteRepository.findById(quote.getId());
        quote2.addOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote2.addOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote2.addOptionalExtra(optionalExtraRepository.findByCode("AS"));
        quote2 = quoteRepository.save(quote2);

        Quote quote3 = quoteRepository.findById(quote2.getId());
        Assertions.assertEquals(3,quote3.getOptionalExtras().size());
    }


    @Test
    public void updateQuoteErrorBecauseCarNotExist() {


        try {
            Holder holder = holderRepository.findById(102L); // this holder has no quotes

            Car car = new Car(holder,"123456",
                    new GregorianCalendar(2018,01,01).getTime(),BigDecimal.valueOf(20000));
            carRepository.save(car);
            holder.addCar(car);
            holderRepository.update(holder);

            Quote quote  = new Quote();
            quote.setHolder(holder);
            quote.setCar(holderRepository.getCar(holder.getId(),"AASSWW3344"));
            quote.setHolder(holder);

            quote.setQuoteNumber("123456");

            quote.setPolicyType(PolicyType.RCA12);
            quote.setCost(BigDecimal.valueOf(50));
            quote.setDate(new Date(System.currentTimeMillis()));
            quote = quoteRepository.save(quote);
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

        Car car = new Car(holder,"123456",
                new GregorianCalendar(2018,01,01).getTime(),BigDecimal.valueOf(20000));
        carRepository.save(car);
        holder.addCar(car);
        holderRepository.update(holder);

        Quote quote  = new Quote();
        quote.setHolder(holder);
        quote.setCar(holderRepository.getCar(holder.getId(),car.getRegistrationMark()));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeTest + 1, quoteRepository.findAll().size());

        Assertions.assertTrue(quoteRepository.delete(quote));
        Assertions.assertEquals(quotesBeforeTest, quoteRepository.findAll().size());

    }

    @Test
    public void deleteQuoteErrorBecauseNotExist() {
        int quotesBeforeTest = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote  = new Quote();
        quote.setId(1234L);
        quote.setHolder(holder);
        quote.setQuoteNumber("123456");
        quote.setCar(holder.getCarList().get(0));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));

        Assertions.assertFalse(quoteRepository.delete(quote));

    }

    @Test
    public void getCarsListByHolder(){
        List<String> registrationsMark = holderRepository.getRegistrationMarks(103L);
        Assertions.assertNotNull(registrationsMark);
        Assertions.assertEquals(2, registrationsMark.size() );
        registrationsMark.forEach(reg -> logger.debug(reg));

    }

    @Test
    public void createQuoteWithOptionalExtras() {
        int quotesBeforeInsert = quoteRepository.findAll().size();

        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote = new Quote();
        quote.setHolder(holder);
        quote.setCar(holder.getCarList().get(0));


        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));

        quote.addOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("AS"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("TL"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("DC"));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeInsert + 1, quoteRepository.findAll().size());

    }


    @Test
    public void createQuoteWithOptionalExtrasAndCheck(){
        int quotesBeforeInsert = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote = new Quote();
        quote.setHolder(holder);
        quote.setCar(holder.getCarList().get(0));

        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("AS"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("TL"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("DC"));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeInsert + 1, quoteRepository.findAll().size());
        Quote quote3 = quoteRepository.findById(quote.getId());
        quote3.getOptionalExtras().forEach(extra ->logger.debug(extra.toString()));
        Assertions.assertEquals(5, quote3.getOptionalExtras().size());

    }



    @Test
    public void createQuoteWithOptionalAndBeforeSavingRemoveSomeAndCheck(){
        int quotesBeforeInsert = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote = new Quote();
        quote.setHolder(holder);
        quote.setCar(holder.getCarList().get(0));

        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("AS"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("TL"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("DC"));

        quote.removeOptionalExtra(optionalExtraRepository.findByCode("DC"));
        quote.removeOptionalExtra(optionalExtraRepository.findByCode("TL"));

        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeInsert + 1, quoteRepository.findAll().size());

        Quote quote2 = quoteRepository.findById(quote.getId());
        Assertions.assertEquals(3, quote2.getOptionalExtras().size());
        quote2.getOptionalExtras().forEach(extra ->logger.debug(extra.toString()));

    }

    @Test
    public void createQuoteWithOptionalAndAfterSavingRemoveSomeAndCheck(){

        int quotesBeforeInsert = quoteRepository.findAll().size();
        Holder holder = holderRepository.findById(100L); // this holder has no quotes
        Quote quote = new Quote();
        quote.setHolder(holder);
        quote.setCar(holder.getCarList().get(0));
        quote.setPolicyType(PolicyType.RCA12);
        quote.setCost(BigDecimal.valueOf(50));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("AS"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("TL"));
        quote.addOptionalExtra(optionalExtraRepository.findByCode("DC"));
        quote = quoteRepository.save(quote);

        Assertions.assertEquals(quotesBeforeInsert + 1, quoteRepository.findAll().size());
        Quote quote2 = quoteRepository.findById(quote.getId());
        quote2.removeOptionalExtra(optionalExtraRepository.findByCode("C"));
        quote2.removeOptionalExtra(optionalExtraRepository.findByCode("FI"));
        quote2 = quoteRepository.save(quote2);

        //check values
        Quote quote3 = quoteRepository.findById(quote2.getId());
        Assertions.assertEquals(3, quote3.getOptionalExtras().size());
        quote3.getOptionalExtras().forEach(extra ->logger.debug(extra.toString()));

    }

    @Test
    public void  verifyIfQuoteHasRegistrationMarkTrue(){
        Long quoteId = 300L;
        String registrationMark = "DR1234A";
        Assertions.assertTrue(quoteRepository.verifyRegistrationMarkNumberExist(quoteId, registrationMark));
}

    @Test
    public void  verifyIfQuoteHasRegistrationMarkFalse(){
        Long quoteId = 300L;
        String registrationMark = "GG1234A";
        Assertions.assertFalse(quoteRepository.verifyRegistrationMarkNumberExist(quoteId, registrationMark));
    }
}
