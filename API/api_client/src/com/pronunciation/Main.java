package com.pronunciation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args)  {
        Client client = new Client("http://StepanBryantsev.pythonanywhere.com/api/v0/");

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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.GetCorrectAudio("crazy", new AsyncResult<List<Word>, ErrorResponse>() {
            @Override
            public void onSuccess(List<Word> result) {

            }

            @Override
            public void onFail(ErrorResponse error) {

            }
        });
    }
}