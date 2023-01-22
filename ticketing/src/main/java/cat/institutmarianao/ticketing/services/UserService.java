package cat.institutmarianao.ticketing.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import cat.institutmarianao.ticketing.model.dto.EmployeeDto;
import cat.institutmarianao.ticketing.model.dto.TechnicianDto;
import cat.institutmarianao.ticketing.model.dto.UserDto;
import cat.institutmarianao.ticketing.model.forms.UsersFilter;

public interface UserService extends UserDetailsService {
	List<UserDto> getAllUsers();

	List<EmployeeDto> getAllEmployees();

	List<TechnicianDto> getAllTechnicians();

	List<UserDto> filterUsers(UsersFilter filter);

	UserDto add(UserDto userDto);

	void update(UserDto userDto);

	void remove(String username);
}
