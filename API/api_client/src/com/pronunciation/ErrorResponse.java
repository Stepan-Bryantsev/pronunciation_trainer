package com.pronunciation;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

public class ErrorResponse {
  private final int statusCode;
  private final String message;
  private Map<String, ArrayList<String>> errors = null;

  public ErrorResponse(Response response) {
    if (response == null) {
      statusCode = 0;
      message = "No internet";
      errors = new HashMap<>();
    } else if (response.code() == 400) {
      statusCode = response.code();
      message = "Validation error";
      Gson gson = new Gson();
      try {
        errors = gson.fromJson(response.body().string(), Map.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    else {
      statusCode = 1;
      message = "Server error";
      errors = new HashMap<>();
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
