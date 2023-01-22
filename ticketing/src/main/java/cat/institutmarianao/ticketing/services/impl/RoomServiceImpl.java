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

import cat.institutmarianao.ticketing.model.dto.RoomDto;
import cat.institutmarianao.ticketing.services.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	private static final String HOST = "https://localhost";
	private static final String PORT = "8443";

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public RoomDto getById(Long id) {
		final String baseUri = HOST + ":" + PORT + "/rooms/find/by/id/{id}";

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, Long> uriVariables = new HashMap<>();
		uriVariables.put("id", id);

		RoomDto response = restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(), RoomDto.class);
		return response;
	}

	@Override
	public List<RoomDto> getAllRooms() {
		final String uri = HOST + ":" + PORT + "/rooms/find/all";

		ResponseEntity<RoomDto[]> response = restTemplate.getForEntity(uri, RoomDto[].class);
		return Arrays.asList(response.getBody());
	}
}
