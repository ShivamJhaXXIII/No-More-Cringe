package com.shivam.no_more_cringe.service;

import com.shivam.no_more_cringe.model.RoastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for sending resume text to OpenAI API for roasting,
 * extracting the roast, cringe score, and suggestions from the response.
 */
@Service
public class RoastService {

    /**
     * The OpenAI API key, injected from application properties.
     */
    @Value("${openai.api-key}")
    private String apiKey;

    /**
     * The OpenAI API URL, injected from application properties.
     */
    @Value("${openai.api-url}")
    private String apiUrl;

    /**
     * WebClient instance for making HTTP requests.
     */
    private final WebClient webClient = WebClient.builder().build();

    /**
     * Sends the resume text to the OpenAI API, receives the roast,
     * extracts the cringe score and suggestions, and returns a RoastResponse.
     *
     * @param resumeText The plain text of the resume to roast.
     * @return RoastResponse containing the score, roast, and suggestions.
     */
    public RoastResponse roastResume(String resumeText) {
        // Build the prompt for the AI model
        String prompt = "Roast this resume brutally. Rate it 1-100 for cringe. Suggest improvements:\n\n" + resumeText;

        // Prepare the request body for OpenAI API
        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are resume critique expert who gives honest feedback."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        // Send POST request to OpenAI API and get the response as a string
        String result = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Loaded api key " + apiKey);

        // Extract roast, score, and suggestions from the response
        String roast = extractRoast(result);
        int score = extractScore(roast);
        String[] suggestion = extractSuggestion(roast);

        return new RoastResponse(score, roast, suggestion);
    }

    /**
     * Extracts the roast text from the API response.
     * Currently returns the raw response; can be refined to parse JSON.
     *
     * @param response The raw API response.
     * @return The roast text.
     */
    private String extractRoast(String response) {
        return response; //refine later
    }

    /**
     * Extracts the cringe score (1-100) from the roast text using regex.
     * Returns 69 if no score is found.
     *
     * @param roastText The roast text from which to extract the score.
     * @return The cringe score.
     */
    private int extractScore(String roastText) {
        Pattern pattern = Pattern.compile("\\b([1-9][0-9]?|100)\\b");
        Matcher matcher = pattern.matcher(roastText);

        return matcher.find() ? Integer.parseInt(matcher.group()) : 69;
    }

    /**
     * Extracts suggestions from the roast text.
     * If "Suggestions:" is found, splits the following lines into an array.
     * Otherwise, returns default suggestions.
     *
     * @param roastText The roast text from which to extract suggestions.
     * @return Array of suggestion strings.
     */
    private String[] extractSuggestion(String roastText) {
        if (roastText.contains("Suggestions:")) {
            return roastText.split("Suggestions:")[1].trim().split("\n");
        }
        return new String[]{"Add more relevant skills.", "Improve formatting"};
    }
}
