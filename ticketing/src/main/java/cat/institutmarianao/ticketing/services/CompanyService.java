package cat.institutmarianao.ticketing.services;

import java.util.List;

import cat.institutmarianao.ticketing.model.dto.CompanyDto;

public interface CompanyService {
	List<CompanyDto> getAllCompanies();

	CompanyDto getById(Long id);
}
