package garage.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
public class EndpointDocControler {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@RequestMapping( value = "/endpoints", method = RequestMethod.GET )
	public String getEndPointsInView( Model model )
	{
	    model.addAttribute( "endPoints", requestMappingHandlerMapping.getHandlerMethods().keySet() );
	    return model.asMap().toString();
	}
}