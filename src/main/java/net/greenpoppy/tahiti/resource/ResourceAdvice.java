package net.greenpoppy.tahiti.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class ResourceAdvice {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<String> handleException(ResourceException ex)
        throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("type", ex.getType());
        responseBody.put("message", ex.getMessage());
        String responseBodyString = new ObjectMapper().writeValueAsString(responseBody);
        return ResponseEntity
            .status(ex.getHttpStatus())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(responseBodyString);
    }
}
