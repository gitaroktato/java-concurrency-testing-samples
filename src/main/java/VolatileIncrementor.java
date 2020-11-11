public class VolatileIncrementor implements Incrementor {

    private volatile int value = 0;

    @Override
    public int increment() {
        value++;
        return value;
    }
}
