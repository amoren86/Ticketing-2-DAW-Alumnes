package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import cat.institutmarianao.ticketingws.model.User.Role;
import cat.institutmarianao.ticketingws.validation.constraints.Performer;
import cat.institutmarianao.ticketingws.validation.groups.OnActionCreate;
import cat.institutmarianao.ticketingws.validation.groups.OnActionUpdate;
import cat.institutmarianao.ticketingws.validation.groups.OnTicketCreate;

/**
 * <p>
 * Represents any performed action by a user on a ticket in a certain date. They
 * are stored in the tracking stack of the ticket.
 * </p>
 *
 * <p>
 * Actions can be:
 * <ul>
 * <li><b>opening</b>: when an employee opens a new ticket</li>
 * <li><b>assignment</b>: when a supervisor assigns a ticket to a
 * technician</li>
 * <li><b>intervention</b>: when a technician performs an intervention</li>
 * <li><b>close</b>: when a the ticket is closed, for instance, when considered
 * that is resolved. It can be done by:
 * <ul>
 * <li>the <b>employee</b> that has opened the ticket</li>
 * <li>the <b>technician</b> who has interventions on the ticket</li>
 * <li>a <b>supervisor</b></li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 *
 * @see Opening
 * @see Assignment
 * @see Intervention
 * @see Close
 * @see User
 * @see Employee
 * @see Technician
 * @see Supervisor
 */
/* Validation */
@Performer(opening = Role.EMPLOYEE, assignment = Role.SUPERVISOR, intervention = { Role.TECHNICIAN,
		Role.SUPERVISOR }, close = Role.SUPERVISOR, openingUserCloseAllowed = true, interventionTechnicianCloseAllowed = true)
/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "actions", indexes = { @Index(name = "type", columnList = "type", unique = false),
		@Index(name = "ticket_x_date", columnList = "ticket_id, date DESC", unique = true) })
/* JPA Inheritance strategy is single table */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
 * Maps different JPA objects depending on his type attribute (Opening,
 * Assignment, Intervention or Close)
 */
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Values for type - MUST be constants */
	public static final String OPENING = "OPENING";
	public static final String ASSIGNMENT = "ASSIGNMENT";
	public static final String INTERVENTION = "INTERVENTION";
	public static final String CLOSE = "CLOSE";

	public enum Type {
		OPENING, ASSIGNMENT, INTERVENTION, CLOSE
	}

	/* Validation */
	@Null(groups = { OnTicketCreate.class, OnActionCreate.class }) // Must be null on inserts
	@NotNull(groups = OnActionUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	protected Long id;

	/* Validation */ @NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(name = "type", insertable = false, updatable = false, nullable = false)
	protected Type type;

	/* Validation */
	@NotNull
	@Valid
	/* JPA */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	protected User performer;

	/* JPA */
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	protected Date date = new Date();

	/* Validation */
	@Null(groups = OnTicketCreate.class) // The JSON do not have the ticket reference (ticket has no id yet)
	@NotNull(groups = { OnActionCreate.class })
//	@NotNull
	/* JPA */
	@ManyToOne(optional = false)
	@JoinColumn(name = "ticket_id", nullable = false)
	protected Ticket ticket;

	protected Action() {
		// POJO Contructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public User getPerformer() {
		return performer;
	}

	public void setPerformer(User performer) {
		this.performer = performer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
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
		if (!(obj instanceof Action other)) {
			return false;
		}
		return Objects.equals(id, other.id);
	}
}
