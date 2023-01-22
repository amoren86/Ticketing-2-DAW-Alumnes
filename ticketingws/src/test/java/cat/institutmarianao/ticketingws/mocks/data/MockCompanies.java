package cat.institutmarianao.ticketingws.mocks.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;

import cat.institutmarianao.ticketingws.model.Company;

public class MockCompanies {
	public static final Company ROOM1;
	public static final Company ROOM2;

	public static final List<Company> ROOMS = new ArrayList<>();

	static {

		ROOM1 = generateRandomCompany();
		ROOM2 = generateRandomCompany();

		ROOMS.add(ROOM1);
		ROOMS.add(ROOM2);
	}

	public static Company generateRandomCompany() {
		Company room = new Company();
		room.setId(new Random().nextLong(Integer.MAX_VALUE - 1) + 1);
		room.setName(RandomString.make(Company.MAX_COMPANY));
		return room;
	}
}
