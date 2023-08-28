package com.example.cameraapplication;

public class Request {

    public void post(String serviceLink,String data){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceLink))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }
}
