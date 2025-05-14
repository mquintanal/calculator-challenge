package com.mmql.calculator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmql.calculator.exception.EmailValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class EmailValidationService {

    @Value("${external.api.abstract.url}")
    private String apiUrl;

    @Value("${external.api.abstract.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EmailValidationService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void validateEmailOrThrow(String email) {
        String requestUrl = buildRequestUrl(email);

        try {
            String responseBody = restTemplate.getForObject(requestUrl, String.class);
            JsonNode json = objectMapper.readTree(responseBody);

            boolean validFormat = isFormatValid(json);
            boolean notDisposable = isNotDisposable(json);
            boolean mxFound = isMxFound(json);
            boolean deliverable = isDeliverable(json);

            if (!validFormat || !notDisposable || !mxFound || !deliverable) {
                StringBuilder sb = new StringBuilder("Email validation failed due to:");

                if (!validFormat) sb.append(" invalid format;");
                if (!notDisposable) sb.append(" disposable email;");
                if (!mxFound) sb.append(" MX record not found;");
                if (!deliverable) sb.append(" undeliverable address;");

                throw new EmailValidationException(sb.toString().trim());
            }

        } catch (EmailValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during email validation: {}", e.getMessage());
            throw new EmailValidationException("Unable to validate email due to system error.");
        }
    }

    private String buildRequestUrl(String email) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("api_key", apiKey)
                .queryParam("email", email)
                .toUriString();
    }

    private boolean isFormatValid(JsonNode json) {
        return json.path("is_valid_format").path("value").asBoolean();
    }

    private boolean isNotDisposable(JsonNode json) {
        return !json.path("is_disposable_email").path("value").asBoolean();
    }

    private boolean isMxFound(JsonNode json) {
        return json.path("is_mx_found").path("value").asBoolean();
    }

    private boolean isDeliverable(JsonNode json) {
        return "DELIVERABLE".equalsIgnoreCase(json.path("deliverability").asText());
    }
}