package cat.institutmarianao.ticketingws.mocks.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;

import cat.institutmarianao.ticketingws.model.Room;

public class MockRooms {
	public static final Room ROOM1;
	public static final Room ROOM2;
	public static final Room ROOM3;

	public static final List<Room> ROOMS = new ArrayList<>();

	static {

		ROOM1 = generateRandomRoom();
		ROOM2 = generateRandomRoom();
		ROOM3 = generateRandomRoom();

		ROOMS.add(ROOM1);
		ROOMS.add(ROOM2);
		ROOMS.add(ROOM3);
	}

	public static Room generateRandomRoom() {
		Room room = new Room();
		room.setId(new Random().nextLong(Integer.MAX_VALUE - 1) + 1);
		room.setName(RandomString.make(Room.MAX_ROOM));
		return room;
	}
}
