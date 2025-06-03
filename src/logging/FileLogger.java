package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILogger {
    private BufferedWriter writer;

    public FileLogger(String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename, true)); // append mode
    }

    @Override
    public void write(long value) {
        try {
            writer.write(Long.toString(value));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String value) {
        try {
            writer.write(value);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Object... values) {
        try {
            for (Object val : values) {
                writer.write(val.toString() + " ");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void writeTime(String label, long timeInNs, TimeUnit unit) {
        double converted = TimeUnit.convert(timeInNs, unit);
        try {
            writer.write(label + " " + converted + " " + unit + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

