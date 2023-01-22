package cat.institutmarianao.ticketingws.validation.constraints.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.validation.constraints.Tracking;

public class TrackingImpl implements ConstraintValidator<Tracking, List<Action>> {

	private static final Type CLOSE = Type.CLOSE;

	// TODO - Parametrise this?
	private static final Map<Type, List<Type>> ACTIONS_X_PREVIOUS_ACTIONS = new EnumMap<>(Type.class);

	static {
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.OPENING, new ArrayList<>());
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.ASSIGNMENT, Arrays.asList(Type.OPENING));
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.INTERVENTION, Arrays.asList(Type.ASSIGNMENT, Type.INTERVENTION));
		ACTIONS_X_PREVIOUS_ACTIONS.put(Type.CLOSE, Arrays.asList(Type.OPENING, Type.ASSIGNMENT, Type.INTERVENTION));
	}

	@Autowired
	private MessageSource messageSource;

	private boolean closed;

	@Override
	public boolean isValid(List<Action> tracking, ConstraintValidatorContext context) {

		if (tracking == null) {
			addError(context, "error.Tracking.is.null");
			return false;
		}
		if (tracking.isEmpty()) {
			addError(context, "error.Tracking.is.empty");
			return false;
		}

		// Date validations
		Date previousDate;
		Date currentDate = null;

		Action previousAction;
		Action currentAction = null;

		closed = false;

		boolean valid = true;

		// Assume tracking is a stack; comes in reverse order
		for (int pos = tracking.size() - 1; pos >= 0; pos--) {
			previousAction = currentAction;
			currentAction = tracking.get(pos);

			valid = valid && !isAlreadyClosed(context, currentAction, pos);

			valid = valid && isAllowedAction(context, currentAction, previousAction, pos);

			previousDate = currentDate;
			currentDate = currentAction.getDate();

			valid = valid && isCurrentDateAfterPrevious(context, previousDate, currentDate, pos);
		}
		if (!valid) {
			addError(context, tracking);
		}
		return valid;
	}

	private boolean isAlreadyClosed(ConstraintValidatorContext context, Action action, int pos) {
		boolean alreadyClosed = closed;
		if (alreadyClosed) {
			addError(context, "error.Tracking.before.close", pos);
		} else {
			closed = CLOSE.equals(action.getType());
		}
		return alreadyClosed;
	}

	private boolean isAllowedAction(ConstraintValidatorContext context, Action action, Action previousAction, int pos) {
		Type currentType = action.getType();
		Type previousType = previousAction == null ? null : previousAction.getType();

		boolean allowed = (previousType == null && ACTIONS_X_PREVIOUS_ACTIONS.get(currentType).isEmpty())
				|| ACTIONS_X_PREVIOUS_ACTIONS.get(currentType).contains(previousType);

		if (!allowed) {
			String nothing = messageSource.getMessage("error.Tracking.nothing", null, LocaleContextHolder.getLocale());

			String allowedTypesString = ACTIONS_X_PREVIOUS_ACTIONS.get(currentType).toString();
			String previousTypeString = previousType == null ? nothing : previousType.name();

			addError(context, "error.Tracking.action.not.allowed", pos, currentType.name(), allowedTypesString,
					previousTypeString);
		}

		return allowed;
	}

	private boolean isCurrentDateAfterPrevious(ConstraintValidatorContext context, Date previousDate, Date currentDate,
			int pos) {
		if (previousDate == null) {
			return true;
		}
		if (previousDate.after(currentDate)) {
			addError(context, "error.Tracking.wrong.date", pos);
			return false;
		}
		return true;
	}

	private void addError(ConstraintValidatorContext context, String code, Object... args) {
		context.buildConstraintViolationWithTemplate(
				messageSource.getMessage(code, args, LocaleContextHolder.getLocale())).addConstraintViolation();
	}

	private void addError(ConstraintValidatorContext context, List<Action> tracking) {
		context.buildConstraintViolationWithTemplate("Tracking=["
				+ tracking.stream().map(a -> a.getType().toString()).collect(Collectors.joining(", ")) + "]")
				.addConstraintViolation();
	}
}
