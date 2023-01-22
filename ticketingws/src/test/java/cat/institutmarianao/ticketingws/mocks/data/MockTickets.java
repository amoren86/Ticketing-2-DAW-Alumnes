package cat.institutmarianao.ticketingws.mocks.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.Ticket.Category;

public class MockTickets {
	public static final Opening OPENING1;
	public static final Opening OPENING2;
	public static final Opening OPENING3;
	public static final Opening OPENING4;

	public static final Ticket TICKET1;
	public static final Ticket TICKET2;
	public static final Ticket TICKET3;
	public static final Ticket TICKET4;

	public static final List<Ticket> TICKETS = new ArrayList<>();

	public static SimpleDateFormat sdf;

	static {
		TICKET1 = generateRandomTicket();
		TICKET2 = generateRandomTicket();
		TICKET3 = generateRandomTicket();
		TICKET4 = generateRandomTicket();

		OPENING1 = (Opening) MockActions.generateRandomAction(TICKET1, new Opening());
		OPENING2 = (Opening) MockActions.generateRandomAction(TICKET2, new Opening());
		OPENING3 = (Opening) MockActions.generateRandomAction(TICKET3, new Opening());
		OPENING4 = (Opening) MockActions.generateRandomAction(TICKET4, new Opening());

		TICKETS.add(TICKET1);
		TICKETS.add(TICKET2);
		TICKETS.add(TICKET3);
		TICKETS.add(TICKET4);
	}

	public static Ticket generateRandomTicket() {
		Category[] categories = Category.values();

		Ticket ticket = new Ticket();
		ticket.setCategory(categories[new Random().nextInt(categories.length)]);
		ticket.setDescription(RandomString.make(Ticket.MAX_DESCRIPTION));
		ticket.setTracking(new ArrayList<Action>());

		Opening opening = new Opening();
		opening.setPerformer(MockUsers.generateRandomEmployee());
		opening.setType(Type.OPENING);

		opening.setTicket(ticket);
		ticket.getTracking().add(opening);
		return ticket;
	}
}
