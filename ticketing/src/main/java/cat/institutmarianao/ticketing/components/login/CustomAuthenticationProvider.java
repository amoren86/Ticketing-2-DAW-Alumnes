package cat.institutmarianao.ticketing.components.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cat.institutmarianao.ticketing.model.dto.UserDto;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private static final String HOST = "https://localhost";
	private static final String PORT = "8443";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${exception.badCredentials}")
	private String badCredentialsMessage;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String uri = HOST + ":" + PORT + "/users/authenticate";

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> postBody = new HashMap<>();
		postBody.put("username", username);
		postBody.put("password", password);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(postBody, headers);

		UserDto userDetails = restTemplate.postForObject(uri, request, UserDto.class);
		if (userDetails == null) {
			throw new BadCredentialsException(badCredentialsMessage);
		}

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
