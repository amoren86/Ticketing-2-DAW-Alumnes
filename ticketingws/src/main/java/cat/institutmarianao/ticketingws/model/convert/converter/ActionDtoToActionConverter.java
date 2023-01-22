package cat.institutmarianao.ticketingws.model.convert.converter;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Assignment;
import cat.institutmarianao.ticketingws.model.Close;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.ActionDto;
import cat.institutmarianao.ticketingws.model.dto.AssignmentDto;
import cat.institutmarianao.ticketingws.model.dto.CloseDto;
import cat.institutmarianao.ticketingws.model.dto.InterventionDto;
import cat.institutmarianao.ticketingws.model.dto.OpeningDto;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;

@Component
public class ActionDtoToActionConverter implements Converter<ActionDto, Action> {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Override
	public Action convert(ActionDto actionDto) {
		if (actionDto == null) {
			return null;
		}

		if (actionDto instanceof OpeningDto openingDto) {
			Opening opening = new Opening();
			copyCommonProperties(openingDto, opening);
			return opening;
		}
		if (actionDto instanceof AssignmentDto assignmentDto) {
			Assignment assignment = new Assignment();
			copyCommonProperties(assignmentDto, assignment);
			User user = userService.getUserByUsername(assignmentDto.getTechnician());
			if (user instanceof Technician technician) {
				assignment.setTechnician(technician);
			}
			return assignment;
		}
		if (actionDto instanceof InterventionDto interventionDto) {
			Intervention intervention = new Intervention();
			copyCommonProperties(interventionDto, intervention);
			return intervention;
		}
		if (actionDto instanceof CloseDto closeDto) {
			Close close = new Close();
			copyCommonProperties(closeDto, close);
			return close;
		}
		return null;
	}

	private void copyCommonProperties(ActionDto actionDto, Action action) {
		BeanUtils.copyProperties(actionDto, action);
		action.setDate(actionDto.getDate());
		User performer = userService.getUserByUsername(actionDto.getPerformer());
		action.setPerformer(performer);
		Optional<Ticket> ticket = ticketService.findById(actionDto.getTicketId());
		if (!ticket.isEmpty()) {
			action.setTicket(ticket.get());
		}
	}
}
