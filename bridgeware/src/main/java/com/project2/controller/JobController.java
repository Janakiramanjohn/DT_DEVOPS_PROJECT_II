package com.project2.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project2.DAO.JobAppliedDAO;
import com.project2.DAO.JobDAO;
import com.project2.DAO.UserDAO;
import com.project2.model.Blog;
import com.project2.model.CurrentUserJobApplied;
import com.project2.model.Error;
import com.project2.model.Friends;
import com.project2.model.Job;
import com.project2.model.JobApplied;
import com.project2.model.User;



@RestController
public class JobController {
	@Autowired
	private JobDAO jobDao;
	@Autowired
	private JobAppliedDAO jobapplieddao;
	@Autowired
	private UserDAO userDao;
	@RequestMapping(value="/addjob",method=RequestMethod.POST)
	public void addjob(@RequestBody Job job)
	{
		job.setPostedon(new Date());
		jobDao.saveJob(job);
			
	}
	
	@RequestMapping(value="/seealljob",method=RequestMethod.GET)
	public List<Job> seeAlljob()
	{
		List<Job> allblog=(List<Job>)this.jobDao.getAllJobs();
		return allblog;
		
		
	}
	@RequestMapping(value="/displayjob/{id}")
	public Job displayblog(@PathVariable("id")int id)
	{
		Job job=jobDao.getJobById(id);
		return job;
		
	}
	@RequestMapping(value="/getjobappliedstatus/{jobid}")
	public CurrentUserJobApplied displaysblog(@PathVariable("jobid")int id,CurrentUserJobApplied cujob,HttpSession session)
	{
		String username=(String)session.getAttribute("userlogged");
		List<JobApplied> jobapp=jobapplieddao.getalljobapplied(id);
		int i=0;
		for(JobApplied noti1:jobapp)
		{
			i=i+1;
			if(noti1.getUsername().equals(username))
			{
			cujob.setApplied(true);	
			}
			
		}
		cujob.setNuma(i);
		
		
		return cujob;
		
	}
	@RequestMapping(value="/userjobapply/{jobid}")
	public void displayszblog(@PathVariable("jobid")int id,JobApplied cujob,HttpSession session)
	{
		String username=(String)session.getAttribute("userlogged");
		cujob.setUsername(username);
		cujob.setJobid(id);
		jobapplieddao.addJobApplied(cujob);
		
	}
	

}