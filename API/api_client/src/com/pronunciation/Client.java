package com.pronunciation;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

public class Client {

  private final String domain;
  private String token = null;
  private HttpClient httpClient = HttpClient.newHttpClient();
  Gson gson = new Gson();

  public Client(String domain) {
    this.domain = domain;
  }

  public Client(String domain, String token) {
    this.domain = domain;
    this.token = token;
  }

  public void Login(Map<String, String> dataMap, AsyncResult<String, ErrorResponse> result) {
    String data = gson.toJson(dataMap);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(domain + Settings.LOGIN_URL_PART))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(data)).build();

    httpClient.sendAsync(request, BodyHandlers.ofString())
        .exceptionally(e -> null)
        .thenAccept((response) -> acceptLoginResponse(response, result))
        .join();
  }

  private void acceptLoginResponse(HttpResponse<String> httpResponse, AsyncResult<String, ErrorResponse> result) {
    if (httpResponse == null || httpResponse.statusCode() != 200) {
      result.onFail(new ErrorResponse(httpResponse));
      return;
    }
    String token = gson.fromJson(httpResponse.body(), Map.class).get("token").toString();
    this.token = token;
    result.onSuccess(token);
  }

  public void Register(Map<String, String> dataMap, AsyncResult<Boolean, ErrorResponse> result) {
    String data = gson.toJson(dataMap);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(domain + Settings.REGISTER_URL_PART))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(data)).build();

    httpClient.sendAsync(request, BodyHandlers.ofString())
        .exceptionally(e -> null)
        .thenAccept((response) -> acceptRegisterResponse(response, result))
        .join();
  }

  private void acceptRegisterResponse(HttpResponse<String> httpResponse, AsyncResult<Boolean, ErrorResponse> result) {
    if (httpResponse == null || httpResponse.statusCode() != 201) {
      result.onFail(new ErrorResponse(httpResponse));
      return;
    }
    result.onSuccess(true);
  }
}