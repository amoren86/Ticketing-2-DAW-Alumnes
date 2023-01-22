package cat.institutmarianao.ticketingws.model.convert.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Assignment;
import cat.institutmarianao.ticketingws.model.Close;
import cat.institutmarianao.ticketingws.model.Intervention;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.dto.ActionDto;
import cat.institutmarianao.ticketingws.model.dto.AssignmentDto;
import cat.institutmarianao.ticketingws.model.dto.CloseDto;
import cat.institutmarianao.ticketingws.model.dto.InterventionDto;
import cat.institutmarianao.ticketingws.model.dto.OpeningDto;

public class ActionToActionDtoConverter implements Converter<Action, ActionDto> {

	@Override
	public ActionDto convert(Action source) {
		if (source instanceof Opening opening) {
			OpeningDto openingDto = new OpeningDto();
			copyCommonProperties(opening, openingDto);
			return openingDto;
		}
		if (source instanceof Assignment assignment) {
			AssignmentDto assignmentDto = new AssignmentDto();
			copyCommonProperties(assignment, assignmentDto);
			assignmentDto.setTechnician((assignment.getTechnician().getUsername()));
			assignmentDto.setPriority(assignment.getPriority());
			return assignmentDto;
		}
		if (source instanceof Intervention intervention) {
			InterventionDto interventionDto = new InterventionDto();
			copyCommonProperties(intervention, interventionDto);
			return interventionDto;
		}
		if (source instanceof Close close) {
			CloseDto closeDto = new CloseDto();
			copyCommonProperties(close, closeDto);
			return closeDto;
		}
		return null;
	}

	private void copyCommonProperties(Action action, ActionDto actionDto) {
		BeanUtils.copyProperties(action, actionDto);
		actionDto.setPerformer(action.getPerformer().getUsername());
		if (action.getTicket() != null) {
			actionDto.setTicketId(action.getTicket().getId());
		}
	}
}
