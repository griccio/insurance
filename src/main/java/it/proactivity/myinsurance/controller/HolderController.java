package it.proactivity.myinsurance.controller;

import freemarker.template.utility.NumberUtil;
import freemarker.template.utility.StringUtil;
import it.proactivity.myinsurance.exception.HolderControllerException;
import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.HolderDTO;
import it.proactivity.myinsurance.model.HolderWithIdDTO;
import it.proactivity.myinsurance.service.HolderService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/holder")
public class HolderController {

    @Autowired
    HolderService holderService;

    @GetMapping("/")
    public ResponseEntity<List<HolderDTO>> getAll() {

        List<Holder> holders = holderService.findAll();
        List<HolderDTO> holdersDTO = new ArrayList<>();

        if(holders == null){
            return ResponseEntity.ok(holdersDTO);
        }

         holdersDTO = holders.stream()
                .sorted(Comparator.comparing(Holder::getSurname))
                .collect(Collectors.mapping(
                        holder -> {
                            HolderDTO holderDTO = new HolderDTO();
                            BeanUtils.copyProperties(holder, holderDTO);
                            return holderDTO;
                        },
                        Collectors.toList()));

        return ResponseEntity.ok(holdersDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<HolderDTO> getById(@PathVariable Long id) {

        if(id == null || id == 0 )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Holder holder = holderService.findById(id);

        if(holder != null) {
            HolderDTO holderDTO = new HolderDTO();
            BeanUtils.copyProperties(holder, holderDTO);
            return ResponseEntity.ok(holderDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody HolderDTO holderDTO) {
        Holder holder = new Holder();
        BeanUtils.copyProperties(holderDTO, holder);
        Long id = holderService.save(holder);

        if(id != null && id >0)
            return ResponseEntity.ok(id);
        else
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
    }

    @PutMapping
    public ResponseEntity<Long> update(@Valid @RequestBody HolderWithIdDTO holderWithIdDTO) {

        Holder holder = new Holder();
        BeanUtils.copyProperties(holderWithIdDTO, holder);
        Long id = holderService.update(holder);

        if(id != null && id >0)
            return ResponseEntity.ok(id);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
    }


    @DeleteMapping
    public ResponseEntity<Long> delete(@PathVariable Long id) {
         Long deletedHolderId = holderService.delete(id);

         if(deletedHolderId!=null)
             return ResponseEntity.ok(id);
         else
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
    }

}
