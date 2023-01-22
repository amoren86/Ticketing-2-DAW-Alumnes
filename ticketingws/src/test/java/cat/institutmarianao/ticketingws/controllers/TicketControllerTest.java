package cat.institutmarianao.ticketingws.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cat.institutmarianao.ticketingws.controllers.utils.SortParamsUtils;
import cat.institutmarianao.ticketingws.mocks.data.MockActions;
import cat.institutmarianao.ticketingws.mocks.data.MockTickets;
import cat.institutmarianao.ticketingws.mocks.data.MockUsers;
import cat.institutmarianao.ticketingws.mocks.data.MockUtils;
import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Assignment;
import cat.institutmarianao.ticketingws.model.Close;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.ActionDto;
import cat.institutmarianao.ticketingws.model.dto.AssignmentDto;
import cat.institutmarianao.ticketingws.model.dto.CloseDto;
import cat.institutmarianao.ticketingws.model.dto.InterventionDto;
import cat.institutmarianao.ticketingws.model.dto.TicketDto;
import cat.institutmarianao.ticketingws.services.ActionService;
import cat.institutmarianao.ticketingws.services.CompanyService;
import cat.institutmarianao.ticketingws.services.RoomService;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;

@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest({ TicketController.class })
public class TicketControllerTest {

//	private static final String ROOT_CONTENT = "$.content"; //WITH PAGINATION
	private static final String ROOT_CONTENT = "$"; // WITHOUT PAGINATION

	private static final String TICKETS_PATH = "/tickets";
	private static final String TICKETS_FIND_ALL_PATH = TICKETS_PATH + "/find/all";
	private static final String FIND_BY_ID_PATH = TICKETS_PATH + "/find/by/id/{ticketId}";
	private static final String FIND_TRACKING_BY_TICKET_ID = TICKETS_PATH + "/find/tracking/by/id/{ticketId}";
	private static final String TICKETS_SAVE_PATH = TICKETS_PATH + "/save";
	private static final String TICKETS_SAVE_ACTION_PATH = TICKETS_PATH + "/save/action";
	private static final String TICKETS_DELETE_BY_ID_PATH = TICKETS_PATH + "/delete/by/id/{ticketId}";

	private static final Map<String, String> DTO_TO_DB_PROPERTY_MAP = new HashMap<>();

	private static final String CATEGORY = "category";
	private static final String DESCRIPTION = "description";
	private static final String PERFORMER = "performer";
	private static final String OPENING_DATE = "openingDate";

	static {
		DTO_TO_DB_PROPERTY_MAP.put(CATEGORY, "ticket.category");
		DTO_TO_DB_PROPERTY_MAP.put(DESCRIPTION, "ticket.description");
		DTO_TO_DB_PROPERTY_MAP.put(PERFORMER, "performer");
		DTO_TO_DB_PROPERTY_MAP.put(OPENING_DATE, "date");
	}

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TicketService ticketService;

	@MockBean
	private ActionService actionService;

	@MockBean
	private UserService userService;

	@MockBean
	private RoomService roomService;

	@MockBean
	private CompanyService companyService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private ModelMapper modelMapper;

	@Nested
	@DisplayName("GET " + TICKETS_FIND_ALL_PATH)
	class FindAllTickets {

		@Nested
		@DisplayName("Without params")
		class PendingWithoutParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> a1.getDate().compareTo(a2.getDate()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc.perform(get(TICKETS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));

				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().is2xxSuccessful()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With page params")
		class WithPageParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> a1.getDate().compareTo(a2.getDate()));

				int page = 1;
				int size = MockActions.OPENINGS.size() / 2 + 1;

				expectedActions = expectedActions.subList(size, expectedActions.size() - 1);

				// Mock service
				when(actionService.findAll(page, size,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("page", String.valueOf(page))
								.param("size", String.valueOf(size)).accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {

				int page = 1;
				int size = MockActions.OPENINGS.size() / 2 + 1;

				// Mock service
				when(actionService.findAll(page, size,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).param("page", String.valueOf(page))
						.param("size", String.valueOf(size)).accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().is2xxSuccessful()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort category asc params")
		class WithSortCategoryAscParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> a1.getTicket().getCategory().compareTo(a2.getTicket().getCategory()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc").accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort category desc params")
		class WithSortCategoryDescParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> -a1.getTicket().getCategory().compareTo(a2.getTicket().getCategory()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "desc").accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort description asc params")
		class WithSortDescriptionAscParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> a1.getTicket().getDescription().compareTo(a2.getTicket().getDescription()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DESCRIPTION, "asc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", DESCRIPTION, "asc").accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort description desc params")
		class WithSortDescriptionDescParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> -a1.getTicket().getDescription().compareTo(a2.getTicket().getDescription()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DESCRIPTION, "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DESCRIPTION, "desc")
						.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort performer asc params")
		class WithSortPerformerAscParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> a1.getPerformer().getUsername().compareTo(a2.getPerformer().getUsername()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "asc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "asc")
						.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort performer desc params")
		class WithSortPerformerDescParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions,
						(a1, a2) -> -a1.getPerformer().getUsername().compareTo(a2.getPerformer().getUsername()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().is2xxSuccessful()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort date asc params")
		class WithSortDateAscParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> a1.getDate().compareTo(a2.getDate()));

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", OPENING_DATE, "asc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "asc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With sort date desc params")
		class WithSortDateDescParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> -a1.getDate().compareTo(a2.getDate()));

				// Mock service
				when(actionService.findAll(null, null, SortParamsUtils.getOrders(new String[] { "date", "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", "date", "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null, SortParamsUtils.getOrders(new String[] { "date", "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", "date", "desc").accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With multiple sort params")
		class WithSortMultipleParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> {
					int sort1 = a1.getTicket().getCategory().compareTo(a2.getTicket().getCategory());
					if (sort1 != 0) {
						return sort1;
					}
					int sort2 = -a1.getTicket().getDescription().compareTo(a2.getTicket().getDescription());
					if (sort2 != 0) {
						return sort2;
					}
					int sort3 = a1.getPerformer().getUsername().compareTo(a2.getPerformer().getUsername());
					if (sort3 != 0) {
						return sort3;
					}
					return -a1.getDate().compareTo(a2.getDate());
				});

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc",
								DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "desc", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER),
								"asc", "date", "desc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc.perform(get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc")
						.param("sort", DESCRIPTION, "desc").param("sort", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER), "asc")
						.param("sort", DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "desc")
						.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc",
								DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "desc", DTO_TO_DB_PROPERTY_MAP.get(PERFORMER),
								"asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(
						get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc", DESCRIPTION, "desc", PERFORMER, "asc")
								.param("sort", DTO_TO_DB_PROPERTY_MAP.get(OPENING_DATE), "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With page and sort params")
		class WithPageAndSortParams {
			@Test
			@DisplayName("When there is a list of tickets to return...")
			void whenHasResultsShouldReturnAllTicketsSortedByOpeningDateDesc() throws Exception {
				List<Action> expectedActions = new ArrayList<>(MockActions.OPENINGS);
				Collections.sort(expectedActions, (a1, a2) -> {
					int sort1 = a1.getTicket().getCategory().compareTo(a2.getTicket().getCategory());
					if (sort1 != 0) {
						return sort1;
					}
					return a1.getPerformer().getUsername().compareTo(a2.getPerformer().getUsername());
				});

				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc",
								DTO_TO_DB_PROPERTY_MAP.get(DESCRIPTION), "desc", "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(expectedActions));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc", DESCRIPTION, "desc")
								.accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedActions.size())));
				checkJsonTicketsResults(result,
						expectedActions.stream().map(a -> conversionService.convert(a, TicketDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no tickets to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(actionService.findAll(null, null,
						SortParamsUtils.getOrders(new String[] { DTO_TO_DB_PROPERTY_MAP.get(CATEGORY), "asc" }),
						Type.OPENING, null, null, null, null, null)).thenReturn(page(new ArrayList<Action>()));

				// Call controller and asserts
				mockMvc.perform(get(TICKETS_FIND_ALL_PATH).param("sort", CATEGORY, "asc")
						.param("sort", PERFORMER, "asc").accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().is2xxSuccessful()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		// TODO - Filter by category
		// TODO - Filter by description
		// TODO - Filter by performer
		// TODO - Filter by openingDate
		// TODO - Filter by all
		// TODO - page + sort + filter by all
	}

	@Nested
	@DisplayName("GET " + FIND_BY_ID_PATH)
	class FindById {
		@Test
		@DisplayName("When there is a ticket with this id...")
		void whenHasResultShouldReturnTheTicket() throws Exception {

			int random = new Random().nextInt(MockTickets.TICKETS.size());
			long id = new Random().nextInt(Integer.MAX_VALUE);

			Ticket expectedTicket = MockTickets.TICKETS.get(random);
			Optional<Ticket> returnedTicket = Optional.of(expectedTicket);

			// Mock service
			when(ticketService.findById(id)).thenReturn(returnedTicket);

			// Call controller and asserts
			ResultActions result = mockMvc.perform(get(FIND_BY_ID_PATH, id).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isOk());

			checkJsonResult(result, conversionService.convert(expectedTicket, TicketDto.class));
		}

		@Test
		@DisplayName("When there is no ticket with this id...")
		void whenHasNoResultShouldReturnNull() throws Exception {

			long id = MockUtils.getRandomId();
			Optional<Ticket> returnedTicket = Optional.ofNullable(null);

			// Mock service
			when(ticketService.findById(id)).thenReturn(returnedTicket);

			// Call controller and asserts
			mockMvc.perform(get(FIND_BY_ID_PATH, id).accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().is2xxSuccessful()).andExpect(content().string("null"));
		}
	}

	@Nested
	@DisplayName("GET " + FIND_TRACKING_BY_TICKET_ID)
	class findTrackingByTicketId {
		@Test
		@DisplayName("When there is a tracking for with this ticket id...")
		void whenHasResultShouldReturnTheTracking() throws Exception {

			long id = MockUtils.getRandomId();

			List<Action> tracking = MockActions.TRACKING1;
			tracking.forEach(a -> a.setId(MockUtils.getRandomId()));

			Ticket ticket = tracking.get(0).getTicket();
			ticket.setId(id);

			List<ActionDto> expectedTracking = tracking.stream().map(a -> conversionService.convert(a, ActionDto.class))
					.toList();

			// Mock service
			when(actionService.findByTicketId(id)).thenReturn(tracking);

			// Call controller and asserts
			ResultActions result = mockMvc
					.perform(get(FIND_TRACKING_BY_TICKET_ID, id).accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().isOk());

			checkJsonResults(result, expectedTracking);
		}

		@Test
		@DisplayName("When there is no tracking for with this ticket id...")
		void whenHasNoResultShouldReturnNull() throws Exception {

			long id = MockUtils.getRandomId();

			// Mock service
			when(actionService.findByTicketId(id)).thenReturn(new ArrayList<Action>());

			// Call controller and asserts
			mockMvc.perform(get(FIND_TRACKING_BY_TICKET_ID, id).accept(MediaType.APPLICATION_JSON)).andDo(print())
					.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$", hasSize(0)));
		}

	}

	@Nested
	@DisplayName("POST " + TICKETS_SAVE_PATH)
	class Save {
		@Test
		@DisplayName("When save a ticket...")
		void whenSaveShouldReturnTheTicket() throws Exception {
			// Prepare data
			Ticket ticket = MockTickets.generateRandomTicket();
			User performer = MockUsers.generateRandomEmployee();
			ticket.getTracking().get(0).setPerformer(performer);

			TicketDto ticketDto = conversionService.convert(ticket, TicketDto.class);

			Ticket expectedTicket = new Ticket();
			modelMapper.map(ticket, expectedTicket);
			expectedTicket.setId(MockUtils.getRandomId());

			TicketDto expectedTicketDto = conversionService.convert(expectedTicket, TicketDto.class);

			// Mock service
			when(ticketService.save(ticket)).thenReturn(expectedTicket);

			// Call controller and asserts
			ResultActions result = mockMvc
					.perform(post(TICKETS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.content(objectMapper.writeValueAsString(ticketDto)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is2xxSuccessful());
			checkJsonResult(result, expectedTicketDto);
		}
	}

	@Nested
	@DisplayName("POST " + TICKETS_SAVE_ACTION_PATH)
	class SaveAction {
		@Test
		@DisplayName("When save an assignment...")
		void whenSaveAssignmentShouldReturnTheAssignment() throws Exception {
			// Prepare data
			Ticket dbTicket = MockTickets.generateRandomTicket();
			User performer = MockUsers.generateRandomEmployee();
			dbTicket.getTracking().get(0).setPerformer(performer);
			dbTicket.setId(MockUtils.getRandomId());

			Assignment assignmentToSave = (Assignment) MockActions.generateRandomAction(dbTicket, new Assignment());

			AssignmentDto assignmentDtoToSave = (AssignmentDto) conversionService.convert(assignmentToSave,
					ActionDto.class);

			Assignment dbSavedAssignment = new Assignment();
			modelMapper.map(assignmentToSave, dbSavedAssignment);
			dbSavedAssignment.setId(MockUtils.getRandomId());

			ActionDto dbSavedActionDto = conversionService.convert(dbSavedAssignment, ActionDto.class);

			// Mock services
			when(actionService.save(assignmentToSave)).thenReturn(dbSavedAssignment);
			when(userService.getUserByUsername(assignmentDtoToSave.getPerformer()))
					.thenReturn(dbSavedAssignment.getPerformer());
			when(userService.getUserByUsername(assignmentDtoToSave.getTechnician()))
					.thenReturn(dbSavedAssignment.getTechnician());
			when(ticketService.findById(dbTicket.getId())).thenReturn(Optional.of(dbTicket));

			// Call controller and asserts
			ResultActions result = mockMvc.perform(post(TICKETS_SAVE_ACTION_PATH)
					.contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
					.content(objectMapper.writeValueAsString(assignmentDtoToSave)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is2xxSuccessful());
			checkJsonResult(result, dbSavedActionDto);
		}

		@Test
		@DisplayName("When save an intervention...")
		void whenSaveInterventionShouldReturnTheIntervention() throws Exception {
			// Prepare data
			Ticket dbTicket = MockTickets.generateRandomTicket();
			User performer = MockUsers.generateRandomEmployee();
			dbTicket.getTracking().get(0).setPerformer(performer);
			dbTicket.setId(MockUtils.getRandomId());

			dbTicket.getTracking().add(MockActions.generateRandomAction(dbTicket, new Assignment()));

			Intervention interventionToSave = (Intervention) MockActions.generateRandomAction(dbTicket,
					new Intervention());

			InterventionDto interventionDtoToSave = (InterventionDto) conversionService.convert(interventionToSave,
					ActionDto.class);

			Intervention dbSavedIntervention = new Intervention();
			modelMapper.map(interventionToSave, dbSavedIntervention);
			dbSavedIntervention.setId(MockUtils.getRandomId());

			InterventionDto dbSavedActionDto = (InterventionDto) conversionService.convert(dbSavedIntervention,
					ActionDto.class);

			// Mock services
			when(actionService.save(interventionToSave)).thenReturn(dbSavedIntervention);
			when(userService.getUserByUsername(interventionDtoToSave.getPerformer()))
					.thenReturn(dbSavedIntervention.getPerformer());
			when(ticketService.findById(dbTicket.getId())).thenReturn(Optional.of(dbTicket));

			// Call controller and asserts
			ResultActions result = mockMvc.perform(post(TICKETS_SAVE_ACTION_PATH)
					.contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
					.content(objectMapper.writeValueAsString(interventionDtoToSave)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is2xxSuccessful());
			checkJsonResult(result, dbSavedActionDto);
		}

		@Test
		@DisplayName("When save a close...")
		void whenSaveCloseShouldReturnTheClose() throws Exception {
			// Prepare data
			Ticket dbTicket = MockTickets.generateRandomTicket();
			User performer = MockUsers.generateRandomEmployee();
			dbTicket.getTracking().get(0).setPerformer(performer);
			dbTicket.setId(MockUtils.getRandomId());

			dbTicket.getTracking().add(MockActions.generateRandomAction(dbTicket, new Assignment()));
			dbTicket.getTracking().add(MockActions.generateRandomAction(dbTicket, new Intervention()));

			Close closeToSave = (Close) MockActions.generateRandomAction(dbTicket, new Close());

			CloseDto closeDtoToSave = (CloseDto) conversionService.convert(closeToSave, ActionDto.class);

			Close dbSavedClose = new Close();
			modelMapper.map(closeToSave, dbSavedClose);
			dbSavedClose.setId(MockUtils.getRandomId());

			CloseDto dbSavedActionDto = (CloseDto) conversionService.convert(dbSavedClose, ActionDto.class);

			// Mock services
			when(actionService.save(closeToSave)).thenReturn(dbSavedClose);
			when(userService.getUserByUsername(closeDtoToSave.getPerformer())).thenReturn(dbSavedClose.getPerformer());
			when(ticketService.findById(dbTicket.getId())).thenReturn(Optional.of(dbTicket));

			// Call controller and asserts
			ResultActions result = mockMvc.perform(post(TICKETS_SAVE_ACTION_PATH)
					.contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
					.content(objectMapper.writeValueAsString(closeDtoToSave)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is2xxSuccessful());
			checkJsonResult(result, dbSavedActionDto);
		}
	}

	@Nested
	@DisplayName("DELETE " + TICKETS_DELETE_BY_ID_PATH)
	class DeleteById {
		@Test
		@DisplayName("When delete by id...")
		void shoudReturn2xxOk() throws Exception {
			long id = MockUtils.getRandomId();
			mockMvc.perform(delete(TICKETS_DELETE_BY_ID_PATH, id).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is2xxSuccessful());
		}
	}

	private void checkJsonTicketsResults(ResultActions result, List<TicketDto> ticketsDto) throws Exception {
		for (int i = 0; i < ticketsDto.size(); i++) {
			TicketDto action = ticketsDto.get(i);
			result = result.andExpect(
					jsonPath(ROOT_CONTENT + "[" + i + "].category", equalTo(action.getCategory().toString())));
			result = result
					.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].description", equalTo(action.getDescription())));
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].performer", equalTo(action.getPerformer())));
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].openingDate",
					equalTo(objectMapper.getDateFormat().format(action.getOpeningDate()))));
		}
	}

	private void checkJsonResult(ResultActions result, TicketDto ticketDto) throws Exception {
		if (ticketDto.getId() != null) {
			result = result.andExpect(jsonPath("$.id", equalTo(ticketDto.getId().intValue())));
		}
		result = result.andExpect(jsonPath("$.category", equalTo(ticketDto.getCategory().toString())));
		result = result.andExpect(jsonPath("$.description", equalTo(ticketDto.getDescription())));
		result = result.andExpect(jsonPath("$.performer", equalTo(ticketDto.getPerformer())));
		result = result.andExpect(
				jsonPath("$.openingDate", equalTo(objectMapper.getDateFormat().format(ticketDto.getOpeningDate()))));
	}

	private void checkJsonResults(ResultActions result, List<ActionDto> actionsDto) throws Exception {
		for (int i = 0; i < actionsDto.size(); i++) {
			ActionDto actionDto = actionsDto.get(i);
			result = result.andExpect(jsonPath("$[" + i + "].id", equalTo(actionDto.getId().intValue())));
			result = result.andExpect(jsonPath("$[" + i + "].type", equalTo(actionDto.getType().toString())));
			result = result.andExpect(jsonPath("$[" + i + "].performer", equalTo(actionDto.getPerformer())));
			result = result.andExpect(
					jsonPath("$[" + i + "].date", equalTo(objectMapper.getDateFormat().format(actionDto.getDate()))));
			result = result.andExpect(jsonPath("$[" + i + "].ticketId", equalTo(actionDto.getTicketId().intValue())));
			if (actionDto instanceof AssignmentDto assignment) {
				result = result.andExpect(jsonPath("$[" + i + "].technician", equalTo(assignment.getTechnician())));
				result = result.andExpect(jsonPath("$[" + i + "].priority", equalTo(assignment.getPriority())));
			} else if (actionDto instanceof InterventionDto intervention) {
				result = result.andExpect(jsonPath("$[" + i + "].hours", equalTo(intervention.getHours())));
				result = result.andExpect(jsonPath("$[" + i + "].description", equalTo(intervention.getDescription())));
			}
		}
	}

	private void checkJsonResult(ResultActions result, ActionDto actionDto) throws Exception {
		if (actionDto.getId() != null) {
			result = result.andExpect(jsonPath("$.id", equalTo(actionDto.getId().intValue())));
		}
		result = result.andExpect(jsonPath("$.type", equalTo(actionDto.getType().toString())));
		result = result.andExpect(jsonPath("$.performer", equalTo(actionDto.getPerformer())));
		result = result
				.andExpect(jsonPath("$.date", equalTo(objectMapper.getDateFormat().format(actionDto.getDate()))));
		result = result.andExpect(jsonPath("$.ticketId", equalTo(actionDto.getTicketId().intValue())));
		if (actionDto instanceof AssignmentDto assignment) {
			result = result.andExpect(jsonPath("$.technician", equalTo(assignment.getTechnician())));
			result = result.andExpect(jsonPath("$.priority", equalTo(assignment.getPriority())));
		} else if (actionDto instanceof InterventionDto intervention) {
			result = result.andExpect(jsonPath("$.hours", equalTo(intervention.getHours())));
			result = result.andExpect(jsonPath("$.description", equalTo(intervention.getDescription())));
		}
	}

//	private Page<Action> page(List<Action> content) {
//		return new PageImpl<>(content);
//	}

	private List<Action> page(List<Action> content) {
		return content;
	}
}
