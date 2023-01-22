package cat.institutmarianao.ticketingws.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.User;

public class ActionWithPerformer implements Specification<Action> {

	private static final long serialVersionUID = 1L;
	private User performer;

	public ActionWithPerformer(User performer) {
		this.performer = performer;
	}

	@Override
	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (performer == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}

		return criteriaBuilder.equal(root.get("performer"), performer);
	}

}
