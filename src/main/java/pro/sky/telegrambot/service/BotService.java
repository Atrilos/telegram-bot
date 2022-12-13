package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import pro.sky.telegrambot.command.Command;
import pro.sky.telegrambot.command.NotificationCommand;
import pro.sky.telegrambot.command.StartCommand;
import pro.sky.telegrambot.command.UnknownCommand;
import pro.sky.telegrambot.exception.DateInPastException;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {

    private static final String REGEX = "(.{16}) (.*)";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationRepository notificationRepository;

    @Transactional
    public Command parseMessage(Message message) {
        String text = message.getText();
        if (text.equals("/start")) {
            return new StartCommand(message.getFrom().getFirstName());
        } else {
            Matcher matcher = Pattern.compile(REGEX).matcher(text);
            if (matcher.matches()) {
                try {
                    LocalDateTime notificationDate = parseDate(matcher.group(1));
                    checkNotificationDate(notificationDate);
                    saveToDb(message, notificationDate, matcher.group(2));
                    return new NotificationCommand(notificationDate);
                } catch (DateTimeParseException e) {
                    log.error("Wrong date time for {}", matcher.group(1));
                    return new UnknownCommand("Parse error! Make sure to enter date for scheduler in the following format: dd.MM.yyyy HH:mm");
                } catch (DateInPastException e) {
                    return new UnknownCommand("Given date has already passed");
                }
            }
            log.error("Unknown command: {}", message.getText());
            return new UnknownCommand("Unknown command");
        }
    }

    private void checkNotificationDate(LocalDateTime notificationDate) {
        if (notificationDate.isBefore(LocalDateTime.now())) {
            log.error("Given Date {} is before current time", notificationDate);
            throw new DateInPastException("Can't schedule notification in the past");
        }
    }

    private void saveToDb(Message message, LocalDateTime notificationDate, String text) {
        NotificationTask notificationTask = NotificationTask.builder()
                .chatId(message.getChatId().toString())
                .text(text)
                .notifyDateTime(notificationDate)
                .build();
        notificationRepository.save(notificationTask);
    }

    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString, FORMATTER);
    }
}
