package com.hbaccara.fma.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbaccara.fma.dto.NotificationDto;
import com.hbaccara.fma.rest.services.NotificationService;

@RestController
public class NotificationRestController {

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/notification")
	public ResponseEntity<List<NotificationDto>> getNotifications(
			@RequestParam(required = true, value = "userId") Long userId){

		try {
			List<NotificationDto> notificationDtos = notificationService.getNotifications(userId);

			return ResponseEntity.status(HttpStatus.OK).body(notificationDtos);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/notification")
	public ResponseEntity<NotificationDto> markAsRead(@RequestParam(value = "id") Long id) {

		try {
			NotificationDto notificationDto = notificationService.markAsRead(id);

			return ResponseEntity.status(HttpStatus.OK).body(notificationDto);

		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
