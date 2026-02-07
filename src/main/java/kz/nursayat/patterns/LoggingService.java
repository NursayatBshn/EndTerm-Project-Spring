package kz.nursayat.patterns;

import java.time.LocalDateTime;

/**
 * Singleton Pattern для сервиса логирования.
 * Реализует требование раздела 1.A: "Ensure a single shared instance"[cite: 11, 17].
 */
public class LoggingService {
    // Единственный статический экземпляр класса
    private static LoggingService instance;

    // Приватный конструктор предотвращает создание объекта через 'new'
    private LoggingService() {}

    // Глобальная точка доступа к экземпляру (ленивая инициализация)
    public static synchronized LoggingService getInstance() {
        if (instance == null) {
            instance = new LoggingService();
        }
        return instance;
    }

    /**
     * Метод для записи логов.
     * @param message Сообщение для логирования
     */
    public void log(String message) {
        System.out.println("[APP-LOG] " + LocalDateTime.now() + " : " + message);
    }
}