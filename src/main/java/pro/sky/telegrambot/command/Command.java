package pro.sky.telegrambot.command;

import java.time.LocalDateTime;

public interface Command {

    String getMessage();

    default LocalDateTime getLocalDateTime() {
        return null;
    }
}
