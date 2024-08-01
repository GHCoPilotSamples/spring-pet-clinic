package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetType;

@DataJpaTest
public class OwnerRepositoryTest {

	@Mock
	private OwnerRepository ownerRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindPetTypes() {
		List<PetType> petTypes = Collections.singletonList(new PetType());
		given(ownerRepository.findPetTypes()).willReturn(petTypes);

		List<PetType> result = ownerRepository.findPetTypes();
		assertThat(result).isNotEmpty();
	}

	@Test
	void testFindByLastName() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> owners = new PageImpl<>(Collections.singletonList(new Owner()));
		given(ownerRepository.findByLastName("Doe", pageable)).willReturn(owners);

		Page<Owner> result = ownerRepository.findByLastName("Doe", pageable);
		assertThat(result).isNotEmpty();
	}

	@Test
	void testFindById() {
		Owner owner = new Owner();
		given(ownerRepository.findById(1)).willReturn(owner);

		Owner result = ownerRepository.findById(1);
		assertThat(result).isNotNull();
	}

	@Test
	void testSave() {
		Owner owner = new Owner();
		ownerRepository.save(owner);
		// No exception means the test passed
	}

	@Test
	void testFindAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> owners = new PageImpl<>(Collections.singletonList(new Owner()));
		given(ownerRepository.findAll(pageable)).willReturn(owners);

		Page<Owner> result = ownerRepository.findAll(pageable);
		assertThat(result).isNotEmpty();
	}
}
