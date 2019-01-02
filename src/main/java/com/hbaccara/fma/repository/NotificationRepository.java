package com.hbaccara.fma.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.entities.Notification;

@Service
public interface NotificationRepository extends CrudRepository<Notification, Long> {

	@Query("SELECT n FROM Notification n WHERE user.id = :userId ORDER BY n.date DESC")
	public List<Notification> findNotificationsByUser(Long userId, Pageable pageRequest);
	
}
