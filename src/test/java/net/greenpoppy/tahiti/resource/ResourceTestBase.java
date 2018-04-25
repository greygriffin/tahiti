package net.greenpoppy.tahiti.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class ResourceTestBase {
    protected static final String LOCATION_HEADER = "Location";

    protected static final MediaType APPLICATION_JSON_UTF8 =
        new MediaType(MediaType.APPLICATION_JSON.getType(),
                      MediaType.APPLICATION_JSON.getSubtype(),
                      StandardCharsets.UTF_8);
    protected static final MediaType APPLICATION_MERGE_PATCH_JSON_UTF8 =
        new MediaType("application",
              "merge-patch+json",
                       StandardCharsets.UTF_8);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected String json(Object obj)
        throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T fromJson(Class<T> aClass, String json) throws IOException {
        return objectMapper.readValue(json, aClass);
    }
}
