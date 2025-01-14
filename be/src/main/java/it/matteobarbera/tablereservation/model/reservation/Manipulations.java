package it.matteobarbera.tablereservation.model.reservation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Manipulations extends ArrayList<Function<?, ?>> {

    public void runAll(Set<Object> objects) {
        for (Function<?, ?> function : this)
            objects.add(function.apply(null));
    }


}
