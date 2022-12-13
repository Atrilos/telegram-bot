package pro.sky.telegrambot.exception;

public class DateInPastException extends RuntimeException {
    public DateInPastException() {
        super();
    }

    public DateInPastException(String message) {
        super(message);
    }
}
