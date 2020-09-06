package com.techlead.dao;

import org.springframework.data.repository.CrudRepository;

import com.techlead.model.Course;

public interface CourseDao extends CrudRepository<Course, Long>{
	
	Course findById(Integer id);
	
}
