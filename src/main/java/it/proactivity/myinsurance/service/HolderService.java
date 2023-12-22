package it.proactivity.myinsurance.service;

import io.ebean.annotation.Transactional;
import it.proactivity.myinsurance.exception.InvalidHolderException;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.dto.HolderDTO;
import it.proactivity.myinsurance.model.dto.HolderForUpdateDTO;
import it.proactivity.myinsurance.repository.HolderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolderService extends MyInsuranceService {
    @Autowired
    HolderRepository holderRepository;


    public List<Holder> findAll() {

        List<Holder> list = holderRepository.findAll();
        logger.debug("Holders Found: " + list.size());
        return list;

    }


    public List<HolderDTO> findAllDTO() {

        List<Holder> holders = findAll();
        logger.debug("holders loaded correctly, tot: "+ holders.size());
        List<HolderDTO> holdersDTO = new ArrayList<>();

        if (holders == null)
            return holdersDTO;

        holdersDTO = holders.stream()
                .sorted(Comparator.comparing(Holder::getSurname))
                .map(holder -> {
                    HolderDTO holderDTO = new HolderDTO();
                    BeanUtils.copyProperties(holder, holderDTO);
                    return holderDTO;
                }).collect(Collectors.toList());

        logger.debug("Holders Found: " + holders.size());
        return holdersDTO;

    }

    /**
     * @param id
     * @return null if not exist or id is incorrct (null negative)
     */
    public HolderDTO findById(Long id) {
        Holder holder = holderRepository.findById(id);
        if (holder == null) {
            logger.error("No holder  found with id = " + id);
            return null;
        } else {
            logger.debug("Holder Found: " + holder.toString());
            HolderDTO holderDTO = new HolderDTO();
            holderDTO.map(holder);
            return holderDTO;
        }
    }

    public Holder getHolderById(Long id) {
        Holder holder = holderRepository.findById(id);
        if (holder == null) {
            logger.error("No holder  found with id = " + id);
            return null;
        } else {
            logger.debug("Holder Found: " + holder.toString());
            return holder;
        }
    }


    /**
     * @param holderDTO
     * @return the id of the new holder otherwise 0
     */
    @Transactional
    public Long save(HolderDTO holderDTO) {

        if (holderDTO == null)
            throw new InvalidHolderException("Holder is null");

        if (verifyEmailExistence(holderDTO.getEmail()))
            throw new InvalidHolderException("The assigned email is already present the holder cannot be saved");

        if (verifyFiscalCodeExistence(holderDTO.getFiscalCode()))
            throw new InvalidHolderException("Fiscal Code is already present,  the holder cannot be saved");

        Holder holder = new Holder();
        BeanUtils.copyProperties(holderDTO, holder);
        holderRepository.save(holder);
        logger.debug("Holder saved correctly; id:" + holder.getId());
        return holder.getId();

    }


    @Transactional
    public Long update(HolderForUpdateDTO holderForUpdateDTO) {

        if (holderForUpdateDTO == null)
            throw new InvalidHolderException("Holder is null");

        if (verifyEmailExistenceForUpdate(holderForUpdateDTO.getEmail(), holderForUpdateDTO.getId()))
            throw new InvalidHolderException("The assigned email is already present the holder cannot be updated");

        if (verifyFiscalCodeExistenceForUpdate(holderForUpdateDTO.getFiscalCode(), holderForUpdateDTO.getId()))
            throw new InvalidHolderException("The assigned Fiscal Code is already present, the holder cannot be updated");

        Holder holderBeforeUpdate = holderRepository.findById(holderForUpdateDTO.getId());
        logger.debug("Holder before updating " + holderBeforeUpdate.toString());

        Holder holder = new Holder();
        BeanUtils.copyProperties(holderForUpdateDTO, holder);
        holderRepository.update(holder);

        logger.debug("Holder after updating " + holder.toString());
        return holder.getId();
    }

    @Transactional
    public Long delete(Long id) {

        if (id == null)
            throw new InvalidHolderException("id is null");

        Holder holder = holderRepository.findById(id);

        if (holder == null)
            throw new InvalidHolderException("Holder doesn't exist and cannot be deleted");

        logger.debug("Holder Found  " + holder.toString());
        Boolean  deleteOk = holderRepository.delete(holder);
        logger.debug("Holder deleted Correctly ");
        return (deleteOk ? holder.getId():null);

    }


    /**
     * return true if the email is already present into DB
     *
     * @param email
     * @return
     */
    public Boolean verifyEmailExistence(String email) {
        Boolean verifyEmail = holderRepository.verifyEmailExistence(email);

        if (verifyEmail)
            logger.error("Email is already present,  another holder has already  been registered with the same email: " + email);
        else
            logger.debug("Email is not present: " + email);

        return verifyEmail;
    }


    public Boolean verifyEmailExistenceForUpdate(String email, Long id) {
        Boolean verifyEmail = holderRepository.verifyEmailForExistingHolder(email, id);

        if (verifyEmail)
            logger.error("Email is present,  another holder has already been registered with the same email: " + email);
        else
            logger.debug("Email is not present: " + email);

        return verifyEmail;
    }


    /**
     * verify if the Fiscal Code is already assigned to an holder.
     * If it is present, the method returns true
     *
     * @param fiscalCode
     * @return
     */
    public Boolean verifyFiscalCodeExistence(String fiscalCode) {
        Boolean verifyFiscalCode = holderRepository.verifyFiscalCodeExistence(fiscalCode);

        if (verifyFiscalCode)
            logger.error("Fiscal Code is present,  another holder has already been registered with the same Fiscal Code: " + fiscalCode);
        else
            logger.debug("Fiscal Code is not present: " + fiscalCode);

        return verifyFiscalCode;
    }


    public Boolean verifyFiscalCodeExistenceForUpdate(String fiscalCode, Long id) {
        Boolean verifyFiscalCode = holderRepository.verifyFiscalCodeForExistingHolder(fiscalCode, id);

        if (verifyFiscalCode)
            logger.error("Fiscal Code is present,  another holder has already been registered with the same Fiscal Code: " + fiscalCode);
        else
            logger.debug("Fiscal Code is not present: " + fiscalCode);

        return verifyFiscalCode;
    }


}
