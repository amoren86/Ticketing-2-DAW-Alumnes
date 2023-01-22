package cat.institutmarianao.ticketing.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.ticketing.model.dto.AssignmentDto;
import cat.institutmarianao.ticketing.model.dto.InterventionDto;
import cat.institutmarianao.ticketing.model.dto.TicketDto;
import cat.institutmarianao.ticketing.model.dto.TicketDto.Category;
import cat.institutmarianao.ticketing.model.dto.TicketDto.Status;
import cat.institutmarianao.ticketing.model.dto.UserDto;
import cat.institutmarianao.ticketing.model.forms.TicketsFilter;
import cat.institutmarianao.ticketing.services.UserService;

@Controller
@SessionAttributes({ "user", "categories" })
@RequestMapping(value = "/tickets")
public class TicketController {

	@Autowired
	private UserService userService;

	@ModelAttribute("user")
	public UserDto setupUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return (UserDto) userService.loadUserByUsername(username);
	}

	@ModelAttribute("categories")
	public Category[] setupCategories() {
		return TicketDto.Category.values();
	}

	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		DateFormatter dateFormatter = new DateFormatter();
		dateFormatter.setPattern("yyyy-MM-dd'T'HH:mm");
		binder.addCustomFormatter(dateFormatter, "date");
	}

	@GetMapping("/new")
	public ModelAndView newTicket(@ModelAttribute("user") UserDto userDto) {
		ModelAndView newTicketsView = new ModelAndView("ticket");
		// TODO new ticket
		return newTicketsView;
	}

	@PostMapping("/new")
	public String submitNewTicket(@Validated TicketDto ticketDto, BindingResult result, ModelMap modelMap) {
		// TODO submit new ticket
		return null;
	}

	@GetMapping("/list/{ticket-status}")
	public ModelAndView allTicketsList(@ModelAttribute("user") UserDto userDto,
			@PathVariable("ticket-status") Status ticketStatus) {
		ModelAndView ticketsView = new ModelAndView("tickets");
		// TODO retrieve all tickets
		return ticketsView;
	}

	@PostMapping("/ajax/list")
	@ResponseBody
	public List<TicketDto> filterTicketList(@RequestBody TicketsFilter filter) {
		// TODO retrieve all tickets filtered
		return null;
	}

	@PostMapping("/assign")
	public String assignTicket(@ModelAttribute("assignment") AssignmentDto assignmentDto) {
		// TODO save assignment to tracking
		return null;
	}

	@PostMapping("/intervention")
	public String interventionTicket(@ModelAttribute("intervention") InterventionDto interventionDto) {
		// TODO save intervention to tracking
		return null;
	}

	@GetMapping("/close")
	public String closeTicket(@RequestParam("ticketId") Long ticketId,
			@RequestParam("ticketStatus") Status ticketStatus) {

		// TODO save close to tracking
		return null;
	}
}
