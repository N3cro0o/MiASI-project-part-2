package pl.krasmap.submission.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import org.springframework.stereotype.Service;
import pl.krasmap.submission.application.domain.Krasnal;

@Service
public class CheckSubmission {

    public Krasnal GenerateKrasnalFromJson(String json) {
        Krasnal obj = null;
        try {
            obj = Json.mapper().readValue(json, Krasnal.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.toString());
        }
        return obj;
    }
}
