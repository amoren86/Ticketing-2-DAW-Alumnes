package cat.institutmarianao.ticketingws.model.convert.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import cat.institutmarianao.ticketingws.model.Employee;
import cat.institutmarianao.ticketingws.model.Supervisor;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.EmployeeDto;
import cat.institutmarianao.ticketingws.model.dto.SupervisorDto;
import cat.institutmarianao.ticketingws.model.dto.TechnicianDto;
import cat.institutmarianao.ticketingws.model.dto.UserDto;

public class UserToUserDtoConverter implements Converter<User, UserDto> {

	@Override
	public UserDto convert(User source) {
		if (source instanceof Supervisor supervisor) {
			// Includes Supervisor
			SupervisorDto supervisorDto = new SupervisorDto();
			copyCommonProperties(supervisor, supervisorDto);
			supervisorDto.setCompanyId(supervisor.getCompany().getId());
			return supervisorDto;
		}
		if (source instanceof Technician technician) {
			// Includes Supervisor
			TechnicianDto technicianDto = new TechnicianDto();
			copyCommonProperties(technician, technicianDto);
			technicianDto.setCompanyId(technician.getCompany().getId());
			return technicianDto;
		}
		if (source instanceof Employee employee) {
			EmployeeDto employeeDto = new EmployeeDto();
			copyCommonProperties(employee, employeeDto);
			employeeDto.setRoomId(employee.getRoom().getId());
			employeeDto.setPlace(employee.getPlace());
			return employeeDto;
		}
		return null;
	}

	private void copyCommonProperties(User user, UserDto userDto) {
		BeanUtils.copyProperties(user, userDto);
	}
}
