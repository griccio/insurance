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
public class HolderService extends MyInsuranceService{
    @Autowired
    HolderRepository holderRepository;


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
            logger.error("Error finding the holder with id =" + id +"\nThe Holder with this id" + e.getMessage());
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
            //
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


    /**
     * return true if the email is already present into DB
     * @param email
     * @return
     */
    public Boolean verifyEmailExistence(String email){
        Boolean verifyEmail = holderRepository.verifyEmailExistence(email);

        if(verifyEmail)
            logger.error("Email is present,  another holder has already  been registered with the same email: " + email);
        else
            logger.debug("Email is not present: " + email);

        return verifyEmail;
    }


    public Boolean verifyEmailExistenceForUpdate(String email, Long id){
        Boolean verifyEmail = holderRepository.verifyEmailForExistingHolder(email, id);

        if(verifyEmail)
            logger.error("Email is present,  another holder has already been registered with the same email: " + email);
        else
            logger.debug("Email is not present: " + email);

        return verifyEmail;
    }


    /**
     * verify if the Fiscal Code is already assigned to an holder.
     * If it is present, the method returns true
     * @param fiscalCode
     * @return
     */
    public Boolean verifyFiscalCodeExistence(String fiscalCode){
        Boolean verifyFiscalCode = holderRepository.verifyFiscalCodeExistence(fiscalCode);

        if(verifyFiscalCode)
            logger.error("Fiscal Code is present,  another holder has already been registered with the same Fiscal Code: " + fiscalCode);
        else
            logger.debug("Fiscal Code is not present: " + fiscalCode);

        return verifyFiscalCode;
    }


    public Boolean verifyFiscalCodeExistenceForUpdate(String fiscalCode, Long id){
        Boolean verifyFiscalCode = holderRepository.verifyFiscalCodeForExistingHolder(fiscalCode, id);

        if(verifyFiscalCode)
            logger.error("Fiscal Code is present,  another holder has already been registered with the same Fiscal Code: " + fiscalCode);
        else
            logger.debug("Fiscal Code is not present: " + fiscalCode);

        return verifyFiscalCode;
    }
}
