package cat.institutmarianao.ticketingws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.ticketingws.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
