package com.pronunciation;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args)  {
        Client client = new Client("http://127.0.0.1:8000/api/v0/");

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email_or_username", "root");
        loginMap.put("password", "toor");

        client.Login(loginMap, new AsyncResult<>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFail(ErrorResponse error) {
                System.out.println(error.getMessage());
                System.out.println(error.getStatusCode());
                System.out.println(error.getErrors());
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.CheckPronunciation("hello", "crazy.mp3", new AsyncResult<Integer, ErrorResponse>() {
            @Override
            public void onSuccess(Integer result) {

            }

            @Override
            public void onFail(ErrorResponse error) {

            }
        });
    }
}