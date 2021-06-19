package huji.postpc.exercises.ex8.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import huji.postpc.exercises.ex8.R;
import huji.postpc.exercises.ex8.data.Calculation;

public class CalculationHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final ImageView cancelButton;
    private final ImageView deleteButton;
    private final ProgressBar progressBar;

    public TextView getTextView() {
        return textView;
    }

    public ImageView getCancelButton() {
        return cancelButton;
    }

    public ImageView getDeleteButton() {
        return deleteButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public CalculationHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.oneItemCalculationTextView);
        cancelButton = itemView.findViewById(R.id.oneItemCancelButton);
        deleteButton = itemView.findViewById(R.id.oneItemDeleteButton);
        progressBar = itemView.findViewById(R.id.oneItemProgressBar);

        cancelButton.setOnClickListener(v -> {
            //TODO Implement
            Log.d("CalculationHolder", "CancelButton was clicked.");
        });

        deleteButton.setOnClickListener(v -> {
            //TODO Implement
            Log.d("CalculationHolder", "DeleteButton was clicked.");
        });
    }

    public void inProgressMode(Calculation calculation) {
        textView.setText(String.format("Calculating roots for number: %d", calculation.getNumber()));
        deleteButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    public void inFinishedMode(Calculation calculation) {
        cancelButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        if (!calculation.isPrime()) {
            textView.setText(String.format("Roots of number: %d are : root1: %d, root2: %d", calculation.getNumber(), calculation.getRoot1(), calculation.getRoot2()));
        } else {
            textView.setText(String.format("Number %d is prime number", calculation.getNumber()));
        }
        progressBar.setProgress(progressBar.getMax());
    }

    public void updateProgressBar(int value) {
        progressBar.setProgress(value);
    }
    public void setMaxOfProgressBar(int max) {
        this.progressBar.setMax(max);
    }
}
