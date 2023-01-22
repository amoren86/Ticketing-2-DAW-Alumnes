package cat.institutmarianao.ticketing.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 * An employee that opens tickets. He can also close his own ticket.
 * </p>
 * <p>
 * An employee has role <code>User.Role.EMPLOYEE</code> and his authorities list
 * contains <code>{"EMPLOYEE"}</code>
 * </p>
 *
 * @see UserDto
 * @see Role
 * @see Opening
 * @see CloseDto
 *
 */
public class EmployeeDto extends UserDto {

	private static final long serialVersionUID = 1L;

	@Positive
	private Long roomId;

	@NotBlank
	private String place;

	public EmployeeDto() {
	}

	/**
	 * <p>
	 * An employee that reports tickets opening tickets
	 * </p>
	 *
	 * @param username  the username
	 * @param password  the user password, stored encrypted by BCrypt hash algorithm
	 *                  (see {@link BCryptPasswordEncoder})
	 * @param fullName  full name of the user
	 * @param extension phone extension, to call the user
	 * @param room      the room where the employee works
	 * @param place     the place in this room where the users is located
	 */
	public EmployeeDto(@NotBlank @Size(min = MIN_USERNAME, max = MAX_USERNAME) String username,
			@NotBlank @Size(min = MIN_PASSWORD) String password,
			@NotBlank @Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME) String fullName,
			@Min(0) @Max(MAX_EXTENSION) Integer extension, @Positive Long roomId, @NotBlank String place) {
		super(username, password, fullName, extension);
		this.roomId = roomId;
		this.place = place;
		super.role = Role.EMPLOYEE;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
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
		return super.equals(obj);
	}

}
