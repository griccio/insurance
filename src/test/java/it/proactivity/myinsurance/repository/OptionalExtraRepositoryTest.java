package it.proactivity.myinsurance.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionalExtraRepositoryTest {


    @Autowired
    OptionalExtraRepository optionalExtraRepository;

    @Test
    public void findByCode(){
        Assertions.assertNotNull(optionalExtraRepository.findByCode("FI"));
    }


    @Test
    public void findByCodeWithErrorBecauseNotExist(){
        Assertions.assertNull(optionalExtraRepository.findByCode("XX"));
    }

}
