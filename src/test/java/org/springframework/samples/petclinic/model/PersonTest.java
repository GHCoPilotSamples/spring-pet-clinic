package org.springframework.samples.petclinic.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testFirstNameNotBlank() {
		Person person = new Person();
		person.setFirstName("");
		person.setLastName("Doe");

		Set<ConstraintViolation<Person>> violations = validator.validate(person);
		assertEquals(1, violations.size());

		ConstraintViolation<Person> violation = violations.iterator().next();
		assertEquals("must not be blank", violation.getMessage());
		assertEquals("firstName", violation.getPropertyPath().toString());
	}

	@Test
	public void testLastNameNotBlank() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("");

		Set<ConstraintViolation<Person>> violations = validator.validate(person);
		assertEquals(1, violations.size());

		ConstraintViolation<Person> violation = violations.iterator().next();
		assertEquals("must not be blank", violation.getMessage());
		assertEquals("lastName", violation.getPropertyPath().toString());
	}

	@Test
	public void testValidPerson() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");

		Set<ConstraintViolation<Person>> violations = validator.validate(person);
		assertTrue(violations.isEmpty());
	}

}
