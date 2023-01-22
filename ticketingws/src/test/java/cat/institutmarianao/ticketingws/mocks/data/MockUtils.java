package cat.institutmarianao.ticketingws.mocks.data;

import java.util.Random;

public class MockUtils {
	public static Long getRandomId() {
		return (long) (new Random().nextInt(Integer.MAX_VALUE - 1) + 1);
	}
}
