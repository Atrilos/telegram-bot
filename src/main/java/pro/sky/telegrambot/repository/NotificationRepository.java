package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationTask, Long> {
    @Query("select n from NotificationTask n where n.notifyDateTime <= ?1")
    Collection<NotificationTask> findByLessOrEqualNotifyDateTime(LocalDateTime notifyDateTime);
}
