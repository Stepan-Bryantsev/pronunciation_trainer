package com.pronunciation;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {

  private final String domain;
  private String token = null;
  private final OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
  Gson gson = new Gson();

  public Client(String domain) {
    this.domain = domain;
  }

  public Client(String domain, String token) {
    this.domain = domain;
    this.token = token;
  }

  private RequestBody buildPostBody(Map<String, String> dataMap) {
    Builder formBodyBuilder = new FormBody.Builder();
    for (String key : dataMap.keySet()) {
      formBodyBuilder.add(key, dataMap.get(key));
    }
    return formBodyBuilder.build();
  }

  public void Login(Map<String, String> dataMap, AsyncResult<String, ErrorResponse> result) {
    Request request = new Request.Builder()
        .url(domain + Settings.LOGIN_URL_PART)
        .post(buildPostBody(dataMap))
        .header("Content-Type", "application/json")
        .build();

    Call call = httpClient.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        acceptLoginResponse(response, result);
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(new ErrorResponse(null));
      }
    });
  }

  private void acceptLoginResponse(Response response, AsyncResult<String, ErrorResponse> result) {
    if (response.code() != 200) {
      result.onFail(new ErrorResponse(response));
      return;
    }
    String token = null;
    try {
      token = gson.fromJson(response.body().string(), Map.class).get("token").toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.token = token;
    result.onSuccess(token);
  }

  public void Register(Map<String, String> dataMap, AsyncResult<Boolean, ErrorResponse> result) {
    Request request = new Request.Builder()
        .url(domain + Settings.REGISTER_URL_PART)
        .post(buildPostBody(dataMap))
        .header("Content-Type", "application/json")
        .build();

    Call call = httpClient.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        result.onSuccess(true);
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(new ErrorResponse(null));
      }
    });
  }
}