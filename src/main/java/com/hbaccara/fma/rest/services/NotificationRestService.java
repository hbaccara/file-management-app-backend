package com.hbaccara.fma.rest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.NotificationDto;
import com.hbaccara.fma.entities.Notification;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.mappers.NotificationMapper;
import com.hbaccara.fma.repository.NotificationRepository;

@Service
public class NotificationRestService {

	private static final int NUMBER_OF_NOTIFICATIONS_TO_LOAD = 4;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationMapper notificationMapper;

	public List<NotificationDto> getNotifications(User user) {

		List<NotificationDto> notificationDtos = new ArrayList<>();

		List<Notification> notifications = notificationRepository.findNotificationsByUser(user.getId(),
				PageRequest.of(0, NUMBER_OF_NOTIFICATIONS_TO_LOAD));

		for (Notification notification : notifications) {

			NotificationDto notificationDto = notificationMapper.notificationToNotificationDto(notification);

			notificationDtos.add(notificationDto);
		}

		return notificationDtos;
	}

	public NotificationDto markAsRead(Long id) {

		Notification notification = notificationRepository.findById(id).get();
		notification.setRead(true);
		notificationRepository.save(notification);

		NotificationDto notificationDto = notificationMapper.notificationToNotificationDto(notification);

		return notificationDto;
	}

}
