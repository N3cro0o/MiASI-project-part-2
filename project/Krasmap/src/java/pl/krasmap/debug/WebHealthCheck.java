package pl.krasmap.debug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class WebHealthCheck {

    @GetMapping("/health")
    public String health_check() {
        return "system running";
    }


}
