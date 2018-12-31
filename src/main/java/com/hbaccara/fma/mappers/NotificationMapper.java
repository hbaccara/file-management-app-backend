package com.hbaccara.fma.mappers;

import org.springframework.stereotype.Service;

import com.hbaccara.fma.dto.FileShareNotificationDto;
import com.hbaccara.fma.dto.NotificationDto;
import com.hbaccara.fma.entities.FileShareNotification;
import com.hbaccara.fma.entities.Notification;

@Service
public class NotificationMapper{

	public NotificationDto notificationToNotificationDto(Notification notification) {
		
		NotificationDto notificationDto = null;

		if (notification instanceof FileShareNotification) {
			notificationDto = new FileShareNotificationDto();
			
			FileShareNotification fileShareNotification = (FileShareNotification) notification;
			FileShareNotificationDto fileShareNotificationDto = (FileShareNotificationDto) notificationDto;
			
			fileShareNotificationDto.setFileId(fileShareNotification.getFileId());
			fileShareNotificationDto.setFolder(fileShareNotification.isFolder());
			fileShareNotificationDto.setFilename(fileShareNotification.getFilename());
			fileShareNotificationDto.setSharer(fileShareNotification.getSharer());
		}
			
		notificationDto.setId(notification.getId());
		notificationDto.setMesssage(notification.getMessage());
		notificationDto.setRead(notification.isRead());
		notificationDto.setType(notification.getType() != null ? notification.getType().toString() : null);
		notificationDto.setDate(notification.getDate() != null ? notification.getDate().getTime() : null);
		
		return notificationDto;		
	}
	
}
