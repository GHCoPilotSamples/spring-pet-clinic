package org.springframework.samples.petclinic.owner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class OwnerControllerTest {

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private RedirectAttributes redirectAttributes;

	@Mock
	private Model model;

	@InjectMocks
	private OwnerController ownerController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testInitCreationForm() {
		Map<String, Object> model = mock(Map.class);
		String view = ownerController.initCreationForm(model);
		assertEquals("owners/createOrUpdateOwnerForm", view);
		verify(model).put(eq("owner"), any(Owner.class));
	}

	@Test
	void testProcessCreationFormSuccess() {
		Owner owner = new Owner();
		when(bindingResult.hasErrors()).thenReturn(false);
		String view = ownerController.processCreationForm(owner, bindingResult, redirectAttributes);
		assertEquals("redirect:/owners/" + owner.getId(), view);
		verify(ownerRepository).save(owner);
		verify(redirectAttributes).addFlashAttribute("message", "New Owner Created");
	}

	@Test
	void testProcessCreationFormHasErrors() {
		Owner owner = new Owner();
		when(bindingResult.hasErrors()).thenReturn(true);
		String view = ownerController.processCreationForm(owner, bindingResult, redirectAttributes);
		assertEquals("owners/createOrUpdateOwnerForm", view);
		verify(redirectAttributes).addFlashAttribute("error", "There was an error in creating the owner.");
	}

	@Test
	void testInitFindForm() {
		String view = ownerController.initFindForm();
		assertEquals("owners/findOwners", view);
	}

	@Test
	void testProcessFindFormNoOwnersFound() {
		Owner owner = new Owner();
		owner.setLastName("NonExistent");
		when(ownerRepository.findByLastName(anyString(), any())).thenReturn(Page.empty());
		String view = ownerController.processFindForm(1, owner, bindingResult, model);
		assertEquals("owners/findOwners", view);
		verify(bindingResult).rejectValue("lastName", "notFound", "not found");
	}

	@Test
	void testProcessFindFormOneOwnerFound() {
		Owner owner = new Owner();
		owner.setId(1);
		owner.setLastName("Existing");
		Page<Owner> page = new PageImpl<>(Collections.singletonList(owner));
		when(ownerRepository.findByLastName(anyString(), any())).thenReturn(page);
		String view = ownerController.processFindForm(1, owner, bindingResult, model);
		assertEquals("redirect:/owners/1", view);
	}

	@Test
	void testProcessFindFormMultipleOwnersFound() {
		Owner owner1 = new Owner();
		owner1.setId(1);
		Owner owner2 = new Owner();
		owner2.setId(2);
		Page<Owner> page = new PageImpl<>(List.of(owner1, owner2));
		when(ownerRepository.findByLastName(anyString(), any())).thenReturn(page);
		String view = ownerController.processFindForm(1, new Owner(), bindingResult, model);
		assertEquals("owners/ownersList", view);
		verify(model).addAttribute("currentPage", 1);
		verify(model).addAttribute("totalPages", 1);
		//verify(model).addAttribute("totalItems", 2);
		verify(model).addAttribute("listOwners", List.of(owner1, owner2));
	}

	@Test
	void testInitUpdateOwnerForm() {
		Owner owner = new Owner();
		owner.setId(1);
		when(ownerRepository.findById(1)).thenReturn(owner);
		String view = ownerController.initUpdateOwnerForm(1, model);
		assertEquals("owners/createOrUpdateOwnerForm", view);
		verify(model).addAttribute(owner);
	}

	@Test
	void testProcessUpdateOwnerFormSuccess() {
		Owner owner = new Owner();
		when(bindingResult.hasErrors()).thenReturn(false);
		String view = ownerController.processUpdateOwnerForm(owner, bindingResult, 1, redirectAttributes);
		assertEquals("redirect:/owners/{ownerId}", view);
		verify(ownerRepository).save(owner);
		verify(redirectAttributes).addFlashAttribute("message", "Owner Values Updated");
	}

	@Test
	void testProcessUpdateOwnerFormHasErrors() {
		Owner owner = new Owner();
		when(bindingResult.hasErrors()).thenReturn(true);
		String view = ownerController.processUpdateOwnerForm(owner, bindingResult, 1, redirectAttributes);
		assertEquals("owners/createOrUpdateOwnerForm", view);
		verify(redirectAttributes).addFlashAttribute("error", "There was an error in updating the owner.");
	}

	@Test
	void testShowOwner() {
		Owner owner = new Owner();
		owner.setId(1);
		when(ownerRepository.findById(1)).thenReturn(owner);
		ModelAndView mav = ownerController.showOwner(1);
		assertEquals("owners/ownerDetails", mav.getViewName());
		assertEquals(owner, mav.getModel().get("owner"));
	}

}