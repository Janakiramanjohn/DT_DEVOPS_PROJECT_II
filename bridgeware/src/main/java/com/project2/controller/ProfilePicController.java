package com.project2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.project2.DAO.BlogPictureDAO;
import com.project2.DAO.ProfilePictureDAO;
import com.project2.model.BlogPicture;
import com.project2.model.Error;
import com.project2.model.ProfilePicture;
@Controller
public class ProfilePicController 
{
	@Autowired
	private ProfilePictureDAO profilePictureDao;
	@Autowired
	private BlogPictureDAO blogPictureDao;
	@RequestMapping(value="/uploadprofilepic",method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePicture(@RequestParam CommonsMultipartFile image,HttpSession session){
		String username=(String)session.getAttribute("userlogged");
		if(username==null){
			Error error=new Error(4,"Please login");
			
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED); //2nd callback function
		}
		ProfilePicture profilePicture=new ProfilePicture();
		profilePicture.setUsername(username);
		profilePicture.setImage(image.getBytes());
		profilePictureDao.addprofilepicture(profilePicture);//insert or update 
		return new ResponseEntity<ProfilePicture>(profilePicture,HttpStatus.OK);
	}
	//<img src="http://localhost:..../middleware/getimage/nameoftheuser" alt="image not found">
	//NO HTTPSTATUS CODE
	//ONLY BYTE[]
	@RequestMapping(value="/getimage/{username}",method=RequestMethod.GET)
	public @ResponseBody byte[] getImage(@PathVariable("username")String Username,HttpSession session){
		
		
		ProfilePicture profilePicture=profilePictureDao.getProfilePicture(Username);
		
		if(profilePicture==null)
			return null;
		
		return profilePicture.getImage();
	}
	@RequestMapping(value="/uploadblogpic",method=RequestMethod.POST)
	public ResponseEntity<?> uploadBlogPicture(@RequestParam CommonsMultipartFile image,HttpSession session){
		String id=(String)session.getAttribute("blogid");
		int blog=Integer.valueOf(id);
		BlogPicture Picture=new BlogPicture();
		Picture.setBlogid(blog);
		Picture.setImage(image.getBytes());
		blogPictureDao.addblogpicture(Picture);
		session.removeAttribute("blogid");
		return new ResponseEntity<BlogPicture>(Picture,HttpStatus.OK);

	}
	//<img src="http://localhost:..../middleware/getimage/nameoftheuser" alt="image not found">
	//NO HTTPSTATUS CODE
	//ONLY BYTE[]
	@RequestMapping(value="/getblogimage/{blogid}",method=RequestMethod.GET)
	public @ResponseBody byte[] getkImage(@PathVariable("blogid")int blogid,HttpSession session){
		
		
		BlogPicture blogPicture=blogPictureDao.getBlogPicture(blogid);
		
		if(blogPicture==null)
			return null;
		
		return blogPicture.getImage();
	}
	
	

}
