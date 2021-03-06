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
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlead.config.Utils;
import com.techlead.dao.StudentDao;
import com.techlead.model.Student;
import com.techlead.model.StudentWrapper;

@RestController // This means that this class is a Controller
@RequestMapping(path = "/") // This means URL's start with /demo (after Application path)
public class StudentController {

	@Autowired
	StudentDao studenteRepo;
	@Autowired
	Utils util;

	@GetMapping(value = "students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Student>> getAll(@RequestHeader(value = "Accept") String acceptHeader) {
		return new ResponseEntity<Iterable<Student>>(studenteRepo.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "students/{rut}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> getStudentById(@PathVariable String rut,
			@RequestHeader(value = "Accept") String acceptHeader) {
		Student rsp;
		if (util.validateRut(rut)) {
			rsp = studenteRepo.findByRut(rut);
			Optional<Student> opt = Optional.ofNullable(rsp);
			if (!opt.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<Student>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Student>(rsp, HttpStatus.OK);

	}

	@PostMapping(value = "students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> saveAll(@Valid @RequestBody StudentWrapper studentWrapper,
			@RequestHeader(value = "Accept") String acceptHeader) {
		try {
			for (Student student : studentWrapper.getStudents()) {
				if (util.validateRut(student.getRut())) {
					studenteRepo.save(student);
				} else {
					return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
				}
			}
		} catch (ConstraintViolationException | TransactionSystemException e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.CREATED);
	}

	@PutMapping(value = "students/{rut}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> updateById(@Valid @RequestBody StudentWrapper studentWrapper,
			@PathVariable String rut, @RequestHeader(value = "Accept") String acceptHeader) {
		if (util.validateRut(rut)) {
			try {
				Student find = studenteRepo.findByRut(rut);
				for (Student student : studentWrapper.getStudents()) {
					if (null != find) {
						find.setAge(student.getAge());
						find.setLastName(student.getLastName());
						find.setName(student.getName());
						find.setCourse_id(student.getCourse_id());
						studenteRepo.save(find);
					} else {
						return new ResponseEntity<List<JSONObject>>(HttpStatus.NOT_FOUND);
					}
				}
			} catch (ConstraintViolationException e) {
				return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.OK);

	}

	@DeleteMapping(value = "students/{rut}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> deleteById(@Valid @PathVariable String rut,
			@RequestHeader(value = "Accept") String acceptHeader) {
		try {
			if (util.validateRut(rut)) {
				Student find = studenteRepo.findByRut(rut);
				if (null != find) {
					studenteRepo.delete(find);
				} else {
					return new ResponseEntity<List<JSONObject>>(HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
			}
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<JSONObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JSONObject>>(HttpStatus.NO_CONTENT);
	}
}
