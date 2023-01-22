package cat.institutmarianao.ticketingws.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.Ticket.Status;

public class ActionWithTicketStatus implements Specification<Action> {

	private static final long serialVersionUID = 1L;
	private Status status;

	public ActionWithTicketStatus(Status ticketStatus) {
		status = ticketStatus;
	}

//	@Override
//	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//		if (status == null) {
//			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
//		}
//		Subquery<Ticket> ticket = query.subquery(Ticket.class);
//		Root<Action> action = ticket.from(Action.class);
//
//		Subquery<Date> lastDate = query.subquery(Date.class);
//		Root<Action> tracking = lastDate.from(Action.class);
//
//		ticket.select(action.get("ticket"));
//		ticket.where(criteriaBuilder.equal(action.get("type"), status), action.get("date").in(lastDate));
//
//		lastDate.select(criteriaBuilder.greatest(tracking.get("date")));
//		lastDate.where(criteriaBuilder.equal(tracking.get("ticket"), action.get("ticket")));
//
//		return root.get("ticket").in(ticket);
//	}

	@Override
	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (status == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}

		Join<Action, Ticket> ticketsActions = root.join("ticket");

		return criteriaBuilder.equal(ticketsActions.get("status"), status);
	}
}
