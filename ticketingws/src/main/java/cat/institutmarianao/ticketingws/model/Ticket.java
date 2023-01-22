package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

import cat.institutmarianao.ticketingws.validation.constraints.Tracking;
import cat.institutmarianao.ticketingws.validation.groups.OnTicketCreate;
import cat.institutmarianao.ticketingws.validation.groups.OnTicketUpdate;

/**
 * <p>
 * Stores an issue reported by a user, who we will call <b>performer</b>
 * </p>
 * <p>
 * The ticket can have different categories (see {@link Category}) depending on
 * the kind of issue reported
 * </p>
 * <p>
 * The status of the ticked is stored in the tracking stack. Depending on the
 * actions that are stored there, the status can be:
 * <ul>
 * <li><b>pending </b> : when the tracking stack contains only a opening
 * action</li>
 * <li><b>assigned </b> : when the tracking stack contains an assignment and an
 * opening actions</li>
 * <li><b>in process </b> : when the tracking stack contains interventions
 * actions as well as an assignment and an opening</li>
 * <li><b>closed </b> : when top action of the stack is a close one</li>
 * </ul>
 * </p>
 *
 * @see Category
 * @see Opening
 * @see Assignment
 * @see Intervention
 * @see Close
 */
/* JPA */
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	/**
	 * <p>
	 * It contains the categories that group most common issues of a ticket
	 * </p>
	 */
	public enum Category {
		HARDWARE, SOFTWARE, PRINTER, NETWORK, SUPPORT, OTHERS
	}

	public enum Status {
		PENDING, IN_PROCESS, HISTORICAL
	}

	/* Validation */
	@Null(groups = OnTicketCreate.class) // Must be null on inserts
	@NotNull(groups = OnTicketUpdate.class) // Must be not null on updates
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	/* Validation */
	@NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(nullable = false)
	private Category category;

	/* Validation */
	@NotBlank
	@Size(max = MAX_DESCRIPTION)
	/* JPA */
	@Column(nullable = false)
	private String description;

	/* Validation */
	@Tracking
	/* JPA */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "ticket")
	@Column(nullable = false)
	@OrderBy("date DESC")
	private List<@Valid Action> tracking;

	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	/* Hibernate */
	@Formula("(SELECT CASE a.type WHEN 'OPENING' THEN 'PENDING' "
			+ " WHEN 'ASSIGNMENT' THEN 'IN_PROCESS' WHEN 'INTERVENTION' THEN 'IN_PROCESS' "
			+ " WHEN 'CLOSE' THEN 'HISTORICAL' ELSE NULL END FROM actions a "
			+ " WHERE a.date=( SELECT MAX(last_action.date) FROM actions last_action "
			+ " WHERE last_action.ticket_id=a.ticket_id AND last_action.ticket_id=id ))")
	private Status status;

	public Ticket() {
		// POJO Contructor
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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

	public List<Action> getTracking() {
		return tracking;
	}

	public void setTracking(List<Action> tracing) {
		tracking = tracing;
	}

	public Status getStatus() {
		return status;
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
		if (!(obj instanceof Ticket other)) {
			return false;
		}
		return Objects.equals(id, other.id);
	}
}
