package cat.institutmarianao.ticketingws.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.Ticket.Category;
import cat.institutmarianao.ticketingws.model.Ticket.Status;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.repositories.ActionRepository;
import cat.institutmarianao.ticketingws.services.ActionService;
import cat.institutmarianao.ticketingws.specifications.ActionWithDateFrom;
import cat.institutmarianao.ticketingws.specifications.ActionWithDateTo;
import cat.institutmarianao.ticketingws.specifications.ActionWithPerformer;
import cat.institutmarianao.ticketingws.specifications.ActionWithTicketCategory;
import cat.institutmarianao.ticketingws.specifications.ActionWithTicketStatus;
import cat.institutmarianao.ticketingws.specifications.ActionWithType;

@Validated
@Service
public class ActionServiceImpl implements ActionService {
//	private static final int DEFAULT_PAGE = 0;
//	private static final int DEFAULT_SIZE = 10;

	@Autowired
	private Validator validator;

	@Autowired
	private ActionRepository actionRepository;

	// PAGINATION
//	@Override
//	public Page<Action> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders,
//			@NotNull Type actionType, Status ticketStatus, User performer, Category category, Date from, Date to) {
//		Specification<Action> spec = Specification.where(new ActionWithType(actionType))
//				.and(new ActionWithTicketStatus(ticketStatus)).and(new ActionWithPerformer(performer))
//				.and(new ActionWithTicketCategory(category)).and(new ActionWithDateFrom(from))
//				.and(new ActionWithDateTo(to));
//
//		Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
//
//		if (page == null) {
//			page = DEFAULT_PAGE;
//		}
//		if (size == null) {
//			size = DEFAULT_SIZE;
//		}
//		return actionRepository.findAll(spec, PageRequest.of(page, size, sort));
//	}

	@Override
	public List<Action> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders,
			@NotNull Type actionType, Status ticketStatus, User performer, Category category, Date from, Date to) {
		Specification<Action> spec = Specification.where(new ActionWithType(actionType))
				.and(new ActionWithTicketStatus(ticketStatus)).and(new ActionWithPerformer(performer))
				.and(new ActionWithTicketCategory(category)).and(new ActionWithDateFrom(from))
				.and(new ActionWithDateTo(to));

		Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);

		return actionRepository.findAll(spec, sort);
	}

	/**
	 * Tracking
	 */
	@Override
	public List<Action> findByTicketId(@Positive Long ticketId) {
		return actionRepository.findByTicketIdOrderByDateDesc(ticketId);
	}

	@Override
	public Action save(@NotNull @Valid Action action) {
		Long ticketId = action.getTicket().getId();

		List<Action> tracking = actionRepository.findByTicketIdOrderByDateDesc(ticketId);
		tracking.add(0, action);
		Ticket ticket = new Ticket();
		ticket.setTracking(tracking);

		Set<ConstraintViolation<Ticket>> errors = validator.validateProperty(ticket, "tracking");

		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		return actionRepository.save(action);
	}

}
