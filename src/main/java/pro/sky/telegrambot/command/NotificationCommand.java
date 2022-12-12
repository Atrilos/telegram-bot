package pro.sky.telegrambot.command;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class NotificationCommand implements Command {
    private final LocalDateTime dateTime;

    @Override
    public String getMessage() {
        return "New notification scheduled";
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }
}
