package it.proactivity.myinsurance.repository;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.model.Holder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.GregorianCalendar;
import java.util.List;

@SpringBootTest
public class HolderRepositoryTest {
    @Autowired
    HolderRepository holderRepository;

    private static final Logger logger = LoggerFactory.getLogger(HolderRepositoryTest.class);

    @BeforeEach
    public void initTable() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");
    }

    @AfterAll
    public static void restoreDb() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db_for_test.sql");

    }

    @Test
    public void findAll() {

        List<Holder> list = holderRepository.findAll();
        Assertions.assertEquals(4, list.size());
    }

    @Test
    public void findById() {
        Holder holder = holderRepository.findById(100L);
        Assertions.assertNotNull(holder);
    }

    @Test
    public void findByIdNotExist() {
        Holder holder = holderRepository.findById(1990L);
        Assertions.assertNull(holder);
    }

    @Test
    public void findBySurname() {
        List<Holder> list = holderRepository.findBySurname("Riccio");
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void findBySurnameWithPartialSurname() {
        List<Holder> list = holderRepository.findBySurname("cio");
        Assertions.assertEquals(1, list.size());

    }

    @Test
    public void findByFiscalCodeWithPartialFiscalCode() {
        List<Holder> list = holderRepository.findByFiscalCode("RCCO4");
        Assertions.assertEquals(1, list.size());
    }


    @Test
    public void createHolder() {
        int holdersBeforeInsert = holderRepository.findAll().size();
        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);

        Assertions.assertEquals(holdersBeforeInsert + 1, holderRepository.findAll().size());
    }

    @Test
    public void createHolderWithoutName() {
        int holdersBeforeInsert = holderRepository.findAll().size();

        try {
            Holder holder = new Holder();
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");
            holderRepository.save(holder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(holdersBeforeInsert, holderRepository.findAll().size());
        }
    }

    @Test
    public void createHolderWithWrongName() {
        int holdersBeforeInsert = holderRepository.findAll().size();

        try {
            Holder holder = new Holder();
            holder.setSurname("VillaFranca");
            holder.setName("ASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPAS" +
                    "DFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPA" +
                    "ASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPSDFGHJKLPASDFGHJKLP");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");
            holderRepository.save(holder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(holdersBeforeInsert, holderRepository.findAll().size());
        }
    }


    @Test
    public void updateHolder() {
        int holdersBeforeInsert = holderRepository.findAll().size();
        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Holder holder2 = holderRepository.findById(holder.getId());
        holder2.setName(holder2.getName() + "modified");
        holder2.setSurname(holder2.getSurname() + "modified");
        holder2.setDomicile(holder2.getDomicile() + "modified");
        holder2.setResidence(holder2.getResidence() + "modified");
        holder2.setBirthDate(new GregorianCalendar(2000, 01, 01).getTime());
        holderRepository.update(holder2);

        Holder holder3 = holderRepository.findById(holder2.getId());
        Assertions.assertEquals(holder3.getName(), holder2.getName());
        Assertions.assertEquals(holder3.getSurname(), holder2.getSurname());
        Assertions.assertEquals(holder3.getDomicile(), holder2.getDomicile());
        Assertions.assertEquals(holder3.getResidence(), holder2.getResidence());
        Assertions.assertEquals(holder3.getBirthDate(), new GregorianCalendar(2000, 01, 01).getTime());

    }


    @Test
    public void updateHolderWithWrongName() {
        int holdersBeforeInsert = holderRepository.findAll().size();
        try {
            Holder holder = new Holder();
            holder.setName("Gustavo");
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");
            holderRepository.save(holder);
            Assertions.assertNotNull(holder.getId());

            Holder holder2 = holderRepository.findById(holder.getId());
            holder2.setName("ASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPAS" +
                    "DFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPA" +
                    "ASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPASDFGHJKLPSDFGHJKLPASDFGHJKLP");

            holder2.setSurname(holder2.getSurname() + "modified");
            holder2.setDomicile(holder2.getDomicile() + "modified");
            holder2.setResidence(holder2.getResidence() + "modified");
            holder2.setBirthDate(new GregorianCalendar(2000, 01, 01).getTime());
            holderRepository.update(holder2);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Holder holder2 = holderRepository.findByName("Gustavo").get(0);
            Assertions.assertNotNull(holder2);
        }

    }

    @Test
    public void deleteHolder() {
        int holdersBeforeInsert = holderRepository.findAll().size();
        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());
        int holdersAfterInsert = holderRepository.findAll().size();

        Assertions.assertEquals(holdersBeforeInsert + 1, holdersAfterInsert);

        holderRepository.delete(holder);

        Assertions.assertEquals(holdersBeforeInsert, holderRepository.findAll().size());

    }

    @Test
    public void verifyEmailExistence(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertTrue(holderRepository.verifyEmailExistence(holder.getEmail()));

    }

    @Test
    public void verifyEmailForExistingHolderFalse(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertFalse(holderRepository.verifyEmailForExistingHolder(holder.getEmail(), holder.getId()));

    }


    @Test
    public void verifyEmailForExistingHolderTrue(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertTrue(holderRepository.verifyEmailForExistingHolder("giu.riccio@gmail.com", holder.getId()));

    }
    @Test
    public void verifyEmailNotExist(){
        int holdersBeforeInsert = holderRepository.findAll().size();

        Assertions.assertFalse(holderRepository.verifyEmailExistence("g.villafrancaxxx@gmail.com"));

    }


    @Test
    public void verifyFiscalCodeThatIsPresent(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertTrue(holderRepository.verifyFiscalCodeExistence(holder.getFiscalCode()));

    }


    @Test
    public void verifyFiscalCodeThatNotExist(){
        Assertions.assertFalse(holderRepository.verifyFiscalCodeExistence("VXXGTV80A01F205F"));
    }



    @Test
    public void verifyFiscalCodeForExistingHolderFalse(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertFalse(holderRepository.verifyFiscalCodeForExistingHolder(holder.getFiscalCode(), holder.getId()));

    }


    @Test
    public void verifyFiscalCodeForExistingHolderTrue(){

        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");
        holderRepository.save(holder);
        Assertions.assertNotNull(holder.getId());

        Assertions.assertTrue(holderRepository.verifyFiscalCodeForExistingHolder("GPPIRCCO456789A", holder.getId()));

    }
}
