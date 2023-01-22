package cat.institutmarianao.ticketing.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.ticketing.model.dto.EmployeeDto;
import cat.institutmarianao.ticketing.model.dto.TechnicianDto;
import cat.institutmarianao.ticketing.model.dto.UserDto;
import cat.institutmarianao.ticketing.model.dto.UserDto.Role;
import cat.institutmarianao.ticketing.model.forms.UsersFilter;
import cat.institutmarianao.ticketing.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final String HOST = "https://localhost";
	private static final String PORT = "8443";

	@Autowired
	private RestTemplate restTemplate;

	// TODO Parametrise
	@Override /* Spring security */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final String baseUri = HOST + ":" + PORT + "/users/find/by/username/{username}";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("username", username);

		UserDto response = restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(), UserDto.class);
		return response;
	}

	@Override
	public List<UserDto> getAllUsers() {
		final String uri = HOST + ":" + PORT + "/users/find/all";

		ResponseEntity<UserDto[]> response = restTemplate.getForEntity(uri, UserDto[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		final String baseUri = HOST + ":" + PORT + "/users/find/all";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		uriTemplate.queryParam("roles", Role.EMPLOYEE.name());

		ResponseEntity<EmployeeDto[]> response = restTemplate.getForEntity(uriTemplate.encode().toUriString(),
				EmployeeDto[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TechnicianDto> getAllTechnicians() {
		final String baseUri = HOST + ":" + PORT + "/users/find/all";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		uriTemplate.queryParam("roles", Role.TECHNICIAN.name());
		uriTemplate.queryParam("roles", Role.SUPERVISOR.name());

		ResponseEntity<TechnicianDto[]> response = restTemplate.getForEntity(uriTemplate.encode().toUriString(),
				TechnicianDto[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<UserDto> filterUsers(UsersFilter filter) {
		final String baseUri = HOST + ":" + PORT + "/users/find/all";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		if (filter.getRole() != null) {
			uriTemplate.queryParam("role", filter.getRole().name());
		}

		if (filter.getFullName() != null) {
			uriTemplate.queryParam("fullName", filter.getFullName());
		}

		ResponseEntity<UserDto[]> response = restTemplate.getForEntity(uriTemplate.encode().toUriString(), UserDto[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public UserDto add(UserDto userDto) {
		final String uri = HOST + ":" + PORT + "/users/save";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);

		return restTemplate.postForObject(uri, request, UserDto.class);
	}

	@Override
	public void update(UserDto userDto) {
		final String uri = HOST + ":" + PORT + "/users/update";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);

		restTemplate.put(uri, request);
	}

	@Override
	public void remove(String username) {
		final String baseUri = HOST + ":" + PORT + "/users/delete/by/username/{username}";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("username", username);

		restTemplate.delete(uriTemplate.buildAndExpand(uriVariables).toUriString(), UserDto.class);
	}
}
