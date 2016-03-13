package helloworld.service;


import java.util.Date;

import javax.ws.rs.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldCompositeService.class);

    @Autowired
    Util util;

    @RequestMapping("/")
    public String mHello() {
        return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Hello Service\"}";
    }

    @GET
    @RequestMapping("/hello/{user}")
    public ResponseEntity<String> mSayHello(@PathVariable String user) {

       
        return util.createOkResponse(new String("Hello "+user));
    }
}
