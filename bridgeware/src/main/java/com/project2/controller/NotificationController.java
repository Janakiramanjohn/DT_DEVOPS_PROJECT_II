package com.project2.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project2.DAO.NotificatioNumberDAO;
import com.project2.DAO.NotificationDAO;

import com.project2.DAOImpl.NotificationNumberDAOImpl;import com.project2.model.Notification;
import com.project2.model.NotificationNumber;


@RestController
public class NotificationController {
	@Autowired
	private NotificationDAO notificationdao;
	@Autowired
	private NotificatioNumberDAO notificationnumberdao;
	
	@RequestMapping(value="/getnotificationnumber",method=RequestMethod.GET)
	public NotificationNumber getnotificationnumber(HttpSession session,NotificationNumber notificationnumber)
	{
		String username=(String)session.getAttribute("userlogged");
		notificationnumber.setUsername(username);
		List<Notification> noti=notificationdao.getallnotificationsnotviewed(username);
		int i=0;
		for(Notification noti1:noti)
		{
			i=i+1;
			
		}
		notificationnumber.setNum(i);
		notificationnumberdao.updatenotificationnumber(notificationnumber);
		NotificationNumber notii=notificationnumberdao.getnotificationnumber(username);
		return notii;
		
		
	}
	@RequestMapping(value="/notificationclick",method=RequestMethod.GET)
	public NotificationNumber clicknotification(HttpSession session,NotificationNumber notificationnumber)
	{
		String username=(String)session.getAttribute("userlogged");
		notificationnumber.setUsername(username);
		List<Notification> noti=notificationdao.getallnotificationsnotviewed(username);
		int i=0;
		for(Notification noti1:noti)
		{
			noti1.setViewed(true);
			notificationdao.updatenotification(noti1);
			
		}
		notificationnumber.setNum(i);
		notificationnumberdao.updatenotificationnumber(notificationnumber);
		NotificationNumber notii=notificationnumberdao.getnotificationnumber(username);
		return notii;
		
		
	}
	@RequestMapping(value="/getallnotifications",method=RequestMethod.GET)
	public List<Notification> getallnotifications(HttpSession session)
	{
		String username=(String)session.getAttribute("userlogged");
		List<Notification> noti=notificationdao.getallnotifications(username);
		return noti;
				
	}
	@RequestMapping(value="/deletenoti/{id}",method=RequestMethod.GET)
	public void deletenoti(@PathVariable("id")int id)
	{
	notificationdao.deletenotification(id);
				
	}
}
