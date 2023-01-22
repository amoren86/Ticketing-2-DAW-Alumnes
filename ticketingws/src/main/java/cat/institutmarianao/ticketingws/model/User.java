package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;
import org.springframework.security.core.userdetails.UserDetails;

import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

/**
 * <p>
 * A generic user of the ticketing app. It can be an employee, a technician or a
 * supervisor (see {@link User.Role})
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
 * @see Employee
 * @see Technician
 * @see Supervisor
 */
/* JPA annotations */
@Entity
/* Mapping JPA Indexes */
@Table(name = "users", indexes = { @Index(name = "role", columnList = "role", unique = false),
		@Index(name = "full_name", columnList = "full_name", unique = false),
		@Index(name = "role_x_full_name", columnList = "role, full_name", unique = false) })
/* JPA Inheritance strategy is single table */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
 * Maps different JPA objects depending on his role attribute (Employee,
 * Technician or Supervisor)
 */
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements Serializable {

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

	/* Validation */
	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	/* JPA */
	@Id
	@Column(unique = true, nullable = false)
	protected String username;

	/* Validation */
	@NotNull
	/* JPA */
	@Enumerated(EnumType.STRING) // Stored as string
	@Column(name = "role", insertable = false, updatable = false, nullable = false)
	protected Role role;

	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = MIN_PASSWORD)
	/* JPA */
	@Column(nullable = false)
	protected String password;

	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME)
	/* JPA */
	@Column(name = "full_name", nullable = false)
	protected String fullName;

	/* Validation */
	@NotNull(groups = OnUserCreate.class)
	@PositiveOrZero
	@Max(MAX_EXTENSION)
	protected Integer extension;

	/* Hibernate */
	@Formula("CONCAT(COALESCE((SELECT c.name FROM companies c WHERE company_id = c.id), ''), COALESCE((SELECT CONCAT(r.name, ' (', place, ')') FROM rooms r WHERE room_id = r.id), ''))")
	protected String location;

	protected User() {
		// POJO Contructor
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User other)) {
			return false;
		}
		return Objects.equals(username, other.username);
	}
}
