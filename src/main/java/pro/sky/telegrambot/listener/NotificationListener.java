package pro.sky.telegrambot.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.telegrambot.bot.TelegramNotificationBot;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@RequiredArgsConstructor
@Component
@EnableAsync
@Slf4j
public class NotificationListener {

    private final TelegramNotificationBot bot;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 */1 * * * *")
    @Async
    @Transactional
    public void run() {
        Collection<NotificationTask> tasks =
                notificationRepository
                        .findByLessOrEqualNotifyDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        tasks.forEach(t -> {
            SendMessage sendMessage = new SendMessage(t.getChatId(), t.getText());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Unexpected TelegramApiException", e);
            }
        });
        notificationRepository.deleteAll(tasks);
    }
}
