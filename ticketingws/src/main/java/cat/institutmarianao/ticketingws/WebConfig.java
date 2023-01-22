package cat.institutmarianao.ticketingws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cat.institutmarianao.ticketingws.model.convert.converter.ActionDtoToActionConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.ActionToActionDtoConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.ActionToTicketDtoConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.TicketDtoToTicketConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.TicketToTicketDtoConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.UserDtoToUserConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.UserToUserDtoConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private ActionDtoToActionConverter actionDtoToActionConverter;

	@Autowired
	private TicketDtoToTicketConverter ticketDtoToTicketConverter;

	@Autowired
	private UserDtoToUserConverter userDtoToUserConverter;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(actionDtoToActionConverter);
		registry.addConverter(new ActionToActionDtoConverter());
		registry.addConverter(new ActionToTicketDtoConverter());
		registry.addConverter(ticketDtoToTicketConverter);
		registry.addConverter(new TicketToTicketDtoConverter());
		registry.addConverter(new UserToUserDtoConverter());
		registry.addConverter(userDtoToUserConverter);
	}
}