package cat.institutmarianao.ticketing.model.dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import cat.institutmarianao.ticketing.model.dto.UserDto.Role;

/**
 * <p>
 * Represents an authority granted to an {@link Authentication} object.
 * </p>
 * <p>
 * It contain a permission, based on a role
 * </p>
 * 
 * @see GrantedAuthority
 * @see Role
 */
public class AuthorityDto implements GrantedAuthority {
	private static final long serialVersionUID = 2881142750146437183L;

	private Role role;

	public AuthorityDto(Role role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role.name();
	}
}
