package it.proactivity.myinsurance.service;

import io.ebean.annotation.Transactional;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.*;
import it.proactivity.myinsurance.repository.CarRepository;
import it.proactivity.myinsurance.repository.HolderRepository;
import it.proactivity.myinsurance.repository.OptionalExtraRepository;
import it.proactivity.myinsurance.repository.QuoteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QuoteService extends MyInsuranceService{
    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    HolderRepository holderRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    OptionalExtraRepository optionalExtraRepository;

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

    public List<QuoteByHolderAndCarDTO> findByHolderIdAndCar(QuoteByHolderAndCarDTO quoteByCarDTO) {

        try {
            List<QuoteByHolderAndCarDTO> quoteByCarDTOList = new ArrayList<>();
            // get the list of the car
            List<String> list = holderRepository.getRegistrationMarks(quoteByCarDTO.getHolder_id());
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


    public QuoteDTO save(QuoteForCreateDTO quoteForCreateDTO) throws QuoteException {

        Holder holder;
        if((holder = holderRepository.findById(quoteForCreateDTO.getHolderId())) == null){
            logger.error("Holder is not registered. In order to receive our quotes, please register to the site");
            throw new QuoteException("Holder is not registered. In order to receive our quotes, please register to the site");
        }


        if(!verifyRegistrationMarkPattern(quoteForCreateDTO.getRegistrationMark())){
            logger.error("Registration Mark is invalid");
            throw new QuoteException("Registration Mark is invalid");
        }


        if(!verifyRegistrationMarkNumber(quoteForCreateDTO.getRegistrationMark())){
            logger.error("You cannot create more than two quotes for the same registration mark ");
            throw new QuoteException("You cannot create more than two quotes for the same registration mark ");
        }


        if(!verifyRegistrationDateCar(quoteForCreateDTO.getRegistrationDateCar())){
            logger.error("Registration date of the car is incorret ");
            throw new QuoteException("Registration date of the car is incorret ");
        }

        Quote quote = new Quote();
        BeanUtils.copyProperties(quoteForCreateDTO,quote);

        quote.setHolder(holder);
        //add the car to the holder
        Car car = new Car(holder,quoteForCreateDTO.getRegistrationMark(),quoteForCreateDTO.getRegistrationDateCar(),quoteForCreateDTO.getWorth());
        car = carRepository.save(car);
        //add the car to the quote
        quote.setCar(car);
        quote.setCost(calculateQuoteCost(quote.getCar().getWorth(),quote.getCar().getRegistrationDate(),quote.getPolicyType()));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote.setQuoteNumber(quote.getHolder().getId() + "-"
               + quote.getCar().getRegistrationMark() + "-"
               + quoteRepository.getNextValidIndexByHolder(quote.getHolder().getId()));

        quoteRepository.save(quote);
        QuoteDTO quoteDTO = new QuoteDTO();
        quoteDTO.map(quote);
        logger.debug("Quote saved correctly"+ quoteDTO.toString());

        return quoteDTO;
    }



    public QuoteWithOptionalExtraDTO update(QuoteForUpdateDTO quoteForUpdateDTO) throws QuoteException {

        Quote quote = quoteRepository.findById(quoteForUpdateDTO.getId());

        if(quote == null) {
            logger.debug("cannot find Quote with the id: " + quoteForUpdateDTO.getId());
            throw new QuoteException("cannot find Quote with the id: " + quoteForUpdateDTO.getId());
        }

        //remove previous extra
        quote.getOptionalExtras().clear();

        //get the Optional extras
        for(String code :  quoteForUpdateDTO.getOptionalExtraByCodeList() ){
            OptionalExtra optionalExtra = optionalExtraRepository.findByCode(code);

            if(optionalExtra == null) {
                logger.debug("Cannot find Optional extra with code " + code);
                throw new QuoteException("cannot find Optional extra with code " + code);
            }
            //A KASKO include all the Optional Extras for this reason I clean up the
            //list of optional Extra and  leave only the KASKO
            if(optionalExtra.getCode().equalsIgnoreCase(MyInsuranceConstants.KASKO_CODE)) {
                logger.debug("this is a kasko optional Extra" + code);
                quote.addKaskoOptionalExtra(optionalExtraRepository.findByCode(MyInsuranceConstants.KASKO_CODE));
                break;
            }

            quote.addOptionalExtra(optionalExtraRepository.findByCode(code));
            logger.debug("Added correctly new  optional extra : " + code );
        }

        //recalculate the costs with extras
        quote.setCost(calculateQuoteCost(quote.getCar().getWorth(),quote.getCar().getRegistrationDate(),quote.getPolicyType()));

        if(quote.hasKasko())
            quote.setCost(quote.getCost().add(addKaskoCosts(quote.getCar().getWorth(),quote.getCar().getRegistrationDate())).setScale(2, RoundingMode.CEILING));
        else
            quote.setCost(quote.getCost().add(addOptionalExtraCosts(quote.getCar().getWorth(),quote.getCar().getRegistrationDate(),quote.getOptionalExtras().size())).setScale(2, RoundingMode.CEILING));

        quoteRepository.update(quote);

        logger.debug("Quote updated correctly");
        QuoteWithOptionalExtraDTO quoteWithOptionalExtraDTO = new QuoteWithOptionalExtraDTO();
        quoteWithOptionalExtraDTO.map(quote);
        return quoteWithOptionalExtraDTO;
    }




    public Long delete(QuoteForDeleteDTO quoteForDeleteDTO) throws QuoteException {

        Quote quote;
        if((quote = quoteRepository.findById(quoteForDeleteDTO.getId())) == null){
            logger.error("Quote is not registered.");
            throw new QuoteException("Quote is not registered");
        }


        if(!verifyIfQuoteHasRegistrationMark(quoteForDeleteDTO.getId(),quoteForDeleteDTO.getRegistrationMark())){
            logger.error("Quote has not this Registration Mark ");
            throw new QuoteException("Quote has not this Registration Mark");
        }

        quoteRepository.delete(quote);
        logger.error("Quote has been deleted correctly ");
        return quote.getId();
    }





    /**
     * check if the plate(registrationMark) has the right pattern match and
     *  if there are less than two plates into the quotes, otherwise throws a QuoteException
     * @param registrationMark
     * @return
     * @throws QuoteException
     */
    private  Boolean verifyRegistrationMarkPattern(String registrationMark)  {

        return Boolean.valueOf(Pattern.matches("[a-zA-Z0-9]{5,10}",registrationMark));

    }

    private  Boolean verifyRegistrationMarkNumber(String registrationMark)  {

        if(!quoteRepository.verifyRegistrationMarkNumber(registrationMark))
            return false;
        else
            return true;
    }

    private  Boolean verifyRegistrationDateCar(Date registrationDate) throws QuoteException {

        if(registrationDate.after(new Date(System.currentTimeMillis())))
            return false;

        if(registrationDate.before(new GregorianCalendar(1950,Calendar.JANUARY,1).getTime()))
             return false;

        return true;
    }

    public  Boolean verifyIfQuoteHasRegistrationMark(Long quoteId, String registrationMark)  {

       return quoteRepository.verifyRegistrationMarkNumberExist(quoteId,registrationMark);

    }

    /**
     * some examples:
     * car registered 1/1/2020, worth 10000 RCA6 = 10000/1000 = 10
     * car registered 1/1/2020, worth 10000 RCA12 = 10000/1000 = 10 + 10*0.12 = 12
     * car registered 1/1/1990, worth 10000 RCA12 = 10000/750 + (10000/750 * 0.12) = 14.93
     * @param worth
     * @param registrationDate
     * @param policyType
     * @return
     */
    public BigDecimal calculateQuoteCost(BigDecimal worth, Date registrationDate, PolicyType policyType){

        BigDecimal cost = new BigDecimal(0);

        if(registrationDate.after(MyInsuranceConstants.STARTING_NEW_TARIFF_DATE.getTime())
                && worth.compareTo(new BigDecimal(MyInsuranceConstants.WORTH_CAR_LIMIT))!=-1)
            cost = worth.divide(new BigDecimal(MyInsuranceConstants.NEW_TARIFF), RoundingMode.UP);
        else
            cost = worth.divide(new BigDecimal(MyInsuranceConstants.OLD_TARIFF), RoundingMode.UP);

         cost = cost.add(cost.multiply(policyType.getPercentage()))
                .setScale(2, RoundingMode.CEILING);

       return cost;
    }

    public BigDecimal addOptionalExtraCosts(BigDecimal worth, Date registrationDate, int optionalExtraTot){

        BigDecimal  extraCost = new BigDecimal(0);

             if(registrationDate.after(MyInsuranceConstants.STARTING_NEW_TARIFF_DATE.getTime())
                     && worth.compareTo(new BigDecimal(MyInsuranceConstants.WORTH_CAR_LIMIT))!=-1)
                 extraCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_AFTER_2020).multiply(new BigDecimal(optionalExtraTot));
             else
                 extraCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_BEFORE_2020).multiply(new BigDecimal(optionalExtraTot));

        return extraCost;
    }

    public BigDecimal addKaskoCosts(BigDecimal worth, Date registrationDate){

        BigDecimal  kaskoCost = new BigDecimal(0);

        if(registrationDate.after(MyInsuranceConstants.STARTING_NEW_TARIFF_DATE.getTime())
                && worth.compareTo(new BigDecimal(MyInsuranceConstants.WORTH_CAR_LIMIT))!=-1)
            kaskoCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_AFTER_2020).multiply(new BigDecimal(optionalExtraRepository.getTotOptionalExtrasIntoKasko()));
        else
            kaskoCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_BEFORE_2020)
                    .multiply(new BigDecimal(optionalExtraRepository.getTotOptionalExtrasIntoKasko())).multiply(MyInsuranceConstants.KASKO_DISCOUNT);

        return kaskoCost;
    }
}
