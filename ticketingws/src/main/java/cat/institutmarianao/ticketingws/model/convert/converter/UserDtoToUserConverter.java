package cat.institutmarianao.ticketingws.model.convert.converter;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.ticketingws.model.Company;
import cat.institutmarianao.ticketingws.model.Employee;
import cat.institutmarianao.ticketingws.model.Room;
import cat.institutmarianao.ticketingws.model.Supervisor;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.convert.exception.ActionToTicketDtoConversionException;
import cat.institutmarianao.ticketingws.model.dto.EmployeeDto;
import cat.institutmarianao.ticketingws.model.dto.SupervisorDto;
import cat.institutmarianao.ticketingws.model.dto.TechnicianDto;
import cat.institutmarianao.ticketingws.model.dto.UserDto;
import cat.institutmarianao.ticketingws.services.CompanyService;
import cat.institutmarianao.ticketingws.services.RoomService;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

	@Autowired
	private RoomService roomService;

	@Autowired
	private CompanyService companyService;

	@Override
	public User convert(UserDto source) {
		if (source instanceof SupervisorDto supervisorDto) {
			// Includes Supervisor
			Supervisor supervisor = new Supervisor();
			copyCommonProperties(supervisorDto, supervisor);

			Optional<Company> company = companyService.findById(supervisorDto.getCompanyId());
			if (company.isEmpty()) {
				throw new ActionToTicketDtoConversionException("Company not found " + supervisorDto.getCompanyId());
			}

			supervisor.setCompany(company.get());
			return supervisor;
		}
		if (source instanceof TechnicianDto technicianDto) {
			// Includes Supervisor
			Technician technician = new Technician();
			copyCommonProperties(technicianDto, technician);

			Optional<Company> company = companyService.findById(technicianDto.getCompanyId());
			if (company.isEmpty()) {
				throw new ActionToTicketDtoConversionException("Company not found " + technicianDto.getCompanyId());
			}

			technician.setCompany(company.get());
			return technician;
		}
		if (source instanceof EmployeeDto employeeDto) {
			Employee employee = new Employee();
			copyCommonProperties(employeeDto, employee);

			Optional<Room> room = roomService.findById(employeeDto.getRoomId());
			if (room.isEmpty()) {
				throw new ActionToTicketDtoConversionException("Room not found " + employeeDto.getRoomId());
			}

			employee.setRoom(room.get());
			employee.setPlace(employeeDto.getPlace());
			return employee;
		}
		return null;
	}

	private void copyCommonProperties(UserDto userDto, User user) {
		BeanUtils.copyProperties(userDto, user);
	}
}
