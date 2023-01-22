package cat.institutmarianao.ticketingws.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.Sort.Order;

import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;

public interface UserService {

//	Page<User> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders, Role[] roles,
//			String fullName);

	List<User> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders, Role[] roles,
			String fullName);

	Optional<User> findByUsername(@NotBlank String username);

	User getUserByUsername(@NotBlank String username);

	User save(@NotNull @Valid User user);

	User update(@NotNull @Valid User user);

	void deleteByUsername(@NotBlank String username);
}