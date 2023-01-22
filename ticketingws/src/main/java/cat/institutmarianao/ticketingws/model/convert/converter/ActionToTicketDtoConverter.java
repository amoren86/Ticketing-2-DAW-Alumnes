package cat.institutmarianao.ticketingws.model.convert.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import cat.institutmarianao.ticketingws.model.Action;
import cat.institutmarianao.ticketingws.model.Opening;
import cat.institutmarianao.ticketingws.model.convert.exception.ActionToTicketDtoConversionException;
import cat.institutmarianao.ticketingws.model.dto.TicketDto;

public class ActionToTicketDtoConverter implements Converter<Action, TicketDto> {

	@Override
	public TicketDto convert(Action source) {
		if (!(source instanceof Opening opening)) {
			throw new ActionToTicketDtoConversionException("Only Opening actions can be converted into a TicketDto");
		}
		TicketDto ticketDto = new TicketDto();
		BeanUtils.copyProperties(opening.getTicket(), ticketDto);
		ticketDto.setId(opening.getTicket().getId());
		ticketDto.setPerformer(opening.getPerformer().getUsername());
		ticketDto.setOpeningDate(opening.getDate());
		return ticketDto;
	}

}
