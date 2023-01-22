package cat.institutmarianao.ticketingws.services.impl;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.repositories.TicketRepository;
import cat.institutmarianao.ticketingws.services.TicketService;

@Validated
@Service
public class TicketServiceImpl implements TicketService {
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public Optional<Ticket> findById(@Positive Long id) {
		return ticketRepository.findById(id);
	}

	@Override
	public Ticket save(@NotNull @Valid Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	@Override
	public void deleteById(@Positive Long ticketId) {
		ticketRepository.deleteById(ticketId);
	}
}
