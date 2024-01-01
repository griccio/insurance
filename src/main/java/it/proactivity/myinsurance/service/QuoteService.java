package it.proactivity.myinsurance.service;

import io.ebean.annotation.Transactional;
import it.proactivity.myinsurance.exception.InvalidParamException;
import it.proactivity.myinsurance.exception.InvalidQuoteException;
import it.proactivity.myinsurance.exception.QuoteException;
import it.proactivity.myinsurance.model.*;
import it.proactivity.myinsurance.model.dto.*;
import it.proactivity.myinsurance.repository.CarRepository;
import it.proactivity.myinsurance.repository.HolderRepository;
import it.proactivity.myinsurance.repository.OptionalExtraRepository;
import it.proactivity.myinsurance.repository.QuoteRepository;
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
    public List<QuoteDTO> findAll() {

        List<Quote> quotes = quoteRepository.findAll();
        logger.debug("Quotes loaded correctly, tot: " + quotes.size());
        List<QuoteDTO> quotesDTO = new ArrayList<>();

        if (quotes == null)
            return quotesDTO;
        else {
            quotesDTO = quotes.stream().sorted(Comparator.comparing(Quote::getDate))
                    .map(quote -> {
                        QuoteDTO quoteDTO = new QuoteDTO();
                        quoteDTO.map(quote);
                        return quoteDTO;
                    }).collect(Collectors.toList());
        }

        logger.debug(("quotes Found " + quotes.size()));
        return quotesDTO;
    }


    public QuoteDTO findById(Long id) {

            Quote quote = quoteRepository.findByIdWithHolderData(id);

            if(quote == null) {
                logger.debug("No quote Found with the id = " + id);
                return null;
            }
            else {
                logger.debug("Quote Found: " + quote.toString());
                QuoteDTO quoteDTO = new QuoteDTO();
                quoteDTO.map(quote);
                return quoteDTO;
            }

    }



    public Quote findByQuoteNumber(String quoteNumber) {

            Quote quote = quoteRepository.findByQuoteNumber(quoteNumber);

            if(quote != null)
                logger.debug("Quote Found: "+ quote.toString());
            else
                logger.debug("Quote is null ");
            return quote;

    }


    public List<Quote> findByRegistrationMark(String registrationMark) {

            List<Quote> list = quoteRepository.findByRegistrationMark(registrationMark);

            logger.debug("Quotes loaded correctly by registration mark, tot: "+ list.size());
            return list;
    }


    public List<Quote> findByHolderId(Long id) {

            List<Quote> list = quoteRepository.findByHolderId(id);
            logger.debug("Quotes loaded correctly by holder id, tot: "+ list.size());
            return list;

    }


    public List<QuoteByHolderAndCarDTO> findByHolderIdAndCar(QuoteByHolderAndCarDTO quoteByCarDTO) {

        if(quoteByCarDTO == null) {
            logger.error("QuoteByHolderAndCarDTO is null");
            return null;
        }

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

    }



    public List<Quote> findByHolderIdAndRegistrationMark(Long holderId, String registrationMark) {
            List<Quote> list = quoteRepository.findByHolderIdAndRegistrationMark(holderId, registrationMark);
            logger.debug("Quotes loaded correctly by holder id and registration mark, tot: "+ list.size());
            return list;

    }


    public QuoteDTO save(QuoteForCreateDTO quoteForCreateDTO)  {

        if(quoteForCreateDTO == null){
            logger.error("Input values are null");
            throw new InvalidQuoteException("Input values are null");
        }

        if(!quoteForCreateDTO.isCorrect()){
            logger.error("Input values are null or incorrect " + quoteForCreateDTO);
            throw new InvalidQuoteException("Input values are null or incorrect");
        }


        Holder holder;
        if((holder = holderRepository.findById(quoteForCreateDTO.getHolderId())) == null){
            logger.error("Holder is not registered. In order to receive our quotes, please register to the site");
            throw new InvalidQuoteException("Holder is not registered. In order to receive our quotes, please register to the site");
        }

        if(!verifyRegistrationMarkPattern(quoteForCreateDTO.getRegistrationMark())){
            logger.error("Registration Mark is invalid");
            throw new InvalidQuoteException("Registration Mark is invalid");
        }

        if(!verifyRegistrationMarkNumber(quoteForCreateDTO.getRegistrationMark())){
            logger.error("You cannot create more than two quotes for the same registration mark ");
            throw new InvalidQuoteException("You cannot create more than two quotes for the same registration mark ");
        }

        if(!verifyRegistrationDateCar(quoteForCreateDTO.getRegistrationDateCar())){
            logger.error("Registration date of the car is incorret ");
            throw new InvalidQuoteException("Registration date of the car is incorret ");
        }
        //create a new car or get the one from DB
        Car car = getTheCar(holder,quoteForCreateDTO.getRegistrationMark(),
                quoteForCreateDTO.getRegistrationDateCar(),
                quoteForCreateDTO.getWorth());

        if(car == null){
            logger.error(" the car belongs to another holder " + quoteForCreateDTO.toString());
            throw new InvalidQuoteException("the car belongs to another holder ");
        }

        //create a new quote with holder and car
        Quote quote = getTheQuote(holder, car, quoteForCreateDTO.getPolicyType());

        quoteRepository.save(quote);
        QuoteDTO quoteDTO = new QuoteDTO();
        quoteDTO.map(quote);
        logger.debug("Quote saved correctly"+ quoteDTO.toString());

        return quoteDTO;
    }

    public QuoteWithOptionalExtraDTO update(QuoteForUpdateDTO quoteForUpdateDTO)  {

        if(quoteForUpdateDTO == null){
            logger.debug("input param is null ");
            throw new InvalidQuoteException("input param is null ");
        }

        if(!quoteForUpdateDTO.isCorrect()){
            logger.debug("input param is null ");
            throw new InvalidQuoteException("input param is null ");
        }

        Quote quote = quoteRepository.findById(quoteForUpdateDTO.getId());

        if(quote == null) {
            logger.debug("cannot find Quote with the id: " + quoteForUpdateDTO.getId());
            throw new InvalidQuoteException("cannot find Quote with the id: " + quoteForUpdateDTO.getId());
        }

        //remove previous extra
        quote.getOptionalExtras().clear();

        //get the Optional extras
        for(String code :  quoteForUpdateDTO.getOptionalExtraByCodeList() ){
            OptionalExtra optionalExtra = optionalExtraRepository.findByCode(code);

            if(optionalExtra == null) {
                logger.debug("Cannot find Optional extra with code " + code);
                throw new InvalidQuoteException("cannot find Optional extra with code " + code);
            }
            //A KASKO includes all the Optional Extras for this reason I clean up the
            //list of optional Extra and  leave only the KASKO
            if(optionalExtra.getCode().equalsIgnoreCase(MyInsuranceConstants.KASKO_CODE)) {
                logger.debug("this is a kasko optional Extra" + code);
                quote.addKaskoOptionalExtra(optionalExtraRepository.findByCode(MyInsuranceConstants.KASKO_CODE));
                break;
            }

            quote.addOptionalExtra(optionalExtra);
            logger.debug("Added correctly new  optional extra : " + code );
        }

        //recalculate the cost
        quote.setCost(calculateCost(quote.getCar().getWorth(),
                quote.getCar().getRegistrationDate(),
                quote.getPolicyType(),
                quote.getOptionalExtras().size(),
                quote.hasKasko()));

        quoteRepository.update(quote);

        logger.debug("Quote updated correctly" + quote.toString());
        QuoteWithOptionalExtraDTO quoteWithOptionalExtraDTO = new QuoteWithOptionalExtraDTO();
        quoteWithOptionalExtraDTO.map(quote);
        return quoteWithOptionalExtraDTO;
    }




    public Long delete(QuoteForDeleteDTO quoteForDeleteDTO)  {

        if(quoteForDeleteDTO == null){
            logger.error("input param is null");
            throw new InvalidQuoteException("input param is null");
        }

        if(!quoteForDeleteDTO.isCorrect()){
            logger.error("input param is null or incorrect" + quoteForDeleteDTO);
            throw new InvalidQuoteException("input param is null or incorrect");
        }


        Quote quote;

        if((quote = quoteRepository.findById(quoteForDeleteDTO.getId())) == null){
            logger.error("Quote is not registered.");
            throw new InvalidQuoteException("Quote is not registered");
        }


        if(!verifyIfQuoteHasRegistrationMark(quoteForDeleteDTO.getId(),quoteForDeleteDTO.getRegistrationMark())){
            logger.error("Quote has not this Registration Mark or the registration Mark is assigned to another quote" + quoteForDeleteDTO);
            throw new InvalidQuoteException("Quote has not this Registration Mark or the registration Mark is assigned to another quote");
        }

        quoteRepository.delete(quote);
        logger.error("Quote has been deleted correctly ");
        return quote.getId();
    }


    private BigDecimal calculateCost(BigDecimal worth, Date registrationDate, PolicyType policyType, Integer totOptionalExtra, Boolean isKasko){
        //recalculate the base costs
         BigDecimal cost = calculateQuoteCost(worth,registrationDate,policyType);

        //add the optional extras
        if(isKasko)
            cost = cost.add(addKaskoCosts(worth, registrationDate).setScale(2, RoundingMode.CEILING));
        else
            cost = cost.add(addOptionalExtraCosts(worth,registrationDate,totOptionalExtra).setScale(2, RoundingMode.CEILING));

        return cost;
    }

    private Quote getTheQuote(Holder holder, Car car, PolicyType policyType){
        Quote quote = new Quote();
        quote.setPolicyType(policyType);
        quote.setHolder(holder);
        quote.setCar(car);
        quote.setCost(calculateQuoteCost(quote.getCar().getWorth(),quote.getCar().getRegistrationDate(),quote.getPolicyType()));
        quote.setDate(new Date(System.currentTimeMillis()));
        quote.setQuoteNumber(quote.getHolder().getId() + "-"
                + quote.getCar().getRegistrationMark() + "-"
                + quoteRepository.getNextValidIndexByHolder(quote.getHolder().getId()));

        return quote;
    }


    /**
     *  if the car doesn't exist and doesn't own by another holder
     *  I create a new car from the input data and
     *  assign  the car to the holder
     *  if the car exists, but doesn't belong to the holder
     *  I return null
     * @param holder
     * @param registrationMark
     * @param registrationDate
     * @param worth
     * @return
     */
    private Car getTheCar(Holder holder, String registrationMark,Date registrationDate, BigDecimal worth){

        Car car = new Car(holder,registrationMark,
                registrationDate,
                worth);
        //if the car exists, I check if belong to the holder
        //if not, I return null
        if (carRepository.existCar(registrationMark)){
            if(holderHasThisCar(holder, registrationMark))
                car = carRepository.findByRegistrationMark(registrationMark);
            else
                return null;
        }
        //If the car doesn't exist I create a new one
        else
            car = carRepository.save(car);

        return car;
    }

    private  Boolean holderHasThisCar( Holder holder, String registrationMark)  {

        if (carRepository.existCar(registrationMark)) {
            if(getHolderIdOwnTheCar(registrationMark) == holder.getId()){
                return true;
            }
        }
        return false;
    }

    private  Long getHolderIdOwnTheCar(String registrationMark)  {

        if (carRepository.existCar(registrationMark))
            return carRepository.findByRegistrationMark(registrationMark).getHolder().getId();
        else return null;
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

    private  Boolean verifyRegistrationDateCar(Date registrationDate)  {

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
            kaskoCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_AFTER_2020).multiply(new BigDecimal(optionalExtraRepository.getTotOptionalExtrasIntoKasko())).multiply(MyInsuranceConstants.KASKO_DISCOUNT);
        else
            kaskoCost = new BigDecimal(MyInsuranceConstants.EXTRA_COST_BEFORE_2020)
                    .multiply(new BigDecimal(optionalExtraRepository.getTotOptionalExtrasIntoKasko())).multiply(MyInsuranceConstants.KASKO_DISCOUNT);

        return kaskoCost;
    }
}
