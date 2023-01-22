package cat.institutmarianao.ticketingws.services.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.ticketingws.model.Company;
import cat.institutmarianao.ticketingws.repositories.CompanyRepository;
import cat.institutmarianao.ticketingws.services.CompanyService;

@Validated
@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	@Override
	public Optional<Company> findById(@Positive Long id) {
		return companyRepository.findById(id);
	}
}
