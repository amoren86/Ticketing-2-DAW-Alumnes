package cat.institutmarianao.ticketingws.model.convert.converter;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.TicketDto;
import cat.institutmarianao.ticketingws.services.UserService;

@Component
public class TicketDtoToTicketConverter implements Converter<TicketDto, Ticket> {
	@Autowired
	private UserService userService;

	@Override
	public Ticket convert(TicketDto source) {
		if (source == null) {
			return null;
		}
		User storedUser = userService.getUserByUsername(source.getPerformer());

		Ticket ticket = new Ticket();

		BeanUtils.copyProperties(source, ticket);

		ticket.setTracking(new ArrayList<>());

		Opening opening = new Opening();
		opening.setDate(source.getOpeningDate());
		opening.setPerformer(null);
		opening.setType(Action.Type.OPENING);
		opening.setPerformer(storedUser);
		opening.setTicket(ticket);
		ticket.getTracking().add(opening);

		return ticket;
	}

}
