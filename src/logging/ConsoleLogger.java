package logging;

public class ConsoleLogger implements ILogger {

    @Override
    public void write(long value) {
        System.out.println(value);
    }

    @Override
    public void write(String value) {
        System.out.println(value);
    }

    @Override
    public void write(Object... values) {
        for (Object value : values) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    @Override
    public void writeTime(String label, long timeInNs, TimeUnit unit) {
        double converted = TimeUnit.convert(timeInNs, unit);
        System.out.println(label + " " + converted + " " + unit);
    }

    @Override
    public void close() {
    }
}
