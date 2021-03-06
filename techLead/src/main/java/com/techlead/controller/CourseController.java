package com.techlead.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlead.dao.CourseDao;
import com.techlead.dao.StudentDao;
import com.techlead.model.Course;
import com.techlead.model.CourseWrapper;


@RestController // This means that this class is a Controller
@RequestMapping(path = "/") // This means URL's start with /demo (after Application path)
public class CourseController {

	
	@Autowired
	StudentDao studenteRepo;
	
	@Autowired
	CourseDao coursesRepo;
	
	@GetMapping(value = "courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Course>> getAll( @RequestHeader(value="Accept") String acceptHeader) {
		return new ResponseEntity<Iterable<Course>>(coursesRepo.findAll(), HttpStatus.OK);
	}
	

	@GetMapping(value = "courses/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Course> getCourseId(@PathVariable Integer id, @RequestHeader(value="Accept") String acceptHeader) {
		Course rsp = coursesRepo.findById(id);
		Optional<Course> opt = Optional.ofNullable(rsp);
		if(!opt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Course>(rsp, HttpStatus.OK);
	}	
	
	@PostMapping(value = "courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> saveAll(@Valid @RequestBody CourseWrapper course, @RequestHeader(value="Accept") String acceptHeader) {
		try {
			//Stream.of(course).forEach(e, course -> coursesRepo.save(course));
			for (Course person : course.getCourses()) {
				coursesRepo.save(person);
			}
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "courses/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> updateById(@Valid @RequestBody CourseWrapper courseWrapper, @PathVariable Integer id, @RequestHeader(value="Accept") String acceptHeader) {
		try {
			
			Course find = coursesRepo.findById(id);
			for (Course course : courseWrapper.getCourses()) {
				if (null != find){
					find.setCode(course.getCode());
					find.setName(course.getName());
					coursesRepo.save(find);
				} else {
					return new ResponseEntity<List<JSONObject>>(HttpStatus.NOT_FOUND);
				}
			}
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "courses/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> deleteById(@Valid  @PathVariable Integer id, @RequestHeader(value="Accept") String acceptHeader) {
		try {
				Course find = coursesRepo.findById(id);
				if (null != find){
					coursesRepo.delete(find);
				} else {
					return new ResponseEntity<List<JSONObject>>(HttpStatus.NOT_FOUND);
				}
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.NO_CONTENT);
	}
}
