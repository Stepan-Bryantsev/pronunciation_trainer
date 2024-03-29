package com.pronunciation;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {

  private final String domain;
  private String token = null;
  private final OkHttpClient httpClient = new OkHttpClient.Builder()
      .connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .readTimeout(120, TimeUnit.SECONDS)
      .build();
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

  public void Register(Map<String, String> dataMap, AsyncResult<Boolean, ErrorResponse> result) {
    Request request = new Request.Builder()
        .url(domain + Settings.REGISTER_URL_PART)
        .post(buildPostBody(dataMap))
        .header("Content-Type", "application/json")
        .build();

    Call call = httpClient.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        if (response.code() == 201) {
          result.onSuccess(true);
        }
        else {
          result.onFail(new ErrorResponse(response));
        }
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(new ErrorResponse(null));
      }
    });
  }

  public void GetRandomWord(AsyncResult<Word, ErrorResponse> result) {
    if (token == null)
      throw new LoginException("No token.");
    Request request = new Request.Builder()
        .url(domain + Settings.RANDOM_WORD_URL_PART)
        .get()
        .header("Authorization", "Token " + token)
        .build();

    Call call = httpClient.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          try {
            result.onSuccess(gson.fromJson(response.body().string(), Word.class));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        else {
          result.onFail(new ErrorResponse(response));
        }
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(new ErrorResponse(null));
      }
    });
  }

  public void SearchWord(String search, AsyncResult<List<Word>, ErrorResponse> result) {
    if (token == null)
      throw new LoginException("No token.");
    Request request = new Request.Builder()
        .url(domain + String.format(Settings.SEARCH_WORD_URL_PART, search))
        .get()
        .header("Authorization", "Token " + token)
        .build();

    Call call = httpClient.newCall(request);
    call.enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          try {
            result.onSuccess(Arrays.asList(gson.fromJson(response.body().string(), Word[].class)));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        else {
          result.onFail(new ErrorResponse(response));
        }
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(new ErrorResponse(null));
      }
    });
  }

  public void GetCorrectAudio(String word, AsyncResult<ByteArrayHolder, ErrorResponse> result) {
    if (token == null)
      throw new LoginException("No token.");
    Request request = new Request.Builder()
        .url(domain + String.format(Settings.AUDIO_WORD_URL_PART, word))
        .header("Authorization", "Token " + token)
        .build();

    httpClient.newCall(request).enqueue(new Callback() {
      public void onResponse(Call call, Response response) throws IOException {
          result.onSuccess(new ByteArrayHolder(response.body().bytes()));
      }

      public void onFailure(Call call, IOException e) {
        result.onFail(null);
      }
    });
  }

  public void CheckPronunciation(String word, byte[] audioFile, AsyncResult<Double, ErrorResponse> result) {
    if (token == null)
      throw new LoginException("No token.");
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "file.mp3",
            RequestBody.create(MediaType.parse("audio/mp3; charset=utf-8"), audioFile))
        .build();

    Request request = new Request.Builder()
        .url(domain + String.format(Settings.CHECK_WORD_URL_PART, word))
        .addHeader("Authorization", "Token " + token)
        .post(requestBody)
        .build();

    httpClient.newCall(request).enqueue(new Callback() {
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          try {
            Double score = Double.parseDouble(gson.fromJson(response.body().string(), Map.class).get("score").toString());
            result.onSuccess(score);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        else {
          result.onFail(new ErrorResponse(response));
        }
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
    result.onSuccess("" + token);
  }
}