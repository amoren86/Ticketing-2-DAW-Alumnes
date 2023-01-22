package cat.institutmarianao.ticketingws.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.ticketingws.model.Company;
import cat.institutmarianao.ticketingws.services.CompanyService;

@RestController
@RequestMapping("/companies")
@Validated
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@GetMapping(value = "/find/all")
	public List<Company> findAll() {
		return companyService.findAll();
	}

	@GetMapping("/find/by/id/{id}")
	public Optional<Company> findById(@PathVariable("id") @Positive Long id) {
		Optional<Company> opId = companyService.findById(id);
		if (opId.isEmpty()) {
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(opId.get());
	}
}
