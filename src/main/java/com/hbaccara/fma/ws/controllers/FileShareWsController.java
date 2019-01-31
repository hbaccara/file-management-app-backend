package com.hbaccara.fma.ws.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbaccara.fma.dto.NotificationDto;
import com.hbaccara.fma.entities.File;
import com.hbaccara.fma.entities.FileShareNotification;
import com.hbaccara.fma.entities.User;
import com.hbaccara.fma.enums.NotificationType;
import com.hbaccara.fma.mappers.NotificationMapper;
import com.hbaccara.fma.repository.FileRepository;
import com.hbaccara.fma.repository.NotificationRepository;
import com.hbaccara.fma.repository.UserRepository;
import com.hbaccara.fma.ws.request.FileShareRequest;

@Controller
public class FileShareWsController {

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationMapper notificationMapper;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private Gson gson = new Gson();

	@MessageMapping("/share")
	public void shareFile(@Payload String message){

		try {

			// extract the request parameters from the message
			FileShareRequest request = gson.fromJson(message, new TypeToken<FileShareRequest>() {
			}.getType());
			Long userId = request.getUserId();
			Long fileId = request.getFileId();
			Long userToShareWithId = request.getUserToShareWithId();

			// share the file with the specified user
			File file = fileRepository.findById(fileId).get();
			List<User> sharedWith = file.getSharedWith();

			if (sharedWith == null) {
				sharedWith = new ArrayList<>();
				file.setSharedWith(sharedWith);
			}

			User userToShareWith = userRepository.findById(userToShareWithId).get();
			sharedWith.add(userToShareWith);

			fileRepository.save(file);
			
			User sharer = userRepository.findById(userId).get();

			// create a notification
			FileShareNotification notification = new FileShareNotification();
			notification.setRead(false);
			notification.setType(NotificationType.FILE_SHARED);
			notification.setUser(userToShareWith);
			notification.setDate(new Date());
			notification.setFileId(fileId);
			notification.setFolder(file.isFolder());
			notification.setFilename(file.getName());
			notification.setSharer(sharer.getUsername());

			notificationRepository.save(notification);

			NotificationDto notificationDto = notificationMapper.notificationToNotificationDto(notification);

			this.messagingTemplate.convertAndSendToUser(userToShareWithId.toString(), "/shared", notificationDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
		}
	}
}
