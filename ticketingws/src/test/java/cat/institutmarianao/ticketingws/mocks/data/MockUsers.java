package cat.institutmarianao.ticketingws.mocks.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;

import cat.institutmarianao.ticketingws.model.Employee;
import cat.institutmarianao.ticketingws.model.Supervisor;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.User.Role;

public class MockUsers {
	public static final Employee EMPLOYEE1;
	public static final Employee EMPLOYEE2;
	public static final Employee EMPLOYEE3;
	public static final Employee EMPLOYEE4;
	public static final Employee EMPLOYEE5;

	public static final Technician TECHNICIAN1;
	public static final Technician TECHNICIAN2;

	public static final Supervisor SUPERVISOR1;

	public static final List<User> USERS = new ArrayList<>();

	static {

		EMPLOYEE1 = generateRandomEmployee();
		EMPLOYEE2 = generateRandomEmployee();
		EMPLOYEE3 = generateRandomEmployee();
		EMPLOYEE4 = generateRandomEmployee();
		EMPLOYEE5 = generateRandomEmployee();

		TECHNICIAN1 = generateRandomTechnician();
		TECHNICIAN2 = generateRandomTechnician();

		SUPERVISOR1 = generateRandomSupervisor();

		USERS.add(EMPLOYEE1);
		USERS.add(EMPLOYEE2);
		USERS.add(EMPLOYEE3);
		USERS.add(EMPLOYEE4);
		USERS.add(EMPLOYEE5);

		USERS.add(TECHNICIAN1);
		USERS.add(TECHNICIAN2);

		USERS.add(SUPERVISOR1);
	}

	private static User fillUserRandom(User user) {
		user.setUsername(RandomString.make(User.MAX_USERNAME));
		user.setPassword(RandomString.make(User.MIN_PASSWORD));
		user.setFullName(RandomString.make(User.MAX_FULL_NAME));
		user.setExtension(new Random().nextInt(User.MAX_EXTENSION));
		return user;
	}

	public static Employee generateRandomEmployee() {
		Employee employee = (Employee) fillUserRandom(new Employee());
		employee.setRole(Role.EMPLOYEE);
		employee.setRoom(MockRooms.generateRandomRoom());
		employee.setPlace(RandomString.make(Employee.MAX_PLACE));
		return employee;
	}

	public static Technician generateRandomTechnician() {
		Technician technician = (Technician) fillUserRandom(new Technician());
		technician.setRole(Role.TECHNICIAN);
		technician.setCompany(MockCompanies.generateRandomCompany());
		return technician;
	}

	public static Supervisor generateRandomSupervisor() {
		Supervisor supervisor = (Supervisor) fillUserRandom(new Supervisor());
		supervisor.setRole(Role.SUPERVISOR);
		supervisor.setCompany(MockCompanies.generateRandomCompany());
		return supervisor;
	}

	public static User generateRandomUser() {
		switch (new Random().nextInt(Role.values().length)) {
		case 1:
			return generateRandomEmployee();
		case 2:
			return generateRandomTechnician();
		case 3:
			return generateRandomSupervisor();
		default:
			return generateRandomEmployee();
		}
	}
}
