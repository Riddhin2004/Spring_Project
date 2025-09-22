package com.riddhinaik.ProjectJava;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
       
        String regNo = "0187CS221167"; 
        Map<String, Object> webhookResponse = generateWebhook("Riddhi Naik", regNo, "riddhinaik103@gmail.com");

        String webhookUrl = (String) webhookResponse.get("webhookUrl");
        String accessToken = (String) webhookResponse.get("accessToken");

        
        String finalQuery = solveSqlProblem(regNo);

        
        submitSolution(webhookUrl, accessToken, finalQuery);
    }

    private Map<String, Object> generateWebhook(String name, String regNo, String email) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = Map.of(
            "name", name,
            "regNo", regNo,
            "email", email
        );

        return restTemplate.postForObject(url, requestBody, Map.class);
    }

    private String solveSqlProblem(String regNo) {
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int number = Integer.parseInt(lastTwoDigits);

        if (number % 2 != 0) {
            
            return "YOUR_SQL_QUERY_FOR_ODD_REGNO";
        } else {
            
            return "YOUR_SQL_QUERY_FOR_EVEN_REGNO";
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(webhookUrl, request, Void.class);
        System.out.println("Solution submitted successfully!");
    }
}