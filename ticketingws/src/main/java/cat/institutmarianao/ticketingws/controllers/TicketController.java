package cat.institutmarianao.ticketingws.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.ticketingws.controllers.utils.SortParamsUtils;
import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.Ticket.Category;
import cat.institutmarianao.ticketingws.model.Ticket.Status;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.ActionDto;
import cat.institutmarianao.ticketingws.model.dto.TicketDto;
import cat.institutmarianao.ticketingws.services.ActionService;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;
import cat.institutmarianao.ticketingws.validation.groups.OnActionCreate;
import cat.institutmarianao.ticketingws.validation.groups.OnTicketCreate;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {
	private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";

	private static final Map<String, String> DTO_TO_DB_PROPERTY_MAP = new HashMap<>();

	@Autowired
	private UserService userService;
	@Autowired
	private TicketService ticketService;

	@Autowired
	private ActionService actionService;

	@Autowired
	private ConversionService conversionService;

	static {
		DTO_TO_DB_PROPERTY_MAP.put("category", "ticket.category");
		DTO_TO_DB_PROPERTY_MAP.put("description", "ticket.description");
		DTO_TO_DB_PROPERTY_MAP.put("openingDate", "date");
	}

	@GetMapping("/find/all")
	public List<TicketDto> findAll(@RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
			@RequestParam(value = "size", required = false) @Positive Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "date,asc") String[] sortDtoFields,
			@RequestParam(value = "status", required = false) Status status,
			@RequestParam(value = "reportedBy", required = false) String reportedBy,
			@RequestParam(value = "category", required = false) Category category,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date to) {

		return findAllOpenings(page, size, sortDtoFields, status, reportedBy, category, from, to);
	}

	@GetMapping("/find/all/PENDING")
	public List<TicketDto> findAllPending(@RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
			@RequestParam(value = "size", required = false) @Positive Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "date,asc") String[] sortDtoFields,
			@RequestParam(value = "reportedBy", required = false) String reportedBy,
			@RequestParam(value = "category", required = false) Category category,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date to) {

		return findAllOpenings(page, size, sortDtoFields, Status.PENDING, reportedBy, category, from, to);
	}

	@GetMapping("/find/all/IN_PROCESS")
	public List<TicketDto> findAllInProcess(
			@RequestParam(value = "page", required = false) @PositiveOrZero Integer page,
			@RequestParam(value = "size", required = false) @Positive Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "date,asc") String[] sortDtoFields,
			@RequestParam(value = "reportedBy", required = false) String reportedBy,
			@RequestParam(value = "category", required = false) Category category,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date to) {

		return findAllOpenings(page, size, sortDtoFields, Status.IN_PROCESS, reportedBy, category, from, to);
	}

	// TODO findAllHISTORICAL

	@GetMapping("/find/by/id/{ticketId}")
	public Optional<TicketDto> findById(@PathVariable("ticketId") @Positive Long ticketId) {
		Optional<Ticket> opTicket = ticketService.findById(ticketId);
		if (opTicket.isEmpty()) {
			return Optional.ofNullable(null);
		}
		return Optional.of(conversionService.convert(opTicket.get(), TicketDto.class));
	}

	@GetMapping("/find/tracking/by/id/{ticketId}")
	public Iterable<ActionDto> findTrackingByTicketId(@PathVariable("ticketId") @Positive Long ticketId) {
		return actionService.findByTicketId(ticketId).stream().map(a -> conversionService.convert(a, ActionDto.class))
				.toList();
	}

	@PostMapping("/save")
	@Validated(OnTicketCreate.class)
	public TicketDto save(@RequestBody @Valid TicketDto ticketDto) {
		return conversionService.convert(ticketService.save(conversionService.convert(ticketDto, Ticket.class)),
				TicketDto.class);
	}

	@PostMapping("/save/action")
	@Validated(OnActionCreate.class)
	public ActionDto saveAction(@RequestBody @Valid ActionDto actionDto) {
		Action action0 = conversionService.convert(actionDto, Action.class);
		Action action = actionService.save(action0);
		return conversionService.convert(action, ActionDto.class);
	}

	@DeleteMapping("/delete/by/id/{ticket_id}")
	public void deleteById(@PathVariable("ticket_id") @Positive Long ticketId) {
		ticketService.deleteById(ticketId);
	}

	private List<TicketDto> findAllOpenings(@PositiveOrZero Integer page, @Positive Integer size,
			String[] sortDtoFields, Status status, String reportedBy, Category category, Date from, Date to) {

		String[] sortDbFields = SortParamsUtils.dtoToDbFields(DTO_TO_DB_PROPERTY_MAP, sortDtoFields);
		List<Order> orders = SortParamsUtils.getOrders(sortDbFields);

		User performer = null;
		if (reportedBy != null) {
			performer = userService.getUserByUsername(reportedBy);
		}

		return actionService.findAll(page, size, orders, Type.OPENING, status, performer, category, from, to).stream()
				.map(a -> conversionService.convert(a, TicketDto.class)).collect(Collectors.toList());
	}
}
