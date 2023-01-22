package cat.institutmarianao.ticketingws.model.convert.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import cat.institutmarianao.ticketingws.model.Ticket;
import cat.institutmarianao.ticketingws.model.dto.TicketDto;

public class TicketToTicketDtoConverter implements Converter<Ticket, TicketDto> {

	@Override
	public TicketDto convert(Ticket source) {
		if (source == null) {
			return null;
		}
		TicketDto ticketDto = new TicketDto();
		BeanUtils.copyProperties(source, ticketDto);
		ticketDto.setPerformer(source.getTracking().get(0).getPerformer().getUsername());
		ticketDto.setOpeningDate(source.getTracking().get(0).getDate());
		return ticketDto;
	}

}
