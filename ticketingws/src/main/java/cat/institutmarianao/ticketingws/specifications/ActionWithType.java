package cat.institutmarianao.ticketingws.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;

public class ActionWithType implements Specification<Action> {

	private static final long serialVersionUID = 1L;
	private Type type;

	public ActionWithType(Type type) {
		this.type = type;
	}

	@Override
	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (type == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}
		return criteriaBuilder.equal(root.get("type"), type);
	}

}
