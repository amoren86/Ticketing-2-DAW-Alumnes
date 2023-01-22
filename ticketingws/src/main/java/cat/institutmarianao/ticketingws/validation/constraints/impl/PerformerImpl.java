package cat.institutmarianao.ticketingws.validation.constraints.impl;

import java.util.Arrays;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;
import cat.institutmarianao.ticketingws.model.dto.ActionDto;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;
import cat.institutmarianao.ticketingws.validation.constraints.Performer;

public class PerformerImpl implements ConstraintValidator<Performer, Object> {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;

	@Autowired
	private TicketService ticketService;

	private String property;
	private Role[] openingRoles;
	private Role[] assignmentRoles;
	private Role[] interventionRoles;
	private Role[] closeRoles;

	private boolean openingUserCloseAllowed;
	private boolean interventionTechnicianCloseAllowed;

	@Override
	public void initialize(Performer constraintAnnotation) {
		property = constraintAnnotation.property();
		openingRoles = constraintAnnotation.opening();
		assignmentRoles = constraintAnnotation.assignment();
		interventionRoles = constraintAnnotation.intervention();
		closeRoles = constraintAnnotation.close();
		openingUserCloseAllowed = constraintAnnotation.openingUserCloseAllowed();
		interventionTechnicianCloseAllowed = constraintAnnotation.interventionTechnicianCloseAllowed();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		if (messageSource == null || userService == null) {
			return true; // We have no context, so is not under @Valid annotation
		}

		Object performerValue = new BeanWrapperImpl(object).getPropertyValue(property);

		Action.Type actionType;
		Long ticketId;
		if (object instanceof Action action) {
			actionType = action.getType();
			ticketId = action.getTicket().getId();
		} else if (object instanceof ActionDto actionDto) {
			actionType = actionDto.getType();
			ticketId = actionDto.getTicketId();
		} else {
			addError(context, "error.Performer.annotation.should.be.attached.to.Action.or.ActionDto.types", property);
			return false;
		}

		String username;
		if (performerValue instanceof String usernameProperty) {
			username = usernameProperty;
		} else if (performerValue instanceof User user) {
			username = user.getUsername();
		} else {
			addError(context, "error.Performer.property.is.not.instance.of.user.or.string", property);
			return false;
		}

		if (username == null) {
			return true; // Depends on @NotNull annotation. If allows nulls, performer is OK being null
		}

		Optional<User> dbPerformer = userService.findByUsername(username);
		if (dbPerformer.isEmpty()) {
			addError(context, "error.Performer.not.found.in.db.with.such.username", username);
			return false;
		}
		User performer = dbPerformer.get();

		switch (actionType) {
		case OPENING:
			return validate(context, actionType, performer, openingRoles);
		case ASSIGNMENT:
			return validate(context, actionType, performer, assignmentRoles);
		case INTERVENTION:
			return validate(context, actionType, performer, interventionRoles);
		case CLOSE:
			Optional<Ticket> opTicket = ticketService.findById(ticketId);
			if (opTicket.isEmpty()) {
				addError(context, "error.Performer.ticket.not.found", ticketId);
				return false;
			}
			switch (performer.getRole()) {
			case SUPERVISOR:
				return validate(context, actionType, performer, closeRoles);
			case TECHNICIAN:
				return interventionTechnicianCloseAllowed && checkHasIntervention(context, opTicket.get(), performer);
			case EMPLOYEE:
				return openingUserCloseAllowed && checkOpener(context, opTicket.get(), performer);
			default:
				break;
			}
			break;
		default:
			break;
		}
		return false;
	}

	private boolean validate(ConstraintValidatorContext context, Type type, User performer, Role[] roles) {
		for (Role role : roles) {
			if (role.equals(performer.getRole())) {
				return true;
			}
		}
		addError(context, "error.Performer.role.invalid.for.action", type, Arrays.toString(roles), performer.getRole());
		return false;
	}

	private void addError(ConstraintValidatorContext context, String code, Object... args) {
		context.buildConstraintViolationWithTemplate(
				messageSource.getMessage(code, args, LocaleContextHolder.getLocale())).addPropertyNode(property)
				.addConstraintViolation();
	}

	private boolean checkOpener(ConstraintValidatorContext context, Ticket ticket, User performer) {
		int openingPos = ticket.getTracking().size() - 1;
		Action action = ticket.getTracking().get(openingPos);
		if (action instanceof Opening opening && performer.equals(opening.getPerformer())) {
			return true;
		}
		addError(context, "error.Performer.role.not.the.opening.employee");
		return false;
	}

	private boolean checkHasIntervention(ConstraintValidatorContext context, Ticket ticket, User performer) {
		for (Action action : ticket.getTracking()) {
			if (action instanceof Intervention intervention && performer.equals(intervention.getPerformer())) {
				return true;
			}
		}
		addError(context, "error.Performer.role.not.the.technician.of.any.intervention");
		return false;
	}
}
