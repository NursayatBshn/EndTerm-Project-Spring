package kz.nursayat.patterns;

import java.time.LocalDateTime;

public class LoggingService {
    private static LoggingService instance;

    private LoggingService() {}

    public static synchronized LoggingService getInstance() {
        if (instance == null) {
            instance = new LoggingService();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[APP-LOG] " + LocalDateTime.now() + " : " + message);
    }
}