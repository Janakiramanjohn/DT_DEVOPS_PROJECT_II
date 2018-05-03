package com.project2.controller;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

import com.project2.DAO.FriendDAO;
import com.project2.DAO.FriendRequestNumberDAO;
import com.project2.DAO.UserDAO;
import com.project2.model.*;
import com.project2.model.Error;
@RestController
public class UserController {
	@Autowired
	private UserDAO userdao;
	@Autowired
	private FriendDAO frienddao;
	@Autowired
	private FriendRequestNumberDAO frdnumberdao;
	@RequestMapping(value="/getfrdnumber",method=RequestMethod.GET)
	public FriendRequestsNumber getfrdnumbernumber(HttpSession session,FriendRequestsNumber frdnumber)
	{
		String username=(String)session.getAttribute("userlogged");
		frdnumber.setUsername(username);
		List<Friends> noti=frienddao.getallfriendrequest(username);
		int i=0;
		for(Friends noti1:noti)
		{
			i=i+1;
			
		}
		frdnumber.setNum(i);
		frdnumberdao.updatefrdrequestnumber(frdnumber);
		FriendRequestsNumber notii=frdnumberdao.getnfrdnumber(username);
		
		
		return notii;
		
		
	}
	@RequestMapping(value="/adduser",method=RequestMethod.POST)
	public void addUser(@RequestBody User user)
	
	{
		
		userdao.addUser(user);
		
		
	}
	@RequestMapping(value="/friendrequests",method=RequestMethod.GET)
	public List<Friends> getfriendrequest(HttpSession session)
	{
		String username=(String)session.getAttribute("userlogged");
		List<Friends> alluser=(List<Friends>)this.frienddao.getallfriendrequest(username);
		return alluser;
		
	}

	@RequestMapping(value="/seeusers",method=RequestMethod.GET)
	public List<User> seealluser()
	{
			
		List<User> alluser=(List<User>)this.userdao.gettalluser();
		return alluser;
		
	}
	@RequestMapping(value="/suggesteduser",method=RequestMethod.GET)
	public List<User> suggesteduser(HttpSession session)
	{
		
		String username=(String)session.getAttribute("userlogged");
		List<User> alluser=(List<User>)this.userdao.gettallsuggesteduser(username);

		
	
	
		
		
		return alluser;
		
	}
	@RequestMapping(value="/deleteuser/{user}",method=RequestMethod.GET)
	public List<User> deleteuser(@PathVariable("user") String username)
	{
		
		userdao.deleteuser(username);
		List<User> alluser=(List<User>)this.userdao.gettalluser();
		return alluser;
	}
	@RequestMapping(value="/addfriend/{username}",method=RequestMethod.GET)
	public List<User> addfriend(@PathVariable("username") String username,HttpSession session,Friends friend)
	{
		String username1=(String)session.getAttribute("userlogged");
		friend.setFromid(username1);
		friend.setToid(username);
		friend.setRejected(false);
		friend.setAccepted(false);
		frienddao.addFriend(friend);
		
		
		
		List<User> alluser=(List<User>)this.userdao.gettalluser();
		return alluser;
	}
	@RequestMapping(value="/edituser/{user}",method=RequestMethod.GET)
	public User edituser(@PathVariable("user")String username)
	{
		User user=userdao.getuser(username);
		
		return user;
		
	}
	@RequestMapping(value="/currentuser",method=RequestMethod.GET)
	public User edituser(HttpSession session)
	
	{
		
		String username=(String)session.getAttribute("userlogged");

		User user=userdao.getuser(username);
		
		return user;
		
	}
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public void update(@RequestBody User user,HttpSession session)
	{
		userdao.edituser(user);
		

		
		
	}
	@RequestMapping(value="/loginuser",method=RequestMethod.POST)
	public ResponseEntity<?> checkusername(@RequestBody User user, HttpSession session)
	{
		 String username=user.getUsername();
		 String password=user.getPassword();
		 
		 
		 
		int i=0;
		List<User> alluser=(List<User>)this.userdao.gettalluser();
		for(User user1:alluser)
		{
			
			if(username.equals(user1.getUsername()))
			{
				if(password.equals(user1.getPassword()))
				{
					
					session.setAttribute("userlogged",username);
					

					User user2=userdao.getuser(username);
					
					return new ResponseEntity<User>(user2,HttpStatus.OK);
				}
			}
		}
		
		
			Error error=new Error(1,"Invalid Username or password");
			
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			
		
		
		
		
	}
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public void logout(HttpSession session)
	{
		session.removeAttribute("userlogged");
		session.invalidate();
	}
	@RequestMapping(value="/acceptfriendrequest/{friendid}")
	public void acceptfriendrequest(@PathVariable("friendid")long id,Notification noti)
	{
		
		Friends frd=(Friends)this.frienddao.getFriendId(id);
		frd.setAccepted(true);
		frd.setRejected(false);
		frienddao.addFriend(frd);
		String s=frd.getToid();
		String g=s+" accepted your friend request";
	}
	@RequestMapping(value="/rejectfriendrequest/{friendid}")
	public void rejectfriendrequest(@PathVariable("friendid")long id,Notification noti)
	{
		Friends frd=(Friends)this.frienddao.getFriendId(id);
		frd.setAccepted(false);
		frd.setRejected(true);
		frienddao.addFriend(frd);
		frienddao.deleteFriendId(id);
		
		
		
	}
	

}
