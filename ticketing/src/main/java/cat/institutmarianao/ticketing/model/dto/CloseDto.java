package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CloseDto extends ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public CloseDto() {
		// POJO constructor
	}

	public CloseDto(@NotEmpty String performer, Date date, @NotNull Long ticketId) {
		super(performer, date, ticketId);
	}
}
