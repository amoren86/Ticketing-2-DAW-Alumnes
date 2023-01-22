package cat.institutmarianao.ticketing.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.ticketing.model.dto.CompanyDto;
import cat.institutmarianao.ticketing.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	private static final String HOST = "https://localhost";
	private static final String PORT = "8443";

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public CompanyDto getById(Long id) {
		final String baseUri = HOST + ":" + PORT + "/companies/find/by/id/{id}";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, Long> uriVariables = new HashMap<>();
		uriVariables.put("id", id);

		CompanyDto response = restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(),
				CompanyDto.class);
		return response;
	}

	@Override
	public List<CompanyDto> getAllCompanies() {
		final String uri = HOST + ":" + PORT + "/companies/find/all";

		ResponseEntity<CompanyDto[]> response = restTemplate.getForEntity(uri, CompanyDto[].class);
		return Arrays.asList(response.getBody());
	}
}
