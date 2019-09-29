package ru.otus.salamandra.app.service;

import com.google.gson.Gson;
import okhttp3.*;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.dto.UserDto;

import java.io.IOException;

public class AuthService {

    private static final AuthService instance = new AuthService();

    private final String SERVER_URL = "http://localhost:8080";

    private AuthService() {
    }

    public static AuthService getInstance() {
        return instance;
    }

    public synchronized void signIn(String login, String password, String baseDir) {
        String json = new Gson().toJson(
                new UserDto(login, password)
        );
        Response response = postToServer("/auth/signIn", json);
        if (response != null && response.code() == 200) {
            if (response.code() == 200) {
                postprocessingResponse(login, baseDir);
                return;
            }
        }
        throw new IllegalStateException("Sign in error");
    }

    public synchronized void register(String login, String password, String baseDir) {
        String json = new Gson().toJson(
                new UserDto(login, password)
        );
        Response response = postToServer("/auth/register", json);
        if (response != null && response.code() == 200) {
            if (response.code() == 200) {
                postprocessingResponse(login, baseDir);
                return;
            }
        }
        throw new IllegalStateException("Register error");
    }

    private void postprocessingResponse(String login, String baseDir) {
        SessionStore.getInstance().setUserLogin(login);
        SessionStore.getInstance().setBaseDirPath(baseDir);
    }

    private Response postToServer(String requestPath, String json) {
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(SERVER_URL + requestPath)
                    .post(body)
                    .build();
            return client.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Post error: \"" + e.getMessage() + "\"");
        }
        return null;
    }


}
