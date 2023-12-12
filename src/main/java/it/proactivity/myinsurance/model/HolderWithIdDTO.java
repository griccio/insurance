package it.proactivity.myinsurance.model;

import java.util.Date;


public class HolderWithIdDTO extends HolderDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HolderWithIdDTO{" +
                "id=" + id +
                '}' + super.toString();
    }
}
