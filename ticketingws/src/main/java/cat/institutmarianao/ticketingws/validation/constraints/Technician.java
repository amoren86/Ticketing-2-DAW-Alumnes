package cat.institutmarianao.ticketingws.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cat.institutmarianao.ticketingws.model.User.Role;
import cat.institutmarianao.ticketingws.validation.constraints.impl.PerformerImpl;

@Documented
@Constraint(validatedBy = PerformerImpl.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface Technician {
	String message() default "{error.Technician.is.not.valid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String property() default "performer";

	Role[] opening();

	Role[] assignment();

	Role[] intervention();

	Role[] close();

	boolean openingUserCloseAllowed();

	boolean interventionTechnicianCloseAllowed();
}
