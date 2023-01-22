package cat.institutmarianao.ticketingws.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.Ticket.Category;

public class ActionWithTicketCategory implements Specification<Action> {

	private static final long serialVersionUID = 1L;
	private Category category;

	public ActionWithTicketCategory(Category category) {
		this.category = category;
	}

	@Override
	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (category == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}

		Join<Action, Ticket> ticketsActions = root.join("ticket");

		return criteriaBuilder.equal(ticketsActions.get("category"), category);
	}

}
