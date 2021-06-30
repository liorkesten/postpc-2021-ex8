package huji.postpc.exercises.ex8;

import org.junit.Before;
import org.junit.Test;

import huji.postpc.exercises.ex8.data.Calculation;
import huji.postpc.exercises.ex8.data.CalculationStatus;
import huji.postpc.exercises.ex8.data.DataBase;
import huji.postpc.exercises.ex8.ui.CalculationHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ex8_unit_tests {
    DataBase db;

    @Before
    public void setup() {
        db = DataBase.getInstance();
        db.isTestMode = true;
    }

    @Test
    public void when_addingNewCalc_should_appear() {
        Calculation expected = new Calculation(100);
        db.addCalculation(expected);
        Calculation actual = db.getCalculationPr(expected.getId());
        assertEquals(expected.getNumber(), actual.getNumber());
    }

    @Test
    public void when_deletingNewCalc_shouldDeleted() {
        Calculation expected = new Calculation(100);
        db.addCalculation(expected);
        db.deleteCalculation(expected.getId());
        assertNull(db.getCalculationPr(expected.getId()));
    }


    @Test
    public void when_addingNewCalc_should_beInInProgressMode() {
        Calculation expected = new Calculation(100);
        db.addCalculation(expected);
        Calculation actual = db.getCalculationPr(expected.getId());
        assertEquals(CalculationStatus.IN_PROGRESS, actual.getStatus());
    }
}
