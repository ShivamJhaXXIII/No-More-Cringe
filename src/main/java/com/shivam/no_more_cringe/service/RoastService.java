package com.shivam.no_more_cringe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Value("${groq.api-key}")
    private String apiKey;

    /**
     * The OpenAI API URL, injected from application properties.
     */
    @Value("${groq.api-url}")
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
        String prompt = """
You are a brutally honest resume reviewer. Your job is to roast resumes, point out flaws, and offer improvements — all with sarcasm, wit, and some real talk.

Given the following resume text, return your response strictly in this format:

Cringe Score: <a number from 1 to 100>

Roast:
<one or two paragraphs of roast — direct, humorous, but helpful>

Suggestions:
- <suggestion 1>
- <suggestion 2>
- <suggestion 3>
- ...

Important:
- Do NOT include extra headings or explanations.
- Do NOT wrap the output in JSON or markdown.
- Just return the raw text exactly in the format above.

Now, here is the resume:

""" + resumeText;


        // Prepare the request body for OpenAI API
        Map<String, Object> body = Map.of(
                "model", "llama3-8b-8192",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are resume critique expert who gives honest, spicy feedback."),
                        Map.of("role", "user", "content", prompt + "\n\n" + resumeText)
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

        

        // Extract roast, score, and suggestions from the response
        String messageContent = extractContentFromJson(result);
        String roast = extractRoast(messageContent);
        int score = extractScore(messageContent);
        String[] suggestion = extractSuggestion(messageContent);

        return new RoastResponse(score, roast, suggestion);
    }

    private String extractContentFromJson(String response) {
        System.out.println(response);
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);
            
            return root
            .path("choices")
            .get(0)
            .path("message")
            .path("content")
            .asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response ", e);
        }
    }

    /**
     * Extracts the roast text from the API response.
     * Currently returns the raw response; can be refined to parse JSON.
     *
     * @param response The raw API response.
     * @return The roast text.
     */
    private String extractRoast(String content) {
        Pattern pattern = Pattern.compile("(?i)roast:\\s*(.*?)\\s*(?=suggestions:)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
        return matcher.group(1).trim();
    }
    return "Roast not found — maybe the resume was too boring to roast.";
}


    /**
     * Extracts the cringe score (1-100) from the roast text using regex.
     * Returns 69 if no score is found.
     *
     * @param roastText The roast text from which to extract the score.
     * @return The cringe score.
     */
    private int extractScore(String content) {
    Pattern pattern = Pattern.compile("(?i)cringe score[:\\s]*(\\d{1,3})");
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
        return Integer.parseInt(matcher.group(1));
    }
    return 69; // Default to nice if missing
    }


    /**
     * Extracts suggestions from the roast text.
     * If "Suggestions:" is found, splits the following lines into an array.
     * Otherwise, returns default suggestions.
     *
     * @param roastText The roast text from which to extract suggestions.
     * @return Array of suggestion strings.
     */
    private String[] extractSuggestion(String content) {
        Pattern pattern = Pattern.compile("(?i)suggestions:\\s*((?:-\\s.*\\n?)+)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
        String raw = matcher.group(1).trim();
        return raw
            .lines()
            .map(String::trim)
            .filter(line -> line.startsWith("-"))
            .map(line -> line.substring(1).trim()) // remove "- "
            .toArray(String[]::new);
    }
    return new String[]{"No suggestions found. Resume might already be perfect?"};

}

}
