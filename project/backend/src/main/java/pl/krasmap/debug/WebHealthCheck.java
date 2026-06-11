package pl.krasmap.debug;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug")
public class WebHealthCheck {

    @GetMapping("/health")
    public String health_check() {
        return "system running";
    }

    @PostMapping("/post_test")
    public String test(@RequestBody String body) {
        System.out.println("DEBUG BODY: " + body);
        return body;
    }

    @GetMapping("/db_conn")
    public String db_check() {
        var db = new DatabaseCheck();
        return db.CheckDBConnection() ? "db working" : "ded";
    }
}
