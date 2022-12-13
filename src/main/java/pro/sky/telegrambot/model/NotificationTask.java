package pro.sky.telegrambot.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "notification_task")
@SQLDelete(sql = "UPDATE notification_task SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "text")
    private String text;
    @Column(name = "notify_date_time")
    private LocalDateTime notifyDateTime;
    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

    @PostPersist
    public void logNotificationAdded() {
        log.info(
                "Added notification: chatId={}, message={}, notifyDateTime={}",
                chatId,
                text,
                notifyDateTime
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationTask that = (NotificationTask) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
