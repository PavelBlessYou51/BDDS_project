import java.util.logging.*;

public class MyLogger {
    public static final Logger logger = Logger.getLogger(MyLogger.class.getName());

    static {
        setupLogger();
    }

    private static void setupLogger() {
        try {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);

            SimpleFormatter formatter = new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new java.util.Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            };

            consoleHandler.setFormatter(formatter);
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);

        } catch (Exception e) {
            System.err.println("Ошибка настройки логгера: " + e.getMessage());
        }
    }
}
