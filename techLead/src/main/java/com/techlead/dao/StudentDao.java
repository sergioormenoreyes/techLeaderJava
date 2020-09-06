package com.techlead.dao;

import org.springframework.data.repository.CrudRepository;

import com.techlead.model.Student;

public interface StudentDao extends CrudRepository<Student, Long> {

	Student findByRut(String rut);
	
}
