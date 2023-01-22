package cat.institutmarianao.ticketingws.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;

public class UserWithRole implements Specification<User> {

	private static final long serialVersionUID = 1L;
	private Role[] roles;

	public UserWithRole(Role[] roles) {
		this.roles = roles;
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (roles == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // always true = no filtering
		}
		return root.get("role").in(roles);
	}

}
