package logging;

public enum TimeUnit {
    Nano, Micro, Milli, Sec;

    public static double convert(long timeInNs, TimeUnit targetUnit) {
        switch (targetUnit) {
            case Nano: return timeInNs;
            case Micro: return timeInNs / 1_000.0;
            case Milli: return timeInNs / 1_000_000.0;
            case Sec: return timeInNs / 1_000_000_000.0;
            default: return timeInNs;
        }
    }
}
