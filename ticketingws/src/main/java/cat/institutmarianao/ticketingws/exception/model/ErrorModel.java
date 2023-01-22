package cat.institutmarianao.ticketingws.exception.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class ErrorModel {

	private Map<String, Object> body;

	public ErrorModel(HttpStatus status, String error) {
		List<String> errors = new ArrayList<>();
		errors.add(error);
		setBody(status, errors);
	}

	public ErrorModel(HttpStatus status, BindingResult bindingResult) {
		List<String> errors = bindingResult.getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage()).toList();
		setBody(status, errors);
	}

	public Map<String, Object> getBody() {
		return body;
	}

	private void setBody(HttpStatus status, List<String> errors) {
		body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());
		body.put("errors", errors);
	}
}