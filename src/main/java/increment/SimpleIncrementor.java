package increment;

public class SimpleIncrementor implements Incrementor {

    private int value = 0;

    @Override
    public int increment() {
        value++;
        return value;
    }
}
