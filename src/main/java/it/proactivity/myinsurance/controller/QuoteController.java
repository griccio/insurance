package it.proactivity.myinsurance.controller;


import it.proactivity.myinsurance.exception.*;
import it.proactivity.myinsurance.model.dto.*;
import it.proactivity.myinsurance.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quote")
public class QuoteController extends MyInsuranceController{

    @Autowired
    QuoteService quoteService;

    @GetMapping("/")
    public ResponseEntity<List<QuoteDTO>> getAll() {
        return ResponseEntity.ok(quoteService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getById(@PathVariable Long id) {

        if (id == null || id <= 0)
            throw new InvalidParamException("The param id you have inserted is incorrect id:" + id);

        QuoteDTO quoteDTO = quoteService.findById(id);

        if (quoteDTO == null)
            throw new QuoteNotFoundException("Quote is not found  id:" + id);
        else
            return ResponseEntity.ok(quoteDTO);

    }


    @PostMapping
    public ResponseEntity<QuoteDTO> save(@Valid @RequestBody QuoteForCreateDTO quoteForCreateDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new InvalidQuoteException("The input object is incorrect: some required fields are missing or incorrect, quote was not saved");
        }

        QuoteDTO quoteDTO = quoteService.save(quoteForCreateDTO);

        if (quoteDTO == null)
            throw new InvalidQuoteException("Quote was not saved");
        else
            return ResponseEntity.ok(quoteDTO);

    }

    @PutMapping
    public ResponseEntity<QuoteWithOptionalExtraDTO> update(@Valid @RequestBody QuoteForUpdateDTO quoteForUpdateDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new InvalidQuoteException("The input object is incorrect: some required fields are missing or incorrect, quote was not saved");
        }

        QuoteWithOptionalExtraDTO quoteWithOptionalExtraDTO = quoteService.update(quoteForUpdateDTO);

        return ResponseEntity.ok(quoteWithOptionalExtraDTO);

    }


    @DeleteMapping
    public ResponseEntity<Long> delete(@Valid @RequestBody QuoteForDeleteDTO quoteForDeleteDTO,
                                       BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new InvalidQuoteException("The input object is incorrect: some required fields are missing or incorrect, quote was not saved");
        }

        if (!quoteService.verifyIfQuoteHasRegistrationMark(quoteForDeleteDTO.getId(),quoteForDeleteDTO.getRegistrationMark()))
            throw new InvalidQuoteException("Quote has not the registration mark. Cannot delete the quote with id = " + quoteForDeleteDTO.getId());

        return ResponseEntity.ok(quoteService.delete(quoteForDeleteDTO));

    }


}
