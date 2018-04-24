package net.greenpoppy.tahiti.resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


@RestController
public class InfoResource
    extends BaseResource {
    @GetMapping(value = API_PREFIX + "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getInfo() {
        return "Tahiti";
    }
}
