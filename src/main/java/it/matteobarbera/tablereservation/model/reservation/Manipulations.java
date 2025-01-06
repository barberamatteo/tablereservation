package it.matteobarbera.tablereservation.model.reservation;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Manipulations extends ArrayList<Supplier<?>> {


    /*
     * TODO: Vedere se va con i supplier oppure riprogettare
     */
    public void runAll(){
        for (Supplier<?> supplier : this) {
            supplier.get();
        }
    }


}
