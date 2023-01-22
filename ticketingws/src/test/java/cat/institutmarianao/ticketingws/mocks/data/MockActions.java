package cat.institutmarianao.ticketingws.mocks.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Action.Type;
import cat.institutmarianao.ticketingws.model.Assignment;
import cat.institutmarianao.ticketingws.model.Close;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Ticket;

public class MockActions {
	public static final Opening OPENING1;
	public static final Opening OPENING2;
	public static final Opening OPENING3;
	public static final Opening OPENING4;

	public static final Assignment ASSIGNMENT1;
	public static final Assignment ASSIGNMENT2;
	public static final Assignment ASSIGNMENT3;

	public static final Intervention INTERVENTION1_1;
	public static final Intervention INTERVENTION1_2;
	public static final Intervention INTERVENTION2_1;

	public static final Close CLOSE1;

	public static final List<Action> OPENINGS = new ArrayList<>();

	public static final List<Action> TRACKING1 = new ArrayList<>();
	public static final List<Action> TRACKING2 = new ArrayList<>();
	public static final List<Action> TRACKING3 = new ArrayList<>();
	public static final List<Action> TRACKING4 = new ArrayList<>();

	public static SimpleDateFormat sdf;

	static {
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Ticket ticket1 = MockTickets.generateRandomTicket();

		OPENING1 = (Opening) generateRandomAction(ticket1, new Opening());
		ASSIGNMENT1 = (Assignment) generateRandomAction(ticket1, new Assignment());
		INTERVENTION1_1 = (Intervention) generateRandomAction(ticket1, new Intervention());
		INTERVENTION1_2 = (Intervention) generateRandomAction(ticket1, new Intervention());
		CLOSE1 = (Close) generateRandomAction(ticket1, new Close());

		OPENINGS.add(OPENING1);
		TRACKING1.add(OPENING1);
		TRACKING1.add(ASSIGNMENT1);
		TRACKING1.add(INTERVENTION1_1);
		TRACKING1.add(INTERVENTION1_2);
		TRACKING1.add(CLOSE1);

		Ticket ticket2 = MockTickets.generateRandomTicket();

		OPENING2 = (Opening) generateRandomAction(ticket2, new Opening());
		OPENINGS.add(OPENING2);
		TRACKING2.add(OPENING2);
		ASSIGNMENT2 = (Assignment) generateRandomAction(ticket2, new Assignment());
		INTERVENTION2_1 = (Intervention) generateRandomAction(ticket2, new Intervention());

		TRACKING2.add(ASSIGNMENT2);
		TRACKING2.add(INTERVENTION2_1);

		Ticket ticket3 = MockTickets.generateRandomTicket();

		OPENING3 = (Opening) generateRandomAction(ticket3, new Opening());
		OPENINGS.add(OPENING3);
		TRACKING3.add(OPENING3);
		ASSIGNMENT3 = (Assignment) generateRandomAction(ticket3, new Assignment());
		TRACKING3.add(ASSIGNMENT3);

		Ticket ticket4 = MockTickets.generateRandomTicket();

		OPENING4 = (Opening) generateRandomAction(ticket4, new Opening());
		OPENINGS.add(OPENING4);
		TRACKING4.add(OPENING4);
	}

	public static Action generateRandomAction(Ticket ticket, Action action) {
		action.setDate(new Date());
		action.setTicket(ticket);

		if (action instanceof Opening opening) {
			opening.setType(Type.OPENING);
			action.setPerformer(MockUsers.generateRandomEmployee());
		} else if (action instanceof Assignment assignment) {
			assignment.setType(Type.ASSIGNMENT);
			action.setPerformer(MockUsers.generateRandomSupervisor());
			assignment.setTechnician(MockUsers.generateRandomTechnician());
			assignment.setPriority(new Random().nextInt(Assignment.MAX_PRIORITAT - 1) + 1);
			assignment.setTicket(ticket);
		} else if (action instanceof Intervention intervention) {
			intervention.setType(Type.INTERVENTION);
			action.setPerformer(MockUsers.generateRandomTechnician());
			intervention.setHours(new Random().nextInt(Short.MAX_VALUE));
			intervention.setDescription(RandomString.make(Intervention.MAX_DESCRIPTION));
			intervention.setTicket(ticket);
		} else if (action instanceof Close close) {
			action.setPerformer(MockUsers.generateRandomSupervisor());
			close.setType(Type.CLOSE);

		}
		return action;
	}
}
