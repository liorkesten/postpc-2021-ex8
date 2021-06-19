package huji.postpc.exercises.ex8.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import huji.postpc.exercises.ex8.R;
import huji.postpc.exercises.ex8.data.Calculation;
import huji.postpc.exercises.ex8.data.CalculationStatus;
import huji.postpc.exercises.ex8.data.DataBase;
import huji.postpc.exercises.ex8.workers.CalculationWorker;

public class MainActivity extends AppCompatActivity {

    private DataBase _db;
    private EditText _editText;
    private ImageButton _addNewCalcButton;
    private WorkManager _workManager;
    CalculationAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _workManager = WorkManager.getInstance(this);
        _db = DataBase.getInstance();
        _adapter = new CalculationAdapter();
        _adapter.setNewCalculations(new ArrayList<>());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        _adapter.setAdapterClickListener(calculation -> {
            if (calculation.getStatus() == CalculationStatus.IN_PROGRESS) {
                //TODO cancel work
            }
            _db.deleteCalculation(calculation.getId());
            _adapter.setNewCalculations(_db.getAllCalculations());
        });

        _editText = findViewById(R.id.calculationEditText);
        _addNewCalcButton = findViewById(R.id.addNewCalculation);

        initEditTextSettings();
        _addNewCalcButton.setOnClickListener(v -> {
            Calculation newCalculation = new Calculation(Long.parseLong(_editText.getText().toString()));
            _db.addCalculation(newCalculation);
            _editText.setText(""); // Empty edit text
            updateAdapter();
            initNewWorker(newCalculation);
        });
    }

    private void updateAdapter() {
        List<Calculation> calculationList = _db.getAllCalculations();
        calculationList.sort((c1, c2) -> {
            if (c1.getStatus() != c2.getStatus()) {
                return (c1.getStatus() == CalculationStatus.IN_PROGRESS) ? -1 : 1;
            }
            return Long.compare(c1.getNumber(), c2.getNumber());
        });
        _adapter.setNewCalculations(calculationList);
    }

    private void initNewWorker(Calculation newCalculation) {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CalculationWorker.class)
                .setInputData(new Data.Builder()
                        .putString("calculation", new Gson().toJson(newCalculation))
                        .build())
                .build();
        _workManager.enqueue(request);
        UUID id = request.getId();
        LiveData<WorkInfo> workInfoByIdLiveData = _workManager.getWorkInfoByIdLiveData(id);
        workInfoByIdLiveData.observe(this, workInfo -> {
            Log.d("MainActivity", String.format("[New calculation] observing worklinfo status: %s", workInfo));
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                _db.editCalculation(new Gson().fromJson(workInfo.getOutputData().getString("calculation"), Calculation.class));
                _db.editProgressStatus(newCalculation.getId(), newCalculation.getNumber());
                updateAdapter();
            } else if (workInfo.getState() == WorkInfo.State.RUNNING) {
                long progress = workInfo.getProgress().getLong("progress", -1);
                if (progress != -1) {
                    _db.editProgressStatus(newCalculation.getId(), progress);
                }
            }
        });
    }


    private static class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }


    private void initEditTextSettings() {
        _editText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        _editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _addNewCalcButton.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }
}