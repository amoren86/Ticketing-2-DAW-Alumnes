package cat.institutmarianao.ticketingws.services;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import cat.institutmarianao.ticketingws.model.Ticket;

public interface TicketService {

	Optional<Ticket> findById(@Positive Long id);

	Ticket save(@NotNull @Valid Ticket ticket);

	void deleteById(@Positive Long ticketId);

}