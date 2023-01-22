package cat.institutmarianao.ticketing.model.forms;

import cat.institutmarianao.ticketing.model.dto.TicketDto.Status;
import cat.institutmarianao.ticketing.model.dto.UserDto.Role;

/**
 * <p>
 * Stores a set of fields used to filter the list of users expected as a result
 * of a query
 * </p>
 *
 * @see Status
 */
public class UsersFilter {
	private Role role;
	private String fullName;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * <p>
	 * Reset the filter
	 * </p>
	 */
	public void clear() {
		role = null;
		fullName = null;
	}
}
