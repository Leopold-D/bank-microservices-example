package helloworld.service;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping("/hello")
    public ResponseEntity<String> mSayHello() {

       
        return util.createOkResponse(new String("Helloworld"));
    }
}
