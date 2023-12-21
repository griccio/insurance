package it.proactivity.myinsurance.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HolderForUpdateDTO extends HolderDTO{

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
