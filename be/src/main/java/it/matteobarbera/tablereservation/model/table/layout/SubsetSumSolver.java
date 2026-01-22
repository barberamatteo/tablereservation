package it.matteobarbera.tablereservation.model.table.layout;

import it.matteobarbera.tablereservation.model.table.SimpleJoinableTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public final class SubsetSumSolver<TableType extends SimpleJoinableTable> {

    private static final Logger log = LoggerFactory.getLogger(SubsetSumSolver.class);
    private final List<TableType> pool;


    public SubsetSumSolver(List<TableType> pool) {
        this.pool = new ArrayList<>(pool);
        this.pool.sort(Comparator.comparingInt(TableType::getStandaloneCapacity));

    }

    public List<List<Integer>> getSubsetsOfCapacities(int target){
        Integer[] capacities = pool.stream().map(TableType::getStandaloneCapacity).toArray(Integer[]::new);
        var subsets = subsetSum(capacities, target);
        subsets.sort(Comparator.comparingInt(List::size));
        return subsets;

    }

    public static List<List<Integer>> subsetSum(Integer[] capacities, int target) {
        List<List<Integer>> subsetsOfCapacities = new ArrayList<>();
        backtrack(
                subsetsOfCapacities,
                new ArrayList<>(),
                capacities,
                target,
                0,
                0,
                2
        );
        return subsetsOfCapacities;
    }

    private static void backtrack(
            List<List<Integer>> subsetOfCapacities,
            List<Integer> temp,
            Integer[] capacities,
            int target,
            int start,
            int currentSum,
            int connectionPenalty
    ) {
        int effectiveSum = currentSum - connectionPenalty * Math.max(0, temp.size() - 1);

        if (effectiveSum == target) {
            subsetOfCapacities.add(new ArrayList<>(temp));
            return;
        }
        if (effectiveSum > target) {
            return;
        }

        for (int i = start; i < capacities.length; i++) {
            if (i > start && Objects.equals(capacities[i], capacities[i - 1])) continue;

            temp.add(capacities[i]);
            backtrack(
                    subsetOfCapacities,
                    temp,
                    capacities,
                    target,
                    i + 1,
                    currentSum + capacities[i],
                    connectionPenalty
            );
            temp.removeLast();
        }
    }


}
