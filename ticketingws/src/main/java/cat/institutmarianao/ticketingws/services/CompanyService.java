package cat.institutmarianao.ticketingws.services;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import cat.institutmarianao.ticketingws.model.Company;

public interface CompanyService {

	List<Company> findAll();

	Optional<Company> findById(@Positive Long id);
}
