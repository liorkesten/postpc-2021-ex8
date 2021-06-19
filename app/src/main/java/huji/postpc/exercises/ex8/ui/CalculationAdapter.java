package huji.postpc.exercises.ex8.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huji.postpc.exercises.ex8.R;
import huji.postpc.exercises.ex8.data.Calculation;
import huji.postpc.exercises.ex8.data.CalculationStatus;
import huji.postpc.exercises.ex8.data.DataBase;

public class CalculationAdapter extends RecyclerView.Adapter<CalculationHolder> {
    private final List<Calculation> calculations = new ArrayList<>();
    private AdapterClickListener adapterClickListener;

    @Override
    public CalculationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_one_calculation, parent, false);
        return new CalculationHolder(view);
    }

    @Override
    public void onBindViewHolder(CalculationHolder holder, int position) {
        if (position < 0 || position >= calculations.size()) {
            Log.e("Adapter", "position is invalid- size of array is: " + calculations.size() + " position is " + position);
            return;
        }
        Calculation calculation = calculations.get(position);
        holder.setMaxOfProgressBar((int)calculation.getNumber());
        holder.getCancelButton().setOnClickListener(v -> {
            Log.d("Adapter", "Canclel button was clicked.");
            if (adapterClickListener != null) {
                adapterClickListener.onCalculationClick(calculation);
            } else {
                Log.d("Adapter", "Adapter listener wasn't initialized yet");
            }
        });
        holder.getDeleteButton().setOnClickListener(v -> {
            Log.d("Adapter", "Delete button was clicked.");
            if (adapterClickListener != null) {
                adapterClickListener.onCalculationClick(calculation);
            } else {
                Log.d("Adapter", "Adapter listener wasn't initialized yet");
            }
        });
        if (calculation.getStatus() == CalculationStatus.IN_PROGRESS) {
            holder.inProgressMode(calculation);
        } else {
            holder.inFinishedMode(calculation);
        }
        DataBase.getInstance().getLiveDataOfSomeProgress(calculation.getId()).observeForever(holder::updateProgressBar);
    }

    @Override
    public int getItemCount() {
        return calculations.size();
    }

    /**
     * Called by acticity when new calculations are received.
     *
     * @param calculations
     */
    public void setNewCalculations(List<Calculation> calculations) {
        this.calculations.clear();
        this.calculations.addAll(calculations);
        notifyDataSetChanged();
    }

    public void setAdapterClickListener(AdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }
}
