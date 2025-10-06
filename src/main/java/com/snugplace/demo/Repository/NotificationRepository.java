package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

        List<Notification> findByReceiverId(Long userId);

        int countByReceiverIdAndReadFalse(Long userId);
}
