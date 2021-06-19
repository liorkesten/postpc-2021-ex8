package huji.postpc.exercises.ex8.ui;

import huji.postpc.exercises.ex8.data.Calculation;

@FunctionalInterface
public interface AdapterClickListener {

    public void onCalculationClick(Calculation calculation);
}
