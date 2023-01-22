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

import cat.institutmarianao.ticketingws.model.Room;
import cat.institutmarianao.ticketingws.services.RoomService;

@RestController
@RequestMapping("/rooms")
@Validated
public class RoomController {

	@Autowired
	private RoomService roomService;

	@GetMapping(value = "/find/all")
	public List<Room> findAll() {
		return roomService.findAll();
	}

	@GetMapping("/find/by/id/{id}")
	public Optional<Room> findById(@PathVariable("id") @Positive Long id) {
		Optional<Room> opId = roomService.findById(id);
		if (opId.isEmpty()) {
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(opId.get());
	}
}
