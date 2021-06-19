package huji.postpc.exercises.ex8.workers;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import huji.postpc.exercises.ex8.data.Calculation;
import huji.postpc.exercises.ex8.data.CalculationStatus;
import huji.postpc.exercises.ex8.data.DataBase;

import static java.lang.Math.sqrt;

public class CalculationWorker extends Worker {
    public CalculationWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        String serializedCalculation = getInputData().getString("calculation");
        if (serializedCalculation == null || serializedCalculation.equals("")) {
            Log.e("Worker", "calculation is empty ?!?!");
            return Result.failure();
        }
        Calculation calculation = new Gson().fromJson(serializedCalculation, Calculation.class);
        long target = calculation.getNumber();
        if (target == -1) {
            Log.e("Worker", "No target was inserted");
            return Result.failure();
        }
        if (target < 0) {
            Log.e("Worker", String.format("The target %d is invalid. target must be >= 0", target));
            return Result.failure();
        }

        long root1 = getInputData().getLong("root1", 2);
        long root2 = root1 - 1;

        while (root1 <= calculation.getNumber() - 1) {
            setProgressAsync(new Data.Builder()
                    .putLong("progress", root1)
                    .build());
            while (root2 <= calculation.getNumber() - 1) {
                if (root1 * root2 == target) {
                    break;
                }
                root2 += 1;
            }
            if (root1 * root2 == target) {
                break;
            }
            root1 += 1;
            root2 = root1;
        }
        // Not prime number
        if (root1 * root2 == target) {
            calculation.rootsFound(root1, root2);
        } else {
            // Prime number
            calculation.markAsPrime();
        }
        return Result.success(new Data.Builder()
                .putString("calculation", new Gson().toJson(calculation))
                .build());
    }
}