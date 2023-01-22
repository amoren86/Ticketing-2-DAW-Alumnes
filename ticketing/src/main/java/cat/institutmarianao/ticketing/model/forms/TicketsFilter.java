package cat.institutmarianao.ticketing.model.forms;

import java.util.Date;

import cat.institutmarianao.ticketing.model.dto.TicketDto.Category;
import cat.institutmarianao.ticketing.model.dto.TicketDto.Status;

/**
 * <p>
 * Stores a set of fields used to filter the list of tickets expected as a
 * result of a query
 * </p>
 *
 * @see Status
 */
public class TicketsFilter {
	private Status status;
	private String performer;
	private Category category;
	private Date from;
	private Date to;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String employee) {
		performer = employee;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * <p>
	 * Reset the filter
	 * </p>
	 */
	public void clear() {
		status = null;
		performer = null;
		category = null;
		from = null;
		to = null;
	}
}
