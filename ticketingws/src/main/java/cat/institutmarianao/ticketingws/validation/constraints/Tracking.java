package cat.institutmarianao.ticketingws.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cat.institutmarianao.ticketingws.validation.constraints.impl.TrackingImpl;

@Documented
@Constraint(validatedBy = TrackingImpl.class)
@Target({ METHOD, FIELD, PARAMETER, LOCAL_VARIABLE })
@Retention(RUNTIME)
public @interface Tracking {
	String message() default "{error.Tracking.is.not.valid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
