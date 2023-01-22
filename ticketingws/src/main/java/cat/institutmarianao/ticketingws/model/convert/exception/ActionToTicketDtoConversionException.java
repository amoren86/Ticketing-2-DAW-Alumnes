package cat.institutmarianao.ticketingws.model.convert.exception;

import org.springframework.core.convert.ConversionException;

public class ActionToTicketDtoConversionException extends ConversionException {

	private static final long serialVersionUID = 1L;

	public ActionToTicketDtoConversionException(String message) {
		super(message);
	}
}
