package cat.institutmarianao.ticketingws.services.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.ticketingws.model.Room;
import cat.institutmarianao.ticketingws.repositories.RoomRepository;
import cat.institutmarianao.ticketingws.services.RoomService;

@Validated
@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public List<Room> findAll() {
		return roomRepository.findAll();
	}

	@Override
	public Optional<Room> findById(@Positive Long id) {
		return roomRepository.findById(id);
	}
}
