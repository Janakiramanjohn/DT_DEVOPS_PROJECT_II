package com.project2.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project2.DAO.BlogCommentDao;
import com.project2.DAO.BlogDAO;
import com.project2.DAO.BlogLikesDAO;
import com.project2.DAO.NotificationDAO;
import com.project2.DAO.UserDAO;
import com.project2.model.Blog;
import com.project2.model.BlogComment;
import com.project2.model.BlogLikes;
import com.project2.model.BlogLikesNumber;
import com.project2.model.Error;
import com.project2.model.Friends;
import com.project2.model.Notification;
import com.project2.model.User;

@RestController
public class BlogController 
{
	@Autowired
	private UserDAO userdao;
@Autowired
private BlogDAO blogdao;
@Autowired
private BlogLikesDAO blogdaolikes;
@Autowired
private BlogCommentDao commentdao;
@Autowired
private NotificationDAO notificationdao;
@RequestMapping(value="/addblog",method=RequestMethod.POST)
public ResponseEntity<?> addUser(@RequestBody Blog blog,HttpSession session)
{
	String username=(String)session.getAttribute("userlogged");
	blog.setUsername(username);
	blog.setPostedon(new Date());
	blogdao.addBlog(blog);
	int id=blog.getBlogid();
	String blogid=Integer.toString(id);
	session.setAttribute("blogid",blogid);
	Blog blog1=blogdao.getblog(id);
	return new ResponseEntity<Blog>(blog1,HttpStatus.OK);
	
	
}
@RequestMapping(value="/seeallblog",method=RequestMethod.GET)
public List<Blog> seeAllBlog()
{
	List<Blog> allblog=(List<Blog>)this.blogdao.getallblog();
	return allblog;
	
	
}
@RequestMapping(value="/seeallblogadmin",method=RequestMethod.GET)
public ResponseEntity<?> seeAllBlogadmin(HttpSession session)
{
	String username=(String)session.getAttribute("userlogged");
	User user=userdao.getuser(username);
	if(user.getRole().equals("ADMIN"))
	{
		List<Blog> allblog=(List<Blog>)this.blogdao.getallblogadmin();
		return new ResponseEntity<List<Blog>>(allblog,HttpStatus.OK);
	}
	
	Error error=new Error(1,"UNAUTHORIZED ACESSS");
	
	return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	
}
@RequestMapping(value="/display/{id}")
public Blog displayblog(@PathVariable("id")int id)
{
	Blog blog=blogdao.getblog(id);
	return blog;
	
}
@RequestMapping(value="/check/{id}")
public List<BlogLikes> displayblsosg(@PathVariable("id")int id,HttpSession session)
{
	String username1=(String)session.getAttribute("userlogged");
	List<BlogLikes> likeslist1=blogdaolikes.getallbloglikes(id);
	return likeslist1;
	
}@RequestMapping(value="/getallbloglikes/{id}")
public BlogLikesNumber updateblogliskes(@PathVariable("id")int id,BlogLikesNumber bloglikesnumber,HttpSession session)

{
	int i=0;
	int j=0;
	String username1=(String)session.getAttribute("userlogged");
	
	List<BlogLikes> likeslist1=blogdaolikes.getallbloglikes(id);
	for(BlogLikes noti1:likeslist1)
		
	{
		i=i+1;
		if(username1.equals(noti1.getLikedby()))
		{

			j=1;
			bloglikesnumber.setLiked(true);	
			
		}else
		{
			
		}
		
	}
	if(j==0)
	{
		bloglikesnumber.setLiked(false);
	}
	
	bloglikesnumber.setBlogid(id);
	bloglikesnumber.setNum2(i);
	blogdaolikes.updatebloglikesnumber(bloglikesnumber);
	BlogLikesNumber num=blogdaolikes.getBlogLikesNumber(id);
	return num;
	
	
	
	
	
	
}


@RequestMapping(value="/updatebloglikes/{blogid}")
public void updatebloglikes(@PathVariable("blogid")int id,BlogLikes bloglikes,HttpSession session)

{
	int i=0;
	int j=0;
	String username=(String)session.getAttribute("userlogged");
	bloglikes.setBlogid(id);
	bloglikes.setLikedby(username);
	blogdaolikes.addBlogLikes(bloglikes);
	
	List<BlogLikes> likeslist=blogdaolikes.getallbloglikes(id);
	for(BlogLikes noti1:likeslist)
		
	{
		if(noti1.getLikedby().equals(username))
		{
			i=1;
			j=j+1;
			
			
		
		}
		
	}
for(BlogLikes noti1:likeslist)
		
	{
		if(noti1.getLikedby().equals(username))
		{
			if(j>1)
			{
			
			blogdaolikes.deleteBlogLikes(noti1);
			
			}
		}
		
	}
	
	
	if(i==0)
	{
		bloglikes.setBlogid(id);
		bloglikes.setLikedby(username);
		blogdaolikes.addBlogLikes(bloglikes);
	}
	
	
	
	
	
	
}
@RequestMapping(value="/adminapprove/{id}")
public void approveblog(@PathVariable("id")int id,Notification notification)
{
	Blog blog=blogdao.getblog(id);
	blog.setApproved(true);
	notification.setUsername(blog.getUsername());
	String blogtitle=blog.getBlogtitle();
	String s="Your Blog Titled '"+blogtitle+"' has been approved ";
	notification.setNotification(s);
	blogdao.updateBlog(blog);
	notificationdao.addnotification(notification);
	
	
}
@RequestMapping(value="/adminreject/{id}")
public void rejectblog(@PathVariable("id")int id,Notification notification)
{
	Blog blog=blogdao.getblog(id);
	blog.setRejected(true);
	notification.setUsername(blog.getUsername());
	String blogtitle=blog.getBlogtitle();
	String s="Your Blog Titled '"+blogtitle+"' has been rejected ";
	notification.setNotification(s);
	notificationdao.addnotification(notification);
	blogdao.updateBlog(blog);
	
	
}
@RequestMapping(value="/addcomment/{blogid}/{blogmessage}")
public boolean displayblogf(@PathVariable("blogid")int id,@PathVariable("blogmessage")String comment,BlogComment blogcomment,HttpSession session)
{
	String username=(String)session.getAttribute("userlogged");
	blogcomment.setBlogid(id);
	blogcomment.setMessage(comment);
	blogcomment.setUsername(username);
	blogcomment.setPostedon(new Date());
	
	commentdao.addBlogComment(blogcomment);
	return true;
}
@RequestMapping(value="/displaymessage/{id}")
public List<BlogComment> seeAllBlogc(@PathVariable("id")int blogid)
{
	List<BlogComment> allblog=(List<BlogComment>)this.commentdao.getallblogcoment(blogid);
	return allblog;
	
	
}


	

}
