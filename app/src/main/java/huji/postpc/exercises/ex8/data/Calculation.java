package huji.postpc.exercises.ex8.data;

import java.util.UUID;

public class Calculation {
    private final String id;
    private final long number;
    private boolean isPrime;
    private long root1;
    private long root2;
    private CalculationStatus status;

    public Calculation(long number) {
        this.id = UUID.randomUUID().toString();
        this.number = number;
        this.isPrime = false;
        this.status = CalculationStatus.IN_PROGRESS;
    }

    public long getNumber() {
        return number;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public void markAsPrime() {
        isPrime = true;
        setStatus(CalculationStatus.FINISHED);
    }

    public long getRoot1() {
        return root1;
    }

    public void setRoot1(long root1) {
        this.root1 = root1;
    }

    public long getRoot2() {
        return root2;
    }

    public void setRoot2(long root2) {
        this.root2 = root2;
    }

    public CalculationStatus getStatus() {
        return status;
    }

    public void setStatus(CalculationStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void rootsFound(long root1, long root2) {
        setStatus(CalculationStatus.FINISHED);
        isPrime = false;
        setRoot1(root1);
        setRoot2(root2);
    }
}
