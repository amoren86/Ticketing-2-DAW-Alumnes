package cat.institutmarianao.ticketing.services;

import java.util.List;

import cat.institutmarianao.ticketing.model.dto.RoomDto;

public interface RoomService {
	RoomDto getById(Long id);

	List<RoomDto> getAllRooms();
}
