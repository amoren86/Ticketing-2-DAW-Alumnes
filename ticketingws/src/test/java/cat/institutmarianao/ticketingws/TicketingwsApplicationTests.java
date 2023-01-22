package cat.institutmarianao.ticketingws;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;

import cat.institutmarianao.ticketingws.model.convert.converter.ActionDtoToActionConverter;
import cat.institutmarianao.ticketingws.model.convert.converter.TicketDtoToTicketConverter;
import cat.institutmarianao.ticketingws.repositories.ActionRepository;
import cat.institutmarianao.ticketingws.repositories.TicketRepository;
import cat.institutmarianao.ticketingws.repositories.UserRepository;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;

@SpringBootTest
class TicketingwsApplicationTests {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ActionRepository actionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private ActionDtoToActionConverter actionDtoToActionConverter;

	@Autowired
	private TicketDtoToTicketConverter ticketDtoToTicketConverter;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource messageSource;

	@Test
	void contextLoads() {
		assertThat(ticketService).isNotNull();
		assertThat(userService).isNotNull();
		assertThat(ticketRepository).isNotNull();
		assertThat(actionRepository).isNotNull();
		assertThat(userRepository).isNotNull();
		assertThat(conversionService).isNotNull();
		assertThat(actionDtoToActionConverter).isNotNull();
		assertThat(ticketDtoToTicketConverter).isNotNull();
		assertThat(modelMapper).isNotNull();
		assertThat(messageSource).isNotNull();
	}

}
