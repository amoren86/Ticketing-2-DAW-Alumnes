package cat.institutmarianao.ticketingws.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import cat.institutmarianao.ticketingws.controllers.utils.SortParamsUtils;
import cat.institutmarianao.ticketingws.mocks.data.MockCompanies;
import cat.institutmarianao.ticketingws.mocks.data.MockRooms;
import cat.institutmarianao.ticketingws.mocks.data.MockUsers;
import cat.institutmarianao.ticketingws.model.Employee;
import cat.institutmarianao.ticketingws.model.Technician;
import cat.institutmarianao.ticketingws.model.User;
import cat.institutmarianao.ticketingws.model.dto.EmployeeDto;
import cat.institutmarianao.ticketingws.model.dto.TechnicianDto;
import cat.institutmarianao.ticketingws.model.dto.UserDto;
import cat.institutmarianao.ticketingws.services.ActionService;
import cat.institutmarianao.ticketingws.services.CompanyService;
import cat.institutmarianao.ticketingws.services.RoomService;
import cat.institutmarianao.ticketingws.services.TicketService;
import cat.institutmarianao.ticketingws.services.UserService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <-Allow BeforeAll not to be static
@AutoConfigureWebMvc
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {

//	private static final String ROOT_CONTENT = "$.content"; //WITH PAGINATION
	private static final String ROOT_CONTENT = "$"; // WITHOUT PAGINATION

	private static final String USERS_PATH = "/users";
	private static final String USERS_FIND_ALL_PATH = USERS_PATH + "/find/all";
	private static final String USERS_FIND_USER_BY_USERNAME = USERS_PATH + "/find/by/username/{username}";
	private static final String USERS_SAVE_PATH = USERS_PATH + "/save";
	private static final String USERS_DELETE_BY_USERNAME = USERS_PATH + "/delete/by/username/{username}";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TicketService ticketService;

	@MockBean
	private ActionService actionService;

	@MockBean
	private UserService userService;

	@MockBean
	private RoomService roomService;

	@MockBean
	private CompanyService companyService;

	@Autowired
	private ConversionService conversionService;

	@Nested
	@DisplayName("GET " + USERS_FIND_ALL_PATH)
	class FindAllUsers {

		@Nested
		@DisplayName("Without params")
		class WithoutParams {
			@Test
			@DisplayName("When there is a list of users to return...")
			void whenHasResultsShouldReturnAllUsersSortedByUsernameDesc() throws Exception {

				List<User> expectedUsers = new ArrayList<>(MockUsers.USERS);
				Collections.sort(expectedUsers, (u1, u2) -> u1.getUsername().compareTo(u2.getUsername()));

				// Mock service
				when(userService.findAll(null, null, SortParamsUtils.getOrders(new String[] { "username,asc" }), null,
						null)).thenReturn(page(expectedUsers));

				// Call controller and asserts
				ResultActions result = mockMvc.perform(get(USERS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedUsers.size())));
				checkJsonResults(result,
						expectedUsers.stream().map(u -> conversionService.convert(u, UserDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no users to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {
				// Mock service
				when(userService.findAll(null, null, SortParamsUtils.getOrders(new String[] { "username,asc" }), null,
						null)).thenReturn(page(new ArrayList<User>()));

				// Call controller and asserts
				mockMvc.perform(get(USERS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().is2xxSuccessful()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}

		@Nested
		@DisplayName("With page params")
		class WithPageParams {
			@Test
			@DisplayName("When there is a list of users to return...")
			void whenHasResultsShouldReturnAllUsersSortedByUsernameDesc() throws Exception {

				List<User> expectedUsers = new ArrayList<>(MockUsers.USERS);
				Collections.sort(expectedUsers, (u1, u2) -> u1.getUsername().compareTo(u2.getUsername()));

				int page = 1;
				int size = MockUsers.USERS.size() / 2 + 1;

				expectedUsers = expectedUsers.subList(size, expectedUsers.size() - 1);

				// Mock service
				when(userService.findAll(page, size, SortParamsUtils.getOrders(new String[] { "username,asc" }), null,
						null)).thenReturn(page(expectedUsers));

				// Call controller and asserts
				ResultActions result = mockMvc
						.perform(get(USERS_FIND_ALL_PATH).param("page", String.valueOf(page))
								.param("size", String.valueOf(size)).accept(MediaType.APPLICATION_JSON))
						.andDo(print()).andExpect(status().isOk())
						.andExpect(jsonPath(ROOT_CONTENT, hasSize(expectedUsers.size())));
				checkJsonResults(result,
						expectedUsers.stream().map(u -> conversionService.convert(u, UserDto.class)).toList());
			}

			@Test
			@DisplayName("When there is no users to return...")
			void whenHasNoResultsShouldReturnEmptyContent() throws Exception {

//				int page = 1;
//				int size = MockUsers.USERS.size() / 2 + 1;

				// Mock service
				when(userService.findAll(null, null, SortParamsUtils.getOrders(new String[] { "username,asc" }), null,
						null)).thenReturn(page(new ArrayList<User>()));

				// Call controller and asserts
				mockMvc.perform(get(USERS_FIND_ALL_PATH).accept(MediaType.APPLICATION_JSON)).andDo(print())
						.andExpect(status().isOk()).andExpect(jsonPath(ROOT_CONTENT, hasSize(0)));
			}
		}
		// TODO - With sort params
		// TODO - With multiple sort params
		// TODO - With page and sort params
	}

	@Nested
	@DisplayName("GET " + USERS_FIND_USER_BY_USERNAME)
	class FindUserByUsername {
		@Test
		@DisplayName("When there is a user with this username...")
		void whenHasResultShouldReturnTheUser() throws Exception {

			int random = new Random().nextInt(MockUsers.USERS.size());
			User expectedUser = MockUsers.USERS.get(random);
			Optional<User> returnedUser = Optional.of(expectedUser);

			// Mock service
			when(userService.findByUsername(expectedUser.getUsername())).thenReturn(returnedUser);

			// Call controller and asserts
			ResultActions result = mockMvc.perform(
					get(USERS_FIND_USER_BY_USERNAME, expectedUser.getUsername()).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isOk());

			checkJsonResult(result, conversionService.convert(expectedUser, UserDto.class));
		}

		@Test
		@DisplayName("When there is no users with this username...")
		void whenHasNoResultShouldReturnNull() throws Exception {

			String username = RandomString.make(20);
			Optional<User> returnedUser = Optional.ofNullable(null);

			// Mock service
			when(userService.findByUsername(username)).thenReturn(returnedUser);

			// Call controller and asserts
			mockMvc.perform(get(USERS_FIND_USER_BY_USERNAME, username).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is2xxSuccessful()).andExpect(content().string("null"));
		}
	}

	// TODO Filter test

	@Nested
	@DisplayName("POST " + USERS_SAVE_PATH)
	class Save {

		// Use this mapper to serialize the password
		private ObjectMapper objectMapper = JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS).build();

		@Test
		@DisplayName("When save a user...")
		void whenSaveShouldReturnTheUser() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			// Mock service
			when(userService.save(user)).thenReturn(user);

			// Call controller and asserts
			ResultActions result = mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8).content(objectMapper.writeValueAsString(dtoUser))
					.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful());
			checkJsonResult(result, dtoUser);
		}

		@Test
		@DisplayName("When save a user without username")
		void whenSaveWithoutUsernameShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setUsername(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user without role")
		void whenSaveWithoutRoleShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setRole(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user without password")
		void whenSaveWithoutPasswordShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setPassword(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user without full name")
		void whenSaveWithoutFullNameShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setFullName(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user without extension")
		void whenSaveWithoutExtensionShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setExtension(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user with extension less than 0")
		void whenSaveWithExtensionLessThanZeroShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setExtension(-user.getExtension());

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a user with extension over MAX_EXTENSION")
		void whenSaveWithExtensionOverMaxExtensionShouldReturn4xxError() throws Exception {
			// Prepare data
			User user = MockUsers.generateRandomUser();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			when(companyService.findById(any())).thenReturn(Optional.of(MockCompanies.generateRandomCompany()));
			UserDto dtoUser = conversionService.convert(user, UserDto.class);

			dtoUser.setExtension(User.MAX_EXTENSION + 1);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save an employee without room")
		void whenSaveEmployeeWithoutRoomShouldReturn4xxError() throws Exception {
			// Prepare data
			Employee user = MockUsers.generateRandomEmployee();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			EmployeeDto dtoUser = (EmployeeDto) conversionService.convert(user, UserDto.class);

			dtoUser.setRoomId(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save an employee without place")
		void whenSaveEmployeeWithoutPlaceShouldReturn4xxError() throws Exception {
			// Prepare data
			Employee user = MockUsers.generateRandomEmployee();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			EmployeeDto dtoUser = (EmployeeDto) conversionService.convert(user, UserDto.class);

			dtoUser.setPlace(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}

		@Test
		@DisplayName("When save a technician without company")
		void whenSaveTechnicianWithoutCompanyShouldReturn4xxError() throws Exception {
			// Prepare data
			Technician user = MockUsers.generateRandomTechnician();

			when(roomService.findById(any())).thenReturn(Optional.of(MockRooms.generateRandomRoom()));
			TechnicianDto dtoUser = (TechnicianDto) conversionService.convert(user, UserDto.class);

			dtoUser.setCompanyId(null);

			// Call controller and asserts
			mockMvc.perform(post(USERS_SAVE_PATH).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dtoUser)).accept(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().is4xxClientError());
		}
	}

	// TODO Update tests
	@Nested
	@DisplayName("DELETE " + USERS_DELETE_BY_USERNAME)
	class DeleteByUsername {
		@Test
		@DisplayName("When delete by username...")
		void shoudReturn2xxOk() throws Exception {
			String username = RandomString.make(20);
			mockMvc.perform(delete(USERS_DELETE_BY_USERNAME, username).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is2xxSuccessful());
		}
	}

	private void checkJsonResults(ResultActions result, List<UserDto> sortedUsers) throws Exception {
		for (int i = 0; i < sortedUsers.size(); i++) {
			UserDto user = sortedUsers.get(i);
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].username", equalTo(user.getUsername())));
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].role", equalTo(user.getRole().toString())));
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].password").doesNotExist());
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].fullName", equalTo(user.getFullName())));
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].extension", equalTo(user.getExtension())));
//			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].room").doesNotExist());
//			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].company").doesNotExist());
			result = result.andExpect(jsonPath(ROOT_CONTENT + "[" + i + "].location", equalTo(user.getLocation())));
		}
	}

	private void checkJsonResult(ResultActions result, UserDto user) throws Exception {
		result = result.andExpect(jsonPath("$.username", equalTo(user.getUsername())));
		result = result.andExpect(jsonPath("$.role", equalTo(user.getRole().toString())));
		result = result.andExpect(jsonPath("$.password").doesNotExist());
		result = result.andExpect(jsonPath("$.fullName", equalTo(user.getFullName())));
		result = result.andExpect(jsonPath("$.extension", equalTo(user.getExtension())));
//		result = result.andExpect(jsonPath("$.room").doesNotExist());
//		result = result.andExpect(jsonPath("$.company").doesNotExist());
		result = result.andExpect(jsonPath("$.location", equalTo(user.getLocation())));
	}

//	private Page<User> page(List<User> content) {
//		return new PageImpl<>(content);
//	}

	private List<User> page(List<User> content) {
		return content;
	}
}