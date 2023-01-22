package cat.institutmarianao.ticketingws.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import cat.institutmarianao.ticketingws.mocks.data.MockActions;
import cat.institutmarianao.ticketingws.mocks.data.MockTickets;
import cat.institutmarianao.ticketingws.mocks.data.MockUsers;
import cat.institutmarianao.ticketingws.mocks.data.MockUtils;
import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Assignment;
import cat.institutmarianao.ticketingws.model.Close;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.repositories.ActionRepository;

@SpringBootTest
public class ActionServiceTest {

	@MockBean
	private ActionRepository actionRepository;

	@Autowired
	private ActionService actionService;

	@MockBean
	private TicketService ticketService;

	@MockBean
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ModelMapper modelMapper;

	private static final Map<Type, List<Type>> ACTIONS_X_PREVIOUS_ACTIONS = new EnumMap<>(Type.class);

	static {
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.OPENING, new ArrayList<>());
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.ASSIGNMENT, Arrays.asList(Type.OPENING));
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.INTERVENTION, Arrays.asList(Type.ASSIGNMENT, Type.INTERVENTION));
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.CLOSE, Arrays.asList(Type.OPENING, Type.ASSIGNMENT, Type.INTERVENTION));
	}

	@Nested
	@DisplayName("findByType")
	class FindByType {
		@SuppressWarnings("unchecked")
		@Test
		@DisplayName("OPENING")
		void whenTypeOpeningShouldReturnAllOpenings() throws Exception {

			// Mock service
			when(actionRepository.findAll(any(Specification.class), any(Sort.class)))
					.thenReturn(page(MockActions.OPENINGS));

			// Call service and asserts
			List<Action> returnedActions = actionService.findAll(null, null, new ArrayList<Order>(), Type.OPENING, null,
					null, null, null, null);

			assertThat(returnedActions).hasSameElementsAs(MockActions.OPENINGS);
		}

		// TODO Sorted
	}

	// TODO Find by ticket id

	@Nested
	@DisplayName("save")
	class Save {
		private static final String ERROR_TRACKING_BEFORE_CLOSE = "error.Tracking.before.close";
		private static final String ERROR_TRACKING_NOT_ALLOWED_CODE = "error.Tracking.action.not.allowed";
		private static final String ERROR_TRACKING_WRONG_DATE = "error.Tracking.wrong.date";
		private static final String ERROR_NOT_OPENING_USER = "error.Performer.role.not.the.opening.employee";
		private static final String ERROR_NOT_TECHNICIAN_OF_ANY_INTERVENTION = "error.Performer.role.not.the.technician.of.any.intervention";

		@Nested
		@DisplayName("OPENING")
		class SaveOpening {
			@Test
			@DisplayName("after opening should throw ConstraintViolationException")
			void afterOpeningShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.OPENING, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.OPENING), Type.OPENING };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				Opening secondOpening = (Opening) MockActions.generateRandomAction(ticket, new Opening());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondOpening.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondOpening.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondOpening);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}

			@Test
			@DisplayName("after assignment should throw ConstraintViolationException")
			void afterAssignmentShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.OPENING, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.OPENING), Type.ASSIGNMENT };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

				Opening secondOpening = (Opening) MockActions.generateRandomAction(ticket, new Opening());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondOpening.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondOpening.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondOpening);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}

			@Test
			@DisplayName("after intervention should throw ConstraintViolationException")
			void afterInterventionShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.OPENING, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.OPENING), Type.INTERVENTION };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

				Opening secondOpening = (Opening) MockActions.generateRandomAction(ticket, new Opening());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondOpening.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondOpening.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondOpening);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}

			@Test
			@DisplayName("after close should throw ConstraintViolationException")
			void afterCloseShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

				Opening secondOpening = (Opening) MockActions.generateRandomAction(ticket, new Opening());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondOpening.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondOpening.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondOpening);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}
		}

		@Nested
		@DisplayName("ASSIGNMENT")
		class SaveAssignment {
			@Test
			@DisplayName("after opening should return the Assignment")
			void afterOpeningShouldReturnTheAssignment() throws Exception {
				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				Assignment assignment = (Assignment) MockActions.generateRandomAction(ticket, new Assignment());
				Assignment expectedAssignment = new Assignment();
				modelMapper.map(assignment, expectedAssignment);
				expectedAssignment.setId(MockUtils.getRandomId());

				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(assignment.getPerformer().getUsername()))
						.thenReturn(Optional.of(assignment.getPerformer()));
				when(actionRepository.save(assignment)).thenReturn(expectedAssignment);

				// Call service and asserts
				Action returnedAction = actionService.save(assignment);
				assertThat(returnedAction).isInstanceOf(Assignment.class).isEqualTo(expectedAssignment);
			}

			@Test
			@DisplayName("after assignment should throw ConstraintViolationException")
			void afterAssignmentShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.ASSIGNMENT, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.ASSIGNMENT),
						Type.ASSIGNMENT };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

				Assignment secondAssignment = (Assignment) MockActions.generateRandomAction(ticket, new Assignment());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondAssignment.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondAssignment.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondAssignment);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}

			@Test
			@DisplayName("after intervention should throw ConstraintViolationException")
			void afterInterventionShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.ASSIGNMENT, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.ASSIGNMENT),
						Type.INTERVENTION };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

				Assignment secondAssignment = (Assignment) MockActions.generateRandomAction(ticket, new Assignment());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondAssignment.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondAssignment.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondAssignment);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}

			@Test
			@DisplayName("after close should throw ConstraintViolationException")
			void afterCloseShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

				Assignment secondAssignment = (Assignment) MockActions.generateRandomAction(ticket, new Assignment());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(secondAssignment.getPerformer().getUsername()))
						.thenReturn(Optional.of(secondAssignment.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(secondAssignment);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}

			@Test
			@DisplayName("date before opening date close should throw ConstraintViolationException")
			void dateBeforeOpeningDateShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_WRONG_DATE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				Assignment assignment = (Assignment) MockActions.generateRandomAction(ticket, new Assignment());

				Date newestDate = new Date(assignment.getDate().getTime() + 1000);
				ticket.getTracking().get(0).setDate(newestDate);

				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(assignment.getPerformer().getUsername()))
						.thenReturn(Optional.of(assignment.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(assignment);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}
		}

		@Nested
		@DisplayName("INTERVENTION")
		class SaveIntervention {
			@Test
			@DisplayName("after opening should throw ConstraintViolationException")
			void afterOpeningShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0, Type.INTERVENTION, ACTIONS_X_PREVIOUS_ACTIONS.get(Type.INTERVENTION),
						Type.OPENING };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_NOT_ALLOWED_CODE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());

				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(intervention);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}

			@Test
			@DisplayName("after assignment should return the Intervention")
			void afterAssignmentShouldReturnTheIntervention() throws Exception {
				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());
				Intervention expectedIntervention = new Intervention();
				modelMapper.map(intervention, expectedIntervention);
				expectedIntervention.setId(MockUtils.getRandomId());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));
				when(actionRepository.save(intervention)).thenReturn(expectedIntervention);

				// Call service and asserts
				Action returnedAction = actionService.save(intervention);
				assertThat(returnedAction).isInstanceOf(Intervention.class).isEqualTo(expectedIntervention);
			}

			@Test
			@DisplayName("after intervention should return the Intervention")
			void afterInterventionShouldReturnTheIntervention() throws Exception {
				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());
				Intervention expectedIntervention = new Intervention();
				modelMapper.map(intervention, expectedIntervention);
				expectedIntervention.setId(MockUtils.getRandomId());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));
				when(actionRepository.save(intervention)).thenReturn(expectedIntervention);

				// Call service and asserts
				Action returnedAction = actionService.save(intervention);
				assertThat(returnedAction).isInstanceOf(Intervention.class).isEqualTo(expectedIntervention);
			}

			@Test
			@DisplayName("after close should throw ConstraintViolationException")
			void afterCloseShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());
				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(intervention);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

			}

			@Test
			@DisplayName("date before assignment date close should throw ConstraintViolationException")
			void dateBeforeAssignmentDateShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_WRONG_DATE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());

				Date newestDate = new Date(intervention.getDate().getTime() + 1000);
				ticket.getTracking().get(0).setDate(newestDate);

				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(intervention);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}

			@Test
			@DisplayName("date before intervention date close should throw ConstraintViolationException")
			void dateBeforeInterventionDateShouldThrowConstraintViolationException() throws Exception {
				Object[] params = { 0 };
				String errorMsg = messageSource.getMessage(ERROR_TRACKING_WRONG_DATE, params,
						LocaleContextHolder.getLocale());

				Ticket ticket = MockTickets.generateRandomTicket();
				ticket.setId(MockUtils.getRandomId());
				ticket.getTracking().get(0).setTicket(ticket);

				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
				ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

				Intervention intervention = (Intervention) MockActions.generateRandomAction(ticket, new Intervention());

				Date newestDate = new Date(intervention.getDate().getTime() + 1000);
				ticket.getTracking().get(0).setDate(newestDate);

				// Mock service
				when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId())).thenReturn(ticket.getTracking());

				when(userService.findByUsername(intervention.getPerformer().getUsername()))
						.thenReturn(Optional.of(intervention.getPerformer()));

				// Call service and asserts
				assertThatThrownBy(() -> {
					actionService.save(intervention);
				}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
			}
		}

		@Nested
		@DisplayName("CLOSE")
		class SaveClose {
			@Nested
			@DisplayName("Performed by the employee in the opening")
			class OpeningEmployee {
				@Test
				@DisplayName("after opening should return the Close")
				void afterOpeningShouldReturnTheClose() throws Exception {
					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = ticket.getTracking().get(0).getPerformer();

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));

					// Call service and asserts
					Action returnedAction = actionService.save(close);
					assertThat(returnedAction).isInstanceOf(Close.class).isEqualTo(expectedClose);
				}

				@Test
				@DisplayName("after assignment should return the Close")
				void afterAssignmentShouldReturnTheClose() throws Exception {
					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = ticket.getTracking().get(0).getPerformer();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					Action returnedAction = actionService.save(close);
					assertThat(returnedAction).isInstanceOf(Close.class).isEqualTo(expectedClose);
				}

				@Test
				@DisplayName("after intervention should return the Close")
				void afterInterventionShouldReturnTheClose() throws Exception {
					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = ticket.getTracking().get(0).getPerformer();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					Action returnedAction = actionService.save(close);
					assertThat(returnedAction).isInstanceOf(Close.class).isEqualTo(expectedClose);
				}

				@Test
				@DisplayName("after close should throw ConstraintViolationException")
				void afterCloseShouldThrowConstraintViolationException() throws Exception {
					Object[] params = { 0 };
					String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = ticket.getTracking().get(0).getPerformer();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}
			}

			@Nested
			@DisplayName("Performed by the employee not in the opening")
			class NotOpeningEmployee {
				@Test
				@DisplayName("after opening should throw ConstraintViolationException")
				void afterOpeningShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_OPENING_USER, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = MockUsers.generateRandomEmployee();

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));

					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after assignment should throw ConstraintViolationException")
				void afterAssignmentShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_OPENING_USER, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = MockUsers.generateRandomEmployee();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after intervention should throw ConstraintViolationException")
				void afterInterventionShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_OPENING_USER, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = MockUsers.generateRandomEmployee();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after close should throw ConstraintViolationException")
				void afterCloseShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_OPENING_USER, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User opener = MockUsers.generateRandomEmployee();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(opener);

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);

				}
			}

			@Nested
			@DisplayName("Performed by a technician with interventions")
			class TechnicianWithInterventions {
				@Test
				@DisplayName("after intervention should return the Close")
				void afterInterventionShouldReturnTheClose() throws Exception {
					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);

					User technician = MockUsers.generateRandomTechnician();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Action intervention = MockActions.generateRandomAction(ticket, new Intervention());
					intervention.setPerformer(technician);
					ticket.getTracking().add(0, intervention);

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					Action returnedAction = actionService.save(close);
					assertThat(returnedAction).isInstanceOf(Close.class).isEqualTo(expectedClose);
				}

				@Test
				@DisplayName("after close should throw ConstraintViolationException")
				void afterCloseShouldThrowConstraintViolationException() throws Exception {
					Object[] params = { 0 };
					String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);

					User technician = MockUsers.generateRandomTechnician();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					Action intervention = MockActions.generateRandomAction(ticket, new Intervention());
					intervention.setPerformer(technician);
					ticket.getTracking().add(0, intervention);

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}
			}

			@Nested
			@DisplayName("Performed by a technician without interventions")
			class TechnicianWithoutInterventions {
				@Test
				@DisplayName("after opening should throw ConstraintViolationException")
				void afterOpeningShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_TECHNICIAN_OF_ANY_INTERVENTION, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User technician = MockUsers.generateRandomTechnician();

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));

					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after assignment should throw ConstraintViolationException")
				void afterAssignmentShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_TECHNICIAN_OF_ANY_INTERVENTION, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User technician = MockUsers.generateRandomTechnician();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after intervention should throw ConstraintViolationException")
				void afterInterventionShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_TECHNICIAN_OF_ANY_INTERVENTION, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User technician = MockUsers.generateRandomTechnician();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}

				@Test
				@DisplayName("after close should throw ConstraintViolationException")
				void afterCloseShouldThrowConstraintViolationException() throws Exception {
					String errorMsg = messageSource.getMessage(ERROR_NOT_TECHNICIAN_OF_ANY_INTERVENTION, null,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);
					User technician = MockUsers.generateRandomTechnician();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));
					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(technician);

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}
			}

			@Nested
			@DisplayName("Performed by a supervisor")
			class Supervisor {
				@Test
				@DisplayName("after intervention should return the Close")
				void afterInterventionShouldReturnTheClose() throws Exception {
					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);

					User supervisor = MockUsers.generateRandomSupervisor();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Action intervention = MockActions.generateRandomAction(ticket, new Intervention());
					intervention.setPerformer(supervisor);
					ticket.getTracking().add(0, intervention);

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Intervention()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(supervisor);

					Close expectedClose = new Close();
					modelMapper.map(close, expectedClose);
					expectedClose.setId(MockUtils.getRandomId());
					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));
					when(actionRepository.save(close)).thenReturn(expectedClose);

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					Action returnedAction = actionService.save(close);
					assertThat(returnedAction).isInstanceOf(Close.class).isEqualTo(expectedClose);
				}

				@Test
				@DisplayName("after close should throw ConstraintViolationException")
				void afterCloseShouldThrowConstraintViolationException() throws Exception {
					Object[] params = { 0 };
					String errorMsg = messageSource.getMessage(ERROR_TRACKING_BEFORE_CLOSE, params,
							LocaleContextHolder.getLocale());

					Ticket ticket = MockTickets.generateRandomTicket();
					ticket.setId(MockUtils.getRandomId());
					ticket.getTracking().get(0).setTicket(ticket);

					User supervisor = MockUsers.generateRandomSupervisor();

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Assignment()));

					Action intervention = MockActions.generateRandomAction(ticket, new Intervention());
					intervention.setPerformer(supervisor);
					ticket.getTracking().add(0, intervention);

					ticket.getTracking().add(0, MockActions.generateRandomAction(ticket, new Close()));

					Close close = (Close) MockActions.generateRandomAction(ticket, new Close());
					close.setPerformer(supervisor);

					// Mock service
					when(actionRepository.findByTicketIdOrderByDateDesc(ticket.getId()))
							.thenReturn(ticket.getTracking());

					when(userService.findByUsername(close.getPerformer().getUsername()))
							.thenReturn(Optional.of(close.getPerformer()));

					when(ticketService.findById(ticket.getId())).thenReturn(Optional.of(ticket));
					// Call service and asserts
					assertThatThrownBy(() -> {
						actionService.save(close);
					}).isInstanceOf(ConstraintViolationException.class).hasMessageContaining(errorMsg);
				}
			}
		}
	}

//	private Page<Action> page(List<Action> content) {
//		return new PageImpl<>(content);
//	}

	private List<Action> page(List<Action> content) {
		return content;
	}
}
