package cat.institutmarianao.ticketingws.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.Action;

public class ActionWithDateFrom implements Specification<Action> {

	private static final long serialVersionUID = 1L;

	private Date from;

	public ActionWithDateFrom(Date from) {
		this.from = from;
	}

	@Override
	public Predicate toPredicate(Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (from == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}

		return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), from);
	}

}
