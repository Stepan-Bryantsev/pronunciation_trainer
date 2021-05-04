package com.pronunciation;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args)  {
        Client client = new Client("http://StepanBryantsev.pythonanywhere.com/api/v0/");

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("email_or_username", "roott");
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

//        Map<String, String> registrationMap = new HashMap<>();
//        registrationMap.put("username", "1");
//        registrationMap.put("first_name", "qwerty");
//        registrationMap.put("last_name", "qwerty");
//        registrationMap.put("email", "qwe1@qwe1.qwe1");
//        registrationMap.put("password", "qwerty.qwerty");
//
//        client.Register(registrationMap, new AsyncResult<Boolean, ErrorResponse>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                System.out.println(result);
//            }
//
//            @Override
//            public void onFail(ErrorResponse error) {
//                System.out.println(error.getErrors());
//            }
//        });
    }
}