package it.proactivity.myinsurance.controller;


import it.proactivity.myinsurance.model.*;
import it.proactivity.myinsurance.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    @Autowired
    QuoteService quoteService;

    @GetMapping("/")
    public ResponseEntity<List<QuoteDTO>> getAll() {

        List<Quote> quotes = quoteService.findAll();

        List<QuoteDTO> quotesDTO = new ArrayList<>();

        if (quotes == null) {
            return ResponseEntity.ok(quotesDTO);
        }

        quotesDTO = quotes.stream()
                .sorted(Comparator.comparing(Quote::getDate))
                .map(quote -> {
                    QuoteDTO quoteDTO = new QuoteDTO();
                    BeanUtils.copyProperties(quote, quoteDTO);
                    quoteDTO.setHolder(quote.getHolder());
                    return quoteDTO;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(quotesDTO);

    }

    @GetMapping("/holder/{id}")
    public ResponseEntity<List<QuoteByHolderAndCarDTO>> getByHolderId(@PathVariable Long id) {

        if (id == null || id <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        QuoteByHolderAndCarDTO quoteByCarDTO = new QuoteByHolderAndCarDTO(id);

        return ResponseEntity.ok(quoteService.findByHolderIdGroupByCar(quoteByCarDTO));

    }


    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getById(@PathVariable Long id) {

        if (id == null || id <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Quote quote = quoteService.findById(id);

        if (quote != null) {
            QuoteDTO quoteDTO = new QuoteDTO();
            BeanUtils.copyProperties(quote, quoteDTO);
            quoteDTO.setHolder( quote.getHolder());
            return ResponseEntity.ok(quoteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping
    public ResponseEntity<QuoteDTO> save(@Valid @RequestBody QuoteForCreateDTO quoteForCreateDTO) {

        try{
            Quote quote = new Quote();
            QuoteDTO quoteDTO = new QuoteDTO();
            quote = quoteService.save(quoteForCreateDTO);
            BeanUtils.copyProperties(quote,quoteDTO);
            quoteDTO.setHolder(quote.getHolder());
            return ResponseEntity.ok(quoteDTO);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
//
//    @PutMapping
//    public ResponseEntity<Long> update(@Valid @RequestBody HolderWithIdDTO holderWithIdDTO) {
//
//        if(holderService.verifyEmailExistenceForUpdate(holderWithIdDTO.getEmail(),holderWithIdDTO.getId()))
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);
//
//        if(holderService.verifyFiscalCodeExistenceForUpdate(holderWithIdDTO.getFiscalCode(),holderWithIdDTO.getId()))
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);
//
//        Quote holder = new Quote();
//        BeanUtils.copyProperties(holderWithIdDTO, holder);
//        Long id = holderService.update(holder);
//
//        if (id != null && id > 0)
//            return ResponseEntity.ok(id);
//        else
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Long> delete(@PathVariable Long id) {
//
//        if (id == null || id <= 0)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//
//        Long deletedHolderId = holderService.delete(id);
//
//        if (deletedHolderId != null)
//            return ResponseEntity.ok(id);
//        else
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
//    }

}
