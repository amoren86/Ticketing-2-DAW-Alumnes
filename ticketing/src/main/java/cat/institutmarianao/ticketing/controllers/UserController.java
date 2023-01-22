package cat.institutmarianao.ticketing.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.ticketing.components.forms.validators.UserFormValidator;
import cat.institutmarianao.ticketing.model.dto.UserDto;
import cat.institutmarianao.ticketing.model.dto.UserDto.Role;
import cat.institutmarianao.ticketing.model.forms.UserForm;
import cat.institutmarianao.ticketing.model.forms.UsersFilter;
import cat.institutmarianao.ticketing.services.CompanyService;
import cat.institutmarianao.ticketing.services.RoomService;
import cat.institutmarianao.ticketing.services.UserService;
import cat.institutmarianao.ticketing.validation.groups.OnUserCreate;
import cat.institutmarianao.ticketing.validation.groups.OnUserUpdate;

@Controller
@SessionAttributes({ "user", "roles" })
@RequestMapping(value = "/users")
public class UserController {
	@Autowired
	private UserFormValidator userFormValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private CompanyService companyService;

	@ModelAttribute("user")
	public UserDto setupUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return (UserDto) userService.loadUserByUsername(username);
	}

	@ModelAttribute("roles")
	public Role[] setupRoles() {
		return Role.values();
	}

	@InitBinder(value = "userForm")
	public void initialiseBinder(WebDataBinder binder) {
		binder.addValidators(userFormValidator);
	}

	@GetMapping("/new")
	public ModelAndView newUser() {
		ModelAndView newUserView = new ModelAndView("user");
		newUserView.getModelMap().addAttribute("pageTitle", "user.new.title");
		newUserView.getModelMap().addAttribute("rooms", roomService.getAllRooms());
		newUserView.getModelMap().addAttribute("companies", companyService.getAllCompanies());
		newUserView.getModelMap().addAttribute("MAX_EXTENSION", UserDto.MAX_EXTENSION);
		newUserView.getModelMap().addAttribute("userForm", new UserForm());

		newUserView.getModelMap().addAttribute("edit", false);

		return newUserView;
	}

	@PostMapping("/new")
	public String submitNewUser(@Validated(OnUserCreate.class) UserForm userForm, BindingResult result,
			ModelMap modelMap) {

		if (result.hasErrors()) {
			modelMap.addAttribute("pageTitle", "user.new.title");
			modelMap.addAttribute("rooms", roomService.getAllRooms());
			modelMap.addAttribute("companies", companyService.getAllCompanies());
			modelMap.addAttribute("MAX_EXTENSION", UserDto.MAX_EXTENSION);
			return "user";
		}

		userService.add(userForm.getUserDto());
		return "redirect:/users/list";
	}

	@GetMapping("/edit")
	public ModelAndView editUser(@ModelAttribute("user") UserDto userDto) {

		ModelAndView editUserView = new ModelAndView("user");
		editUserView.getModelMap().addAttribute("pageTitle", "user.edit.title");
		editUserView.getModelMap().addAttribute("rooms", roomService.getAllRooms());
		editUserView.getModelMap().addAttribute("companies", companyService.getAllCompanies());
		editUserView.getModelMap().addAttribute("MAX_EXTENSION", UserDto.MAX_EXTENSION);
		editUserView.getModelMap().addAttribute("userForm", new UserForm(userDto));

		editUserView.getModelMap().addAttribute("edit", true);

		return editUserView;
	}

	@PostMapping("/edit")
	public String submitEditUser(@Validated(OnUserUpdate.class) UserForm userForm, BindingResult result,
			ModelMap modelMap, Model model) {
		if (result.hasErrors()) {
			modelMap.addAttribute("pageTitle", "user.new.title");
			modelMap.addAttribute("rooms", roomService.getAllRooms());
			modelMap.addAttribute("companies", companyService.getAllCompanies());
			modelMap.addAttribute("MAX_EXTENSION", UserDto.MAX_EXTENSION);
			modelMap.addAttribute("edit", true);
			return "user";
		}

		if (userForm.getPassword() == null || userForm.getPassword().isBlank()) {
			userForm.setPassword(null);
		}

		UserDto userDto = userForm.getUserDto();

		userService.update(userDto);
		model.addAttribute("user", userDto);

		return "redirect:/users/list";
	}

	@GetMapping("/list")
	public ModelAndView allUsersList() {
		ModelAndView usersView = new ModelAndView("users");
		usersView.getModelMap().addAttribute("pageTitle", "users.list.title");
		usersView.getModelMap().addAttribute("usersFilter", new UsersFilter());
		usersView.getModelMap().addAttribute("usersList", userService.getAllUsers());
		return usersView;
	}

	@PostMapping("/ajax/list")
	@ResponseBody
	public List<UserDto> filterUserList(@RequestBody UsersFilter requestFilter) {
		return userService.filterUsers(requestFilter);
	}

	@GetMapping("/remove")
	public String removeUser(@RequestParam("username") String username) {
		userService.remove(username);
		return "redirect:/users/list";
	}
}
