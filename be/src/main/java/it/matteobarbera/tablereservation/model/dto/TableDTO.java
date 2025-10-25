package it.matteobarbera.tablereservation.model.dto;

import it.matteobarbera.tablereservation.model.table.layout.Joinable;
import org.springframework.lang.Nullable;

public class TableDTO {

    private Integer number;
    private String category;

    @Nullable
    private Integer headCapacity;
    @Nullable
    private Integer joiningCapacity;

    protected TableDTO(){

    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Nullable
    public Integer getHeadCapacity() {
        return headCapacity;
    }

    public void setHeadCapacity(@Nullable Integer headCapacity) {
        this.headCapacity = headCapacity;
    }

    @Nullable
    public Integer getJoiningCapacity() {
        return joiningCapacity;
    }

    public void setJoiningCapacity(@Nullable Integer joiningCapacity) {
        this.joiningCapacity = joiningCapacity;
    }
}
