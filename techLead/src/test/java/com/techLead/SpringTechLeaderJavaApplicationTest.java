package com.techLead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.techlead.SpringTechLeaderJavaApplication;
import com.techlead.config.Utils;
import com.techlead.model.Student;


@SpringBootTest(classes = SpringTechLeaderJavaApplication.class)
public class SpringTechLeaderJavaApplicationTest {

	Utils utils = new Utils();

	@Test
	public void invalidRut() {
		Student user = new Student();
		assertThrows(ConstraintViolationException.class, () -> {
			utils.validateRut(user.getRut());
		});
	}
	
	@Test
	public void validRut() {
		Student user = new Student();
		user.setRut("16996969-8");
		assertEquals(true, utils.validateRut(user.getRut()));
	}
}
