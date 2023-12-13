package it.proactivity.myinsurance.service;

import io.ebean.annotation.Transactional;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.HolderDTO;
import it.proactivity.myinsurance.repository.HolderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolderService {
    @Autowired
    HolderRepository holderRepository;


    private static final Logger logger = LoggerFactory.getLogger(HolderService.class);




    public List<Holder> findAll() {
        try {
            List<Holder> list = holderRepository.findAll();
            logger.debug("Holders Found: "+ list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error finding all holder "+ e.getMessage());
            return null;
        }
    }



    public Holder findById(Long id) {
        try {
            Holder holder = holderRepository.findById(id);
            logger.debug("Holder Found: "+ holder.toString());
            return holder;
        } catch (Exception e) {
            logger.error("Error finding the holder with id =" + id +"\n" + e.getMessage());
            return null;
        }
    }



    @Transactional
    public Long save(Holder holder) {
        try {
            holderRepository.save(holder);
            logger.debug("Holder saved correctly; "+ holder.toString());
            return holder.getId();
        } catch (Exception e) {
            logger.error("Error saving the holder " + holder.toString() +"\n" + e.getMessage());
            return null;
        }
    }


    @Transactional
    public Long update(Holder holder) {
        try {
            Holder holderBeforeUpdate = holderRepository.findById(holder.getId());
            logger.debug("Holder before updating " +holderBeforeUpdate.toString());
            holderRepository.update(holder);
            logger.debug("Holder after updating " +holder.toString());
            return holder.getId();
        } catch (Exception e) {
            logger.error("Error updating the holder " + holder.toString() +"\n" + e.getMessage());
            return null;
        }
    }

    @Transactional
    public Long delete(Long id) {
        try {
            Holder holder = holderRepository.findById(id);
            logger.debug("Holder Found  " + holder.toString());
            holderRepository.delete(holder);
            logger.debug("Holder deleted Correctly ");
            return holder.getId();
        } catch (Exception e) {
            logger.error("Error deleting the holder with id " + id +"\n" + e.getMessage());
            return null;
        }
    }

}
