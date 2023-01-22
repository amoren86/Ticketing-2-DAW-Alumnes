package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <p>
 * A generic user of the ticketing app. It can be an employee, a technician or a
 * supervisor (see {@link UserDto.Role})
 * </p>
 *
 * <p>
 * It is integrated with Spring Security through the implementation of
 * <code>UserDetails</code> interface
 * </p>
 *
 * <p>
 * Its <code>username</code> attribute must be unique
 * </p>
 *
 * @see UserDetails
 * @see Role
 * @see EmployeeDto
 * @see TechnicianDto
 * @see SupervisorDto
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @Type(value = EmployeeDto.class, name = UserDto.EMPLOYEE),
		@Type(value = TechnicianDto.class, name = UserDto.TECHNICIAN),
		@Type(value = SupervisorDto.class, name = UserDto.SUPERVISOR) })
public abstract class UserDto implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	/* Values for role - MUST be constants (can not be enums) */
	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String TECHNICIAN = "TECHNICIAN";
	public static final String SUPERVISOR = "SUPERVISOR";

	public enum Role {
		EMPLOYEE, TECHNICIAN, SUPERVISOR
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 10;
	public static final int MIN_FULL_NAME = 3;
	public static final int MAX_FULL_NAME = 100;
	public static final int MAX_EXTENSION = 9999;

	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	protected String username;
	@NotNull
	protected Role role;

	@JsonInclude(Include.NON_NULL)
	@NotBlank
	@Size(min = MIN_PASSWORD)
	protected String password;

	@NotBlank
	@Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME)
	protected String fullName;

	@NotNull
	@Min(0)
	@Max(MAX_EXTENSION)
	protected Integer extension;

	protected String location;

	protected transient List<AuthorityDto> authorityDtos;

	/**
	 * For serializable purposes
	 */
	protected UserDto() {
	}

	/**
	 * Initialises user common fields
	 *
	 * @param username  the user name, which identifies this user
	 * @param password  the user password, stored encrypted by BCrypt hash algorithm
	 *                  (see {@link BCryptPasswordEncoder})
	 * @param fullName  full name of the user
	 * @param extension phone extension, to call the user
	 */
	protected UserDto(@NotBlank @Size(min = MIN_USERNAME, max = MAX_USERNAME) String username,
			@NotBlank @Size(min = MIN_PASSWORD) String password,
			@NotBlank @Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME) String fullName,
			@Min(0) @Max(MAX_EXTENSION) Integer extension) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.extension = extension;
	}

	@Override /* Spring Security getter */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getExtension() {
		return extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * <p>
	 * It gets the <code>Authorities</code> list depending on the <code>Role</code>
	 * </p>
	 *
	 * <p>
	 * An authority is an individual privilege (=permission)
	 * </p>
	 *
	 * <p>
	 * The role represents the category of a user. Depending on this category, it
	 * can group several permissions. For example, the Role.SUPERVISOR has all
	 * SUPERVISOR permissions as well as all TECHNICIAN ones, cause a SUPERVISOR is
	 * also a TECHNICIAN.
	 * </p>
	 *
	 * @return the list of authorities that this user has, depending on his role
	 *         </p>
	 *
	 * @see AuthorityDto
	 */
	@Override /* Spring Security getter */
	public List<AuthorityDto> getAuthorities() {
		if (authorityDtos != null) {
			return authorityDtos;
		}
		authorityDtos = new ArrayList<>();
		switch (getRole()) {
		case EMPLOYEE:
			authorityDtos.add(new AuthorityDto(Role.EMPLOYEE));
			break;
		case SUPERVISOR:
			authorityDtos.add(new AuthorityDto(Role.SUPERVISOR));
			authorityDtos.add(new AuthorityDto(Role.TECHNICIAN));
			break;
		case TECHNICIAN:
			authorityDtos.add(new AuthorityDto(Role.TECHNICIAN));
			break;
		default:
			break;
		}
		return authorityDtos;
	}

	@Override /* Spring Security getter */
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	/**
	 * Two users are equals if their <code>username</code>s are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserDto other)) {
			return false;
		}
		return Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return username + ", " + fullName + " (" + extension + ")";
	}
}
