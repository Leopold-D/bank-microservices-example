package garage.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
public class EndpointDocControler {
	private final RequestMappingHandlerMapping aHandlerMapping;

	@Autowired
	public EndpointDocControler(RequestMappingHandlerMapping handlerMapping) {
		this.aHandlerMapping = handlerMapping;
	}

	@RequestMapping(value = "/endpointdoc", method = RequestMethod.GET)
	public void show(Model model) {
		model.addAttribute("handlerMethods", this.aHandlerMapping.getHandlerMethods());
	}
}