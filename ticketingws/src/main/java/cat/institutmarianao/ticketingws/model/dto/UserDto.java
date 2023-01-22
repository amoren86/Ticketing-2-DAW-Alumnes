package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

/* JSON annotations */
/*
 * Maps JSON data to Employee, Technician or Supervisor instance depending on
 * property role
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @Type(value = EmployeeDto.class, name = User.EMPLOYEE),
		@Type(value = TechnicianDto.class, name = User.TECHNICIAN),
		@Type(value = SupervisorDto.class, name = User.SUPERVISOR) })
public abstract class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@NotBlank
	@Size(min = User.MIN_USERNAME, max = User.MAX_USERNAME)
	protected String username;

	/* Validation */
	@NotNull
	protected User.Role role;

	/* JSON */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Not present in generated JSON
	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = User.MIN_PASSWORD)
	protected String password;

	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = User.MIN_FULL_NAME, max = User.MAX_FULL_NAME)
	protected String fullName;

	/* Validation */
	@NotNull(groups = OnUserCreate.class)
	@PositiveOrZero
	@Max(User.MAX_EXTENSION)
	protected Integer extension;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected String location;

	protected UserDto() {
		// POJO Contructor
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User.Role getRole() {
		return role;
	}

	public void setRole(User.Role role) {
		this.role = role;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
