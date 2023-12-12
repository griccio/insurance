package it.proactivity.myinsurance.service;

import io.ebean.DB;
import io.ebean.Database;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.HolderDTO;
import it.proactivity.myinsurance.repository.HolderRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class HolderServiceTest {
    @Autowired
    HolderService holderService;

    private static final Logger logger = LoggerFactory.getLogger(HolderServiceTest.class);


    @BeforeEach
    public void initTable() {
        Database database = DB.getDefault();
        database.script().run("/scripttest/insert_db.sql");
    }

    @Test
    public void findAll() {
        List<Holder> list = holderService.findAll();
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void findAllDTO() {
        List<Holder> holders = holderService.findAll();
        List<HolderDTO> holdersDTO = holders.stream()
                .sorted(Comparator.comparing(Holder::getSurname))
                .collect(Collectors.mapping(
                        holder -> {
                            HolderDTO holderDTO = new HolderDTO();
                            BeanUtils.copyProperties(holder, holderDTO);
                            return holderDTO;
                            },
                        Collectors.toList()));

        Assertions.assertEquals(3, holdersDTO.size());
    }


    @Test
    public void findById() {
        Holder holder = holderService.findById(100L);
        Assertions.assertNotNull(holder);
    }



    @Test
    public void create() {
        int holdersBeforeInsert = holderService.findAll().size();
        Holder holder = new Holder();
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
    }

    @Test
    public void createWithError() {
        int holdersBeforeInsert = holderService.findAll().size();
        try {
            Holder holder = new Holder();
//        holder.setName("Gustavo");
            holder.setSurname("VillaFranca");
            holder.setFiscalCode("VLLGTV80A01F205F");
            holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
            holder.setDomicile("Via della Spiga 14/22 20019 Milano");
            holder.setResidence("Via della Spiga 14/22 20019 Milano");
            holder.setTel("+346565678");
            holder.setEmail("g.villafranca@gmail.com");
            holderService.save(holder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Assertions.assertEquals(holdersBeforeInsert, holderService.findAll().size());
        }
    }



    @Test
    public void createFromDTO() {
        int holdersBeforeInsert = holderService.findAll().size();
        Holder holderDTO = new Holder();
        Holder holder = new Holder();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");
        BeanUtils.copyProperties(holderDTO,holder);
        Long id = holderService.save(holder);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());
    }

    @Test
    public void createFromDTO2() {
        int holdersBeforeInsert = holderService.findAll().size();
        HolderDTO holderDTO = new HolderDTO();
//        Holder holder = new Holder();
        holderDTO.setName("Gustavo");
        holderDTO.setSurname("VillaFranca");
        holderDTO.setFiscalCode("VLLGTV80A01F205F");
        holderDTO.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holderDTO.setDomicile("Via della Spiga 14/22 20019 Milano");
        holderDTO.setResidence("Via della Spiga 14/22 20019 Milano");
        holderDTO.setTel("+346565678");
        holderDTO.setEmail("g.villafranca@gmail.com");
//        BeanUtils.copyProperties(holderDTO,holder);
        Long id = holderService.save(holderDTO);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(holdersBeforeInsert + 1, holderService.findAll().size());
    }




    @Test
    public void updateHolder() {
        int holdersBeforeInsert = holderService.findAll().size();
        Holder holder = new Holder();
        holder.setName("Gustavo");
        holder.setSurname("VillaFranca");
        holder.setFiscalCode("VLLGTV80A01F205F");
        holder.setBirthDate(new GregorianCalendar(1980, 01, 01).getTime());
        holder.setDomicile("Via della Spiga 14/22 20019 Milano");
        holder.setResidence("Via della Spiga 14/22 20019 Milano");
        holder.setTel("+346565678");
        holder.setEmail("g.villafranca@gmail.com");

        holderService.save(holder);

        Holder holder2 = holderService.findById(holder.getId());
        holder2.setName(holder2.getName() + "modified");
        holder2.setSurname(holder2.getSurname() + "modified");
        holder2.setDomicile(holder2.getDomicile() + "modified");
        holder2.setResidence(holder2.getResidence() + "modified");
        holder2.setBirthDate(new GregorianCalendar(2000, 01, 01).getTime());

        holderService.update(holder2);

        Holder holder3 = holderService.findById(holder2.getId());
        Assertions.assertEquals(holder3.getName(), holder2.getName());
        Assertions.assertEquals(holder3.getSurname(), holder2.getSurname());
        Assertions.assertEquals(holder3.getDomicile(), holder2.getDomicile());
        Assertions.assertEquals(holder3.getResidence(), holder2.getResidence());
        Assertions.assertEquals(holder3.getBirthDate(), new GregorianCalendar(2000, 01, 01).getTime());

    }




    @Test
    public void updateHolderWithNullName() {
        int holdersBeforeInsert = holderService.findAll().size();
        long holderId =0;
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

            holderService.save(holder);
            holderId = holder.getId();
            Holder holder2 = holderService.findById(holder.getId());
            holder2.setName(null);
           holderService.update(holder2);
        }catch(Exception e){
            logger.error(e.getMessage());
            Holder holder3 = holderService.findById(holderId);
            Assertions.assertEquals(holder3.getName(), "Gustavo");
        }

    }



    @Test
    public void delete() {
        int holdersBeforeInsert = holderService.findAll().size();
        Holder holder = new Holder();
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

        holderService.delete(holder.getId());

        Assertions.assertEquals(holdersBeforeInsert , holderService.findAll().size());

    }

}
