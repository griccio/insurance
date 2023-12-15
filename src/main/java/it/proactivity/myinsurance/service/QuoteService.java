package it.proactivity.myinsurance.service;

import io.ebean.annotation.Transactional;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.*;
import it.proactivity.myinsurance.repository.HolderRepository;
import it.proactivity.myinsurance.repository.QuoteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QuoteService extends MyInsuranceService{
    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    HolderRepository holderRepository;

    public static final GregorianCalendar STARTING_NEW_TARIFF_DATE = new GregorianCalendar(2020, Calendar.JANUARY,01);
    public static final int NEW_TARIFF = 1000;
    public static final int OLD_TARIFF = 750;

    public static final int WORTH_CAR_LIMIT = 10000;

    @Transactional
    public List<Quote> findAll(){
        try {
            List<Quote> list = quoteRepository.findAll();
            logger.debug("Quotes loaded correctly, tot: "+ list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error loading all quotes "+ e.getMessage());
            return null;
        }
    }


    public Quote findById(Long id) {
        try {
            Quote quote = quoteRepository.findByIdWithHolderData(id);
            if(quote != null)
                 logger.debug("Quote Found: "+ quote.toString());
            else
                logger.debug("Quote is null ");
            return quote;
        } catch (Exception e) {
            logger.error("Error finding the quote with id =" + id +"\n" + e.getMessage());
            return null;
        }
    }


    public List<Quote> findByRegistrationMark(String registrationMark) {
        try {
            List<Quote> list = quoteRepository.findByRegistrationMark(registrationMark);
            logger.debug("Quotes loaded correctly by registration mark, tot: "+ list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error loading  quotes by registration mark"+ e.getMessage());
            return null;
        }
    }


    public List<Quote> findByHolderId(Long id) {
        try {
            List<Quote> list = quoteRepository.findByHolderId(id);
            logger.debug("Quotes loaded correctly by holder id, tot: "+ list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error loading  quotes by holder id"+ e.getMessage());
            return null;
        }
    }

    public List<QuoteByHolderAndCarDTO> findByHolderIdGroupByCar(QuoteByHolderAndCarDTO quoteByCarDTO) {

        try {
            List<QuoteByHolderAndCarDTO> quoteByCarDTOList = new ArrayList<>();
            // get the list of the car
            List<String> list = quoteRepository.getCarsListOwnedByHolder(quoteByCarDTO.getHolder_id());
            logger.debug("The Holder with this id " + quoteByCarDTO.getHolder_id() + " has these cars " + list);

            list.forEach(reg -> {
                QuoteByHolderAndCarDTO qByCarDTO = new QuoteByHolderAndCarDTO();
                                 qByCarDTO.setHolder_id(quoteByCarDTO.getHolder_id());
                                 qByCarDTO.setRegistrationMark(reg);
                                 List<Quote> quotelist = quoteRepository.findByRegistrationMark(reg);
                                 qByCarDTO.setQuotes(quotelist.stream().map(q -> {
                                                                                   QuoteDTO qDTO = new QuoteDTO();
                                                                                   qDTO.map(q);
                                                                                   return qDTO;
                                                                                 }).collect(Collectors.toList()));
                                 quoteByCarDTOList.add(qByCarDTO);
                                });

            return quoteByCarDTOList;
        } catch (Exception e) {
            logger.error("Error loading  quotes by holder id"+ e.getMessage());
            return null;
        }
    }



    public List<Quote> findByHolderIdAndRegistrationMark(Long holderId, String registrationMark) {
        try {
            List<Quote> list = quoteRepository.findByHolderIdAndRegistrationMark(holderId, registrationMark);
            logger.debug("Quotes loaded correctly by holder id and registration mark, tot: "+ list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error loading  quotes by holder id and registration mark"+ e.getMessage());
            return null;
        }
    }


    public Quote save(QuoteForCreateDTO quoteForCreateDTO) throws QuoteException {

        Holder holder;
        if((holder = holderRepository.findById(quoteForCreateDTO.getHolderId())) == null)
            throw new QuoteException("Holder is not registered. In order to receive our quotes, please register to the site");

        if(!verifyRegistrationMarkPattern(quoteForCreateDTO.getRegistrationMark()))
            throw new QuoteException("Registration Mark is invalid");

        if(!verifyRegistrationMarkNumber(quoteForCreateDTO.getRegistrationMark()))
            throw new QuoteException("You cannot create more than two quotes for the same registration mark ");

        if(!verifyRegistrationDateCar(quoteForCreateDTO.getRegistrationDateCar()))
            throw new QuoteException("Registration date of the car is incorret ");

        Quote quote = new Quote();
        BeanUtils.copyProperties(quoteForCreateDTO,quote);

        quote.setHolder(holder);
        quote.setCost(calculateQuoteCost(quote.getWorth(),quote.getRegistrationDateCar(),quote.getPolicyType()));
        quote.setDate(new Date(System.currentTimeMillis()));
        quoteRepository.save(quote);

        quote.setQuoteNumber(quote.getHolder().getId()+"-"+quote.getRegistrationMark()+"-"+ quote.getId());

        quoteRepository.update(quote);

        logger.debug("Quote saved correctly");

        return quote;
    }


    /**
     * check if the plate(registrationMark) has the right pattern match and
     *  if there are less than two plates into the quotes, otherwise throws a QuoteException
     * @param registrationMark
     * @return
     * @throws QuoteException
     */
    private  Boolean verifyRegistrationMarkPattern(String registrationMark) throws QuoteException {
        Pattern pattern = Pattern.compile("[a-zA-Z1-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(registrationMark);

        if(!matcher.find())
            return false;
        else
            return true;
    }

    private  Boolean verifyRegistrationMarkNumber(String registrationMark) throws QuoteException {

        if(!quoteRepository.verifyRegistrationMarkNumber(registrationMark))
            return false;
        else
            return true;
    }

    private  Boolean verifyRegistrationDateCar(Date registrationDate) throws QuoteException {

        return true;
    }

    /**
     * some examples:
     * car registered 1/1/2020, worth 10000 RCA6 = 10000/1000 = 10
     * car registered 1/1/2020, worth 10000 RCA12 = 10000/1000 = 10*0.12 = 12
     * car registered 1/1/1990, worth 10000 RCA12 = 10000/750 * 0.12 = 12
     * @param worth
     * @param registrationDate
     * @param policyType
     * @return
     */
    public BigDecimal calculateQuoteCost(BigDecimal worth, Date registrationDate, PolicyType policyType){

        BigDecimal cost = new BigDecimal(0);

        if(registrationDate.after(STARTING_NEW_TARIFF_DATE.getTime())
                && worth.compareTo(new BigDecimal(WORTH_CAR_LIMIT))!=-1)
            cost = worth.divide(new BigDecimal(NEW_TARIFF), RoundingMode.UP);
        else
            cost = worth.divide(new BigDecimal(OLD_TARIFF), RoundingMode.UP);

        cost = cost.add(cost.multiply(policyType.getPercentage()))
                .setScale(2, RoundingMode.CEILING);

       return cost;
    }
}
