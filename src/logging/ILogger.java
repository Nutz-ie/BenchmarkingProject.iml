package logging;

public interface ILogger {
    void write(long value);
    void write(String value);
    void write(Object... values);
    void writeTime(String label, long timeInNs, TimeUnit unit);
    void close();
}
