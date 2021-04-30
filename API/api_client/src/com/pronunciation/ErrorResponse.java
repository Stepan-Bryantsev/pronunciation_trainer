package com.pronunciation;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
  private final int statusCode;
  private final String message;
  private final Map<String, ArrayList<String>> errors;

  public ErrorResponse(HttpResponse<String> response) {
    if (response == null) {
      statusCode = 0;
      message = "No internet";
      errors = new HashMap<>();
    } else {
      statusCode = response.statusCode();
      message = "Error";
      Gson gson = new Gson();
      errors = gson.fromJson(response.body(), Map.class);
    }
  }

  public Map<String, ArrayList<String>> getErrors() {
    return errors;
  }

  public String getMessage() {
    return message;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
