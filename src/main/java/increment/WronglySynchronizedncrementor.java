package increment;

public class WronglySynchronizedncrementor implements Incrementor {

    private volatile Integer value = 0;

    @Override
    public int increment() {
        synchronized (value) {
            value++;
            return value;
        }
    }
}
