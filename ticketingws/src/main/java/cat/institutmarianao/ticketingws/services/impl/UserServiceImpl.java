package cat.institutmarianao.ticketingws.services.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.ticketingws.model.Employee;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;
import cat.institutmarianao.ticketingws.repositories.UserRepository;
import cat.institutmarianao.ticketingws.services.UserService;
import cat.institutmarianao.ticketingws.specifications.UserWithFullName;
import cat.institutmarianao.ticketingws.specifications.UserWithRole;
import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;
import cat.institutmarianao.ticketingws.validation.groups.OnUserUpdate;

@Validated
@Service
public class UserServiceImpl implements UserService {
//	private static final int DEFAULT_PAGE = 0;
//	private static final int DEFAULT_SIZE = 10;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	// PAGINATION
//	@Override
//	public Page<User> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders, Role[] roles,
//			String fullName) {
//		Specification<User> spec = Specification.where(new UserWithRole(roles)).and(new UserWithFullName(fullName));
//
//		Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
//
//		if (page == null) {
//			page = DEFAULT_PAGE;
//		}
//		if (size == null) {
//			size = DEFAULT_SIZE;
//		}
//		return userRepository.findAll(spec, PageRequest.of(page, size, sort));
//	}

	@Override
	public List<User> findAll(@PositiveOrZero Integer page, @Positive Integer size, List<Order> orders, Role[] roles,
			String fullName) {
		Specification<User> spec = Specification.where(new UserWithRole(roles)).and(new UserWithFullName(fullName));

		Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);

		return userRepository.findAll(spec, sort);
	}

	@Override
	public Optional<User> findByUsername(@NotBlank String username) {
		return userRepository.findById(username);
	}

	@Override
	public User getUserByUsername(@NotBlank String username) {
		Optional<User> user = userRepository.findById(username);
		if (user.isEmpty()) {
			String errorMerrage = messageSource.getMessage("error.UserService.user.not.found",
					new Object[] { username }, LocaleContextHolder.getLocale());
			throw new ValidationException(errorMerrage);
		}
		return user.get();
	}

	@Override
	@Validated(OnUserCreate.class)
	public User save(@NotNull @Valid User user) {
		return userRepository.save(user);
	}

	@Override
	@Validated(OnUserUpdate.class)
	public User update(@NotNull @Valid User user) {
		User dbUser = getUserByUsername(user.getUsername());

		if (user.getPassword() != null) {
			dbUser.setPassword(user.getPassword());
		}
		if (user.getFullName() != null) {
			dbUser.setFullName(user.getFullName());
		}
		if (user.getExtension() != null) {
			dbUser.setExtension(user.getExtension());
		}

		if (user instanceof Employee employee && dbUser instanceof Employee dbEmployee) {
			if (employee.getRoom() != null) {
				dbEmployee.setRoom(employee.getRoom());
			}
			if (employee.getPlace() != null) {
				dbEmployee.setPlace(employee.getPlace());
			}
		} else if (user instanceof Technician technician && dbUser instanceof Technician dbTechnician) {
			if (technician.getCompany() != null) {
				dbTechnician.setCompany(technician.getCompany());
			}
		}

		return userRepository.save(dbUser);
	}

	@Override
	public void deleteByUsername(@NotBlank String username) {
		userRepository.deleteById(username);
	}

}
