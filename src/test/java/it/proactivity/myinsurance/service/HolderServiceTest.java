package it.proactivity.myinsurance.service;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.exception.InvalidHolderException;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.HolderDTO;
import it.proactivity.myinsurance.model.HolderForUpdateDTO;
import it.proactivity.myinsurance.repository.CarRepository;
import it.proactivity.myinsurance.repository.HolderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class HolderServiceTest {
    @Autowired
    HolderService holderService;

    @Autowired
    HolderRepository holderRepository;

    @Autowired
    CarRepository carRepository;

    private static final Logger logger = LoggerFactory.getLogger(HolderServiceTest.class);
    final int TOT_HOLDERS_BEFORE_TEST = 4; //this is the tot of records into the holder table before each test



    @BeforeEach
    public void initTable() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");
    }

    @Test
    public void findAll() {
        List<Holder> list = holderService.findAll();
        Assertions.assertEquals(TOT_HOLDERS_BEFORE_TEST, list.size());
    }

    @Test
    public void findAllDTO() {
        List<Holder> holders = holderService.findAll();
        List<HolderDTO> holdersDTO = holders.stream()
                .sorted(Comparator.comparing(Holder::getSurname))
                .map(holder -> {
                    HolderDTO holderDTO = new HolderDTO();
                    BeanUtils.copyProperties(holder, holderDTO);
                    return holderDTO;
                }).collect(Collectors.toList());

        Assertions.assertEquals(TOT_HOLDERS_BEFORE_TEST, holdersDTO.size());
    }


    @Test
    public void findById() {
        HolderDTO holderDTO = holderService.findById(100L);
        Assertions.assertNotNull(holderDTO);
    }

    @Test
    public void findByIdWithErrorBecauseIdIsNull() {
        HolderDTO holderDTO = holderService.findById(null);
        Assertions.fail();
    }

    @Test
    public void findByIdWithErrorBecauseIdIsNegative() {
        HolderDTO holderDTO = holderService.findById(-123L);
        Assertions.fail();
    }

    @Test
    public void findByIdWithErrorBecauseNotFound() {
        HolderDTO holderDTO = holderService.findById(123L);
        Assertions.fail();
    }


    @Test
    public void create() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());
    }

    @Test
    public void createWithErrorBecauseExistingEmail() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            HolderDTO holderDTO = new HolderDTO();
            holderDTO.setName("Gustavo");
            holderDTO.setSurname("VillaFranca");
            holderDTO.setFiscalCode("VLLGTV80A01F205F");
            holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
            holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
            holderDTO.setTel("+346565678");
            holderDTO.setEmail("g.villafranca@gmail.com");

            holderService.save(holderDTO);
            Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());


            HolderDTO holder2DTO = new HolderDTO();
            holder2DTO.setName("Gustavo");
            holder2DTO.setSurname("VillaFranca");
            holder2DTO.setFiscalCode("VLLPPP80A01F205F");
            holder2DTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder2DTO.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder2DTO.setResidence("Via della Spiga 14/22 20019 Milano");
            holder2DTO.setTel("+346565678");
            holder2DTO.setEmail("g.villafranca@gmail.com");
            holderService.save(holder2DTO); //I have an exception because the email

        }catch(InvalidHolderException ie){
            logger.error(ie.getMessage());
            Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());
        }catch(Exception e){
            Assertions.fail();
        }
    }



    @Test
    public void createWithErrorBecauseExistingFiscalCode() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            HolderDTO holder = new HolderDTO();
            holder.setName("Gustavo");
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");

            holderService.save(holder);
            Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());

            HolderDTO holder2 = new HolderDTO();
            holder2.setName("Gustavo");
            holder2.setSurname("VillaFranca");
            holder2.setFiscalCode("VLLGTV80A01F205F");
            holder2.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder2.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder2.setResidence("Via della Spiga 14/22 20019 Milano");
            holder2.setTel("+346565678");
            holder2.setEmail("g.villafranca2@gmail.com");
            holderService.save(holder2); //I have an exception because the fiscal code


        }catch(InvalidHolderException ie){
            logger.error(ie.getMessage());
            Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());
        }catch(Exception e){
            Assertions.fail();
        }


    }


    @Test
    public void createWithErrorErrorBecauseNameIsMissing() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            HolderDTO holderDTO = new HolderDTO();
//        holderDTO.setName("Gustavo");
            holderDTO.setSurname("VillaFranca");
            holderDTO.setFiscalCode("VLLGTV80A01F205F");
            holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
            holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
            holderDTO.setTel("+346565678");
            holderDTO.setEmail("g.villafranca@gmail.com");
            holderService.save(holderDTO);
            Assertions.fail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertTrue(true);
        }
    }



    @Test
    public void createWithErrorBecauseNull() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            HolderDTO holderDTO = null;
            holderService.save(holderDTO);
            Assertions.fail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertTrue(true);
        }
    }


    @Test
    public void updateHolder() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());

        HolderForUpdateDTO holderForUpdateDTO = new HolderForUpdateDTO();
        holderForUpdateDTO.map(holderRepository.findById(id));

        holderForUpdateDTO.setName(holderDTO.getName() + "modified");
        holderForUpdateDTO.setSurname(holderDTO.getSurname() + "modified");
        holderForUpdateDTO.setDomicile(holderDTO.getDomicile() + "modified");
        holderForUpdateDTO.setResidence(holderDTO.getResidence() + "modified");
        holderForUpdateDTO.setBirthDate(new GregorianCalendar(2000, 01, 01).getTime());

        holderService.update(holderForUpdateDTO);

        Holder holder3 = holderRepository.findById(holderForUpdateDTO.getId());
        Assertions.assertEquals(holder3.getName(), holderDTO.getName() + "modified");
        Assertions.assertEquals(holder3.getSurname(),holderDTO.getSurname() + "modified");
        Assertions.assertEquals(holder3.getDomicile(), holderDTO.getDomicile() + "modified");
        Assertions.assertEquals(holder3.getResidence(), holderDTO.getResidence() + "modified");
        Assertions.assertEquals(holder3.getBirthDate(), new GregorianCalendar(2000, 01, 01).getTime());
    }




    @Test
    public void updateHolderWithErrorBecauseNullName() {
        int holdersBeforeInsert = holderService.findAll().size();
        long id =0;
        try {
            HolderDTO holder = new HolderDTO();
            holder.setName("Gustavo");
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");

            id = holderService.save(holder);

            Holder holder2 = holderRepository.findById(id);
            holder2.setName(null);
            HolderForUpdateDTO holderForUpdateDTO = new HolderForUpdateDTO();
            holderForUpdateDTO.map(holder2);
           holderService.update(holderForUpdateDTO);
           Assertions.fail();
        }catch(Exception e){
            logger.error(e.getMessage());
            Holder holder3 = holderRepository.findById(id);
            Assertions.assertEquals(holder3.getName(), "Gustavo");
        }

    }



    @Test
    public void updateHolderWithErrorBecauseTooLongName() {
        int holdersBeforeInsert = holderService.findAll().size();
        long id =0;
        try {
            HolderDTO holder = new HolderDTO();
            holder.setName("Gustavo");
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");

            id = holderService.save(holder);

            Holder holder2 = holderRepository.findById(id);
            holder2.setName("1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm" +
                    "1234567890qwertyuiopasdfghjklzxcvbnm");
            HolderForUpdateDTO holderForUpdateDTO = new HolderForUpdateDTO();
            holderForUpdateDTO.map(holder2);
            holderService.update(holderForUpdateDTO);
            Assertions.fail();
        }catch(Exception e){
            logger.error(e.getMessage());
            Holder holder3 = holderRepository.findById(id);
            Assertions.assertEquals(holder3.getName(), "Gustavo");
        }

    }



    @Test
    public void delete() {
        int holdersBeforeTest = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(holdersBeforeTest + 1, holderService.findAll().size());

        holderService.delete(id);

        Assertions.assertEquals(holdersBeforeTest , holderService.findAll().size());

    }




    @Test
    public void deleteandCheckIfAllCarsAreDeleted() {
        int holdersBeforeTest = holderService.findAll().size();
        int carsBeforeTest = carRepository.findByHolder(102L).size();


        holderService.delete(102L);

        Assertions.assertEquals(holdersBeforeTest - 1 , holderService.findAll().size());
        Assertions.assertEquals(carsBeforeTest - 1 , carRepository.findByHolder(102L).size());
    }


    @Test
    public void deleteWithErrorBecauseIdIsNull() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            HolderDTO holderDTO = new HolderDTO();
            holderDTO.setName("Gustavo");
            holderDTO.setSurname("VillaFranca");
            holderDTO.setFiscalCode("VLLGTV80A01F205F");
            holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
            holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
            holderDTO.setTel("+346565678");
            holderDTO.setEmail("g.villafranca@gmail.com");

            Long id = holderService.save(holderDTO);
            Assertions.assertNotNull(id);
            Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());

            id = null;
            holderService.delete(id); // go to the catch
            Assertions.fail();

        } catch (Exception e) {
            logger.error(e.getMessage());
            //deletion was not performed
            Assertions.assertEquals(holdersBeforeInsert +1, holderService.findAll().size());
        }

    }


    @Test
    public void deleteWithErrorBecauseHolderDoNotExist() {

        try {
            Long id = 12345L;
            holderService.delete(id); // go to the catch
            Assertions.fail();

        } catch (Exception e) {
            logger.error(e.getMessage());
       }

    }



    @Test
    public void verifyEmailThatIsPresent() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        holderService.save(holderDTO);
        Assertions.assertTrue(holderService.verifyEmailExistence(holderDTO.getEmail()));

    }

    @Test
    public void verifyEmailExistenceForUpdateFalse() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertFalse(holderService.verifyEmailExistenceForUpdate(holderDTO.getEmail(), id));

    }


    @Test
    public void verifyEmailThatNotExist() {
        Assertions.assertFalse(holderService.verifyEmailExistence("g.villafrancaxxx@gmail.com"));
    }
    @Test
    public void verifyEmailExistenceForUpdateTrue() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertTrue(holderService.verifyEmailExistenceForUpdate("giu.riccio@gmail.com",id));

    }




    @Test
    public void verifyFiscalCodeThatIsPresent() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        holderService.save(holderDTO);
        Assertions.assertTrue(holderService.verifyFiscalCodeExistence(holderDTO.getFiscalCode()));

    }



    @Test
    public void verifyFiscalCodeThatNotExist() {
        Assertions.assertFalse(holderService.verifyFiscalCodeExistence("VXXGTV80A01F205F"));
    }

    @Test
    public void verifyFiscalCodeExistenceForUpdateTrue() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");

        Long id = holderService.save(holderDTO);
        Assertions.assertTrue(holderService.verifyFiscalCodeExistenceForUpdate("GPPIRCCO456789A", id));

    }
}
