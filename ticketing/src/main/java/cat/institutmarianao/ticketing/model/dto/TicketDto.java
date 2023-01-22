package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;
	public static final String DATE_FORMAT = ActionDto.DATE_FORMAT;

	/**
	 * <p>
	 * The status of a ticket:
	 * </p>
	 * <ul>
	 * <li><b>pending </b> : when a ticket is opened by an employee but not assigned
	 * yet</li>
	 * <li><b>assigned </b> : when a ticket is assigned to a technician but not
	 * resolved</li>
	 * <li><b>in process </b> : when a ticket has interventions but not resolved
	 * </li>
	 * <li><b>closed </b> : when a ticket is resolved</li>
	 * </ul>
	 *
	 */
	public enum Status {
		PENDING, ASSIGNED, IN_PROCESS, CLOSED;
	}

	/**
	 * <p>
	 * It contains the categories that group most common tickets of a ticket
	 * </p>
	 */
	public enum Category {
		HARDWARE, SOFTWARE, PRINTER, NETWORK, SUPPORT, OTHERS;
	}

	private Long id;

	/* Validation */
	@NotNull
	private Category category;

	/* Validation */
	@NotBlank
	@Size(max = TicketDto.MAX_DESCRIPTION)
	private String description;

	/* Validation */
	@NotBlank
	protected String performer;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	protected Date openingDate = new Date();

	public TicketDto() {
		// POJO Contructor
	}

	public TicketDto(@NotNull Category category, @NotBlank @Size(max = TicketDto.MAX_DESCRIPTION) String description,
			@NotEmpty String performer, Date openingDate) {
		this.category = category;
		this.description = description;
		this.performer = performer;
		this.openingDate = openingDate;
	}

	public TicketDto(@NotNull Category category, @NotBlank @Size(max = TicketDto.MAX_DESCRIPTION) String description,
			@NotEmpty String performer) {
		this(category, description, performer, new Date());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TicketDto other)) {
			return false;
		}
		return Objects.equals(id, other.id);
	}
}
