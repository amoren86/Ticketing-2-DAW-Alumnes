package cat.institutmarianao.ticketing.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping(value = "/")
	public String root(HttpServletRequest request) throws ServletException, IOException {
		return "redirect:/check";
	}

	@GetMapping(value = "/check")
	public String checkLogin(HttpServletRequest request) throws ServletException, IOException {
		return "redirect:/tickets/list/PENDING";
	}

	@GetMapping(value = "/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/loginfailed")
	public String loginFailed(Model model) {
		model.addAttribute("error", "true");
		return "login";
	}

	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		request.logout();
		return "redirect:/";
	}
}
