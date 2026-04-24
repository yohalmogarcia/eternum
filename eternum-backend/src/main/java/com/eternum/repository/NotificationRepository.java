package com.eternum.repository;

import com.eternum.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByFkUserOrderByCreatedDateDesc(Integer fkUser);

    List<Notification> findByFkUserAndIsReadFalseOrderByCreatedDateDesc(Integer fkUser);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readDate = CURRENT_TIMESTAMP WHERE n.pkNotification = :notificationId")
    void markAsRead(Integer notificationId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readDate = CURRENT_TIMESTAMP WHERE n.fkUser = :userId AND n.isRead = false")
    void markAllAsReadForUser(Integer userId);

}
