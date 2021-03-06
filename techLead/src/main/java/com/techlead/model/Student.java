package com.techlead.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name = "student")
public class Student {

	@NotBlank(message = "validation error")
	@Column(name = "name")
	private String name;

	@NotBlank(message = "validation error")
	@Column(name = "last_name")
	private String lastName;

	@Column(name="course_id")
	private Integer course_id;

	@Range(min = 18)
	private Integer age;
	@Id
	@NotBlank(message = "validation error")
	@Column(name = "rut")
	private String rut;
}
