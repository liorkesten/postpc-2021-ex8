package huji.postpc.exercises.ex8.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static DataBase singleton;
    private FirebaseFirestore fireStore;
    private final Map<String, Calculation> calculations = new HashMap<>();
    private final MutableLiveData<List<Calculation>> mutableLiveData = new MutableLiveData<>();
    private final Map<String, MutableLiveData<Integer>> calculationIdToProgress = new HashMap<>();

    public static DataBase getInstance() {
        if (singleton == null) {
            singleton = new DataBase();
        }
        return singleton;
    }

    private DataBase() {
        mutableLiveData.setValue(new ArrayList<>());
//        fireStore = FirebaseFirestore.getInstance();
        // Add listener!
//        fireStore.collection("orders").addSnapshotListener((value, err) -> {
//            if (err != null) {
//                Log.e(DataBase.class.toString(), "Fail to extacrt data base. err: " + err);
//            } else if (value == null) {
//                //TODO Delete everything
//            } else {
//                List<DocumentSnapshot> documentSnapshots = value.getDocuments();
//                calculations.clear();
//                documentSnapshots.forEach(doc -> calculations.put(doc.getId(), doc.toObject(Calculation.class)));
//                mutableLiveData.setValue(new ArrayList<>(calculations.values()));
//                Log.d(DataBase.class.toString(), "All orders extracted successfully");
//            }
//        });
    }

    public LiveData<List<Calculation>> getLiveDataCalculations() {
        return mutableLiveData;
    }

    public void deleteCalculation(String calculationID) {
        if (!calculations.containsKey(calculationID)) {
            Log.d(DataBase.class.toString(), String.format("Order: %s is not exist", calculationID));
        }
        Calculation deleted = calculations.remove(calculationID);
        calculationIdToProgress.remove(calculationID);
        Log.d("Database", String.format("Calculation id %s (number : %d) was deleted from db", deleted.getId(), deleted.getNumber()));
//        fireStore.collection("orders").document(calculationID).delete();
    }

    public void addCalculation(Calculation calculation) {
        if (calculation == null) {
            Log.d(DataBase.class.toString(), "Can't add null order");
        }
        calculations.put(calculation.getId(), calculation);
        calculationIdToProgress.put(calculation.getId(), new MutableLiveData<>(0));
        Log.d(DataBase.class.toString(), String.format("Calculation (id:%s,number:%d) was added successfully", calculation.getId(), calculation.getNumber()));
    }

    public Calculation getCalculationPr(String calulationId) {
        if (!calculations.containsKey(calulationId)) {
            Log.d(DataBase.class.toString(), "Can't add null order");
            return null;
        }
        return calculations.get(calulationId);
    }

    public LiveData<Integer> getLiveDataOfSomeProgress(String calculcationId) {
        if (!calculationIdToProgress.containsKey(calculcationId)) {
            Log.e(DataBase.class.toString(), "Can't get livedata of order that is not exist");
            return null;
        }
        return calculationIdToProgress.get(calculcationId);
    }

    public void editProgressStatus(String calculationID, long progress) {
        if (!calculationIdToProgress.containsKey(calculationID)) {
            Log.e(DataBase.class.toString(), "Edited calculation is null?!");
            return;
        }
        calculationIdToProgress.get(calculationID).setValue((int) progress);
    }

    public void editCalculation(Calculation editedCalculation) {
        if (editedCalculation == null) {
            Log.e(DataBase.class.toString(), "Edited calculation is null?!");
            return;
        }
        if (!calculations.containsKey(editedCalculation.getId())) {
            Log.e(DataBase.class.toString(), "Edited calculation is not in the database??!");
            return;
        }
        calculations.put(editedCalculation.getId(), editedCalculation);
        Log.d(DataBase.class.toString(), String.format("Calculation (%s) edited successfully", editedCalculation.getNumber()));
    }

    public List<Calculation> getAllCalculations() {
        return new ArrayList<>(calculations.values());
    }
}