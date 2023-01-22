package cat.institutmarianao.ticketingws.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.ticketingws.controllers.utils.SortParamsUtils;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;
import cat.institutmarianao.ticketingws.model.dto.UserDto;
import cat.institutmarianao.ticketingws.services.UserService;
import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;
import cat.institutmarianao.ticketingws.validation.groups.OnUserUpdate;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/authenticate")
	public Optional<UserDto> authenticate(@RequestBody Map<String, String> usernamePassword) {
		Optional<User> opUser = userService.findByUsername(usernamePassword.get("username"));

		if (opUser.isPresent()
				&& passwordEncoder.matches(usernamePassword.get("password"), opUser.get().getPassword())) {
			return Optional.of(conversionService.convert(opUser.get(), UserDto.class));
		}

		return Optional.empty();
	}

	@GetMapping(value = "/find/all")
	public List<UserDto> findAll(@RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
			@RequestParam(value = "size", required = false) @Positive Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "username,asc") String[] sortFields,
			@RequestParam(value = "roles", required = false) Role[] roles,
			@RequestParam(value = "fullName", required = false) String fullName) {

		List<Order> orders = SortParamsUtils.getOrders(sortFields);
//		return userService.findAll(page, size, orders, role, fullName)
//				.map(u -> conversionService.convert(u, UserDto.class)).getContent();

		return userService.findAll(page, size, orders, roles, fullName).stream()
				.map(u -> conversionService.convert(u, UserDto.class)).collect(Collectors.toList());
	}

	@GetMapping("/find/by/username/{username}")
	public Optional<UserDto> findByUsername(@PathVariable("username") @NotBlank String username) {
		Optional<User> opUser = userService.findByUsername(username);
		if (opUser.isEmpty()) {
			return Optional.ofNullable(null);
		}
		return Optional.of(conversionService.convert(opUser.get(), UserDto.class));
	}

	@PostMapping("/save")
	@Validated(OnUserCreate.class)
	public UserDto save(@RequestBody @Valid UserDto userDto) {
		return conversionService.convert(userService.save(saveOrUpdate(userDto)), UserDto.class);
	}

	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public UserDto update(@RequestBody @Valid UserDto userDto) {
		return conversionService.convert(userService.update(saveOrUpdate(userDto)), UserDto.class);
	}

	@DeleteMapping("/delete/by/username/{username}")
	public void deleteByUsername(@PathVariable("username") @NotBlank String username) {
		userService.deleteByUsername(username);
	}

	private User saveOrUpdate(UserDto userDto) {
		String rawPassword = userDto.getPassword();
		if (rawPassword != null) {
			userDto.setPassword(passwordEncoder.encode(rawPassword));
		}
		User user = conversionService.convert(userDto, User.class);
		return user;
	}
}
