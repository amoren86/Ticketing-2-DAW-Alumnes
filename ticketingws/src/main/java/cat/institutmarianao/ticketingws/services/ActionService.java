package cat.institutmarianao.ticketingws.services;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.Sort.Order;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Ticket.Category;
import cat.institutmarianao.ticketingws.model.Ticket.Status;
import cat.institutmarianao.ticketingws.model.User;

public interface ActionService {

//	Page<Action> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders,
//			@NotNull Type actionType, Status ticketStatus, User performer, Category category, Date from, Date to);

	List<Action> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders,
			@NotNull Type actionType, Status ticketStatus, User performer, Category category, Date from, Date to);

	/**
	 * Tracking
	 */
	List<Action> findByTicketId(@Positive Long ticketId);

	Action save(@NotNull @Valid Action action);

}
