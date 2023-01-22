package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/* JSON annotations */
/*
 * Maps JSON data to OpeningDto, AssignmentDto, InterventionDto or CloseDto instance
 * depending on property type
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({ @Type(value = AssignmentDto.class, name = ActionDto.ASSIGNMENT),
		@Type(value = InterventionDto.class, name = ActionDto.INTERVENTION),
		@Type(value = CloseDto.class, name = ActionDto.CLOSE) })
/* Validation */
public abstract class ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	/* Values for type - MUST be constants */
	public static final String ASSIGNMENT = "ASSIGNMENT";
	public static final String INTERVENTION = "INTERVENTION";
	public static final String CLOSE = "CLOSE";

	public enum Type {
		OPENING, ASSIGNMENT, INTERVENTION, CLOSE
	}

	/* Validation */
	// @Null(groups = { OnTicketCreate.class, OnActionCreate.class }) // Must be
	// null on inserts
	// @NotNull(groups = OnActionUpdate.class) // Must be not null on updates
	protected Long id;

	/* Validation */
//	@NotNull
//	protected Action.Type type;

	/* Validation */
	@NotEmpty
	protected String performer;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	protected Date date = new Date();

	/* JSON */
	@NotNull
	protected Long ticketId;

	protected ActionDto() {
		// POJO Contructor
	}

	public ActionDto(@NotEmpty String performer, Date date, @NotNull Long ticketId) {
		this.performer = performer;
		this.date = date;
		this.ticketId = ticketId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Action.Type getType() {
//		return type;
//	}
//
//	public void setType(Action.Type type) {
//		this.type = type;
//	}

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
}
