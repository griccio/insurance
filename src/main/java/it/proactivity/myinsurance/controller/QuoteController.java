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
            return ResponseEntity.ok(quoteService.save(quoteForCreateDTO));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping
    public ResponseEntity<QuoteWithOptionalExtraDTO> update(@Valid @RequestBody QuoteForUpdateDTO quoteForUpdateDTO) {
        try{
            return ResponseEntity.ok(quoteService.update(quoteForUpdateDTO));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping
    public ResponseEntity<Long> delete(@Valid @RequestBody QuoteForDeleteDTO quoteForDeleteDTO){

        if (quoteForDeleteDTO.getId() == null || quoteForDeleteDTO.getId() <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);

        if (!quoteService.verifyIfQuoteHasRegistrationMark(quoteForDeleteDTO.getId(),quoteForDeleteDTO.getRegistrationMark()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);
        try {
            return ResponseEntity.ok(quoteService.delete(quoteForDeleteDTO));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0L);
        }
    }


}
