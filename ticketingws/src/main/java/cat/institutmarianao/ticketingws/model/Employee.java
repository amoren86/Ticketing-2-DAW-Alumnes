package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

/**
 * <p>
 * An employee that opens tickets. He can also close his own ticket.
 * </p>
 * <p>
 * An employee has role <code>User.Role.EMPLOYEE</code> and his authorities list
 * contains <code>{"EMPLOYEE"}</code>
 * </p>
 *
 * @see User
 * @see Role
 * @see Opening
 * @see Close
 *
 */
/* JPA */
@Entity
/* An employee is identified in the user table with role=EMPLOYEE */
@DiscriminatorValue(User.EMPLOYEE)
public class Employee extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_PLACE = 100;

	/* Validation */
	@NotNull(groups = OnUserCreate.class)
	@Valid
	/* JPA */
	@ManyToOne(fetch = FetchType.EAGER)
	private Room room;

	/* Validation */
	@Size(max = MAX_PLACE)
	@NotBlank(groups = OnUserCreate.class)
	private String place;

	public Employee() {
		// POJO constructor
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String lloc) {
		place = lloc;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Employee employee)) {
			return false;
		}
		return Objects.equals(username, employee.username);
	}
}
