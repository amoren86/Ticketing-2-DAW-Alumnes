package cat.institutmarianao.ticketingws.services;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import cat.institutmarianao.ticketingws.model.Room;

public interface RoomService {
	List<Room> findAll();

	Optional<Room> findById(@Positive Long id);
}
