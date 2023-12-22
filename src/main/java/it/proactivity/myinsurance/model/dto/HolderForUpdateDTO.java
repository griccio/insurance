package it.proactivity.myinsurance.model.dto;

import it.proactivity.myinsurance.model.Holder;
import it.proactivity.myinsurance.model.dto.HolderDTO;
import jakarta.validation.constraints.NotNull;

public class HolderForUpdateDTO extends HolderDTO {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HolderForUpdateDTO{" +
                "id=" + id +
                "} " + super.toString();
    }

    public void map(Holder holder){
        super.map(holder);
        this.id = holder.getId();

    }
}
