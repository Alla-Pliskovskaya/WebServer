package threadDispatcher;

public class Counter extends ThreadedTask {

    @Override
    public void start()
    {
        int a = 0;
        do {
            a = (int) (a + Math.cos(Math.PI));

        } while (a >= -100000000);
    }
}
