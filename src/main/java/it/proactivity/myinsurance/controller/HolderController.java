package it.proactivity.myinsurance.controller;


import it.proactivity.myinsurance.exception.HolderNotFoundException;
import it.proactivity.myinsurance.exception.InvalidHolderException;
import it.proactivity.myinsurance.exception.InvalidParamException;
import it.proactivity.myinsurance.model.dto.HolderDTO;
import it.proactivity.myinsurance.model.dto.HolderForUpdateDTO;
import it.proactivity.myinsurance.model.dto.QuoteByHolderAndCarDTO;
import it.proactivity.myinsurance.service.HolderService;
import it.proactivity.myinsurance.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holder")
public class HolderController extends MyInsuranceController {

    @Autowired
    HolderService holderService;

    @Autowired
    QuoteService quoteService;

    @GetMapping("/")
    public ResponseEntity<List<HolderDTO>> getAll() {

        return ResponseEntity.ok(holderService.findAllDTO());

    }

    @GetMapping("/{id}")
    public ResponseEntity<HolderDTO> getById(@Valid @PathVariable Long id) {

        if (id == null || id <= 0)
            throw new InvalidParamException("The param id you have inserted is incorrect id:" + id);

        HolderDTO holderDTO = holderService.findById(id);

        if (holderDTO == null)
            throw new HolderNotFoundException("Holder is not found  id:" + id);
        else
         return ResponseEntity.ok(holderDTO);
    }


    @GetMapping("/{id}/quotes")
    public ResponseEntity<List<QuoteByHolderAndCarDTO>> getQuoteByHolderIdGroupByCar(@PathVariable Long id) {

        if (id == null || id <= 0)
            throw new InvalidParamException("The param id you have inserted is incorrect id:" + id);

        QuoteByHolderAndCarDTO quoteByCarDTO = new QuoteByHolderAndCarDTO(id);

        if (quoteByCarDTO == null)
            throw new HolderNotFoundException("Holder is not found  id:" + id);

        return ResponseEntity.ok(quoteService.findByHolderIdAndCar(quoteByCarDTO));

    }

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody HolderDTO holderDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new InvalidHolderException("The input object is incorrect: some required fields are missing or incorrect, Holder was not saved");
        }

        Long id = holderService.save(holderDTO);

        if (id == 0)
            throw new InvalidHolderException("Holder has not been saved");
        else
            return ResponseEntity.ok(id);
    }

    @PutMapping
    public ResponseEntity<Long> update(@Valid @RequestBody HolderForUpdateDTO holderForUpdateDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getAllErrors().toString());
            throw new InvalidHolderException("The input object is incorrect: some required fields are missing or incorrect. Holder has not been updated");
        }

        Long id = holderService.update(holderForUpdateDTO);

        if (id == 0)
            throw new InvalidHolderException("Holder has not been updated");
        else
            return ResponseEntity.ok(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {

        if (id == null || id <= 0)
            throw new InvalidHolderException("The input object is incorrect: some required fields are missing or incorrect. Holder has not been updated");

        Long deletedHolderId = holderService.delete(id);

        if (deletedHolderId != null)
            return ResponseEntity.ok(id);
        else
            throw new InvalidHolderException("Holder has not been updated");
    }

}
