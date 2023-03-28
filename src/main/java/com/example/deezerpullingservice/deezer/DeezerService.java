package com.example.deezerpullingservice.deezer;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeezerService {

    private static final String ERROR = "{\"error\"";

    private final OkHttpClient okHttpClient;

    private final Gson gson;

    public <T> CompletableFuture<T> getAsync(Class<T> tClass, int id) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        Request request = request(tClass, id);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error(request.toString(), e);
                completableFuture.complete(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    String string = responseBody.string();
                    if (string.startsWith(ERROR)) {
                        completableFuture.complete(null);
                        return;
                    }
                    T t = gson.fromJson(string, tClass);
                    completableFuture.complete(t);
                } catch (Exception e) {
                    log.error(request.toString(), e);
                    completableFuture.complete(null);
                }
            }
        });
        return completableFuture;
    }

    private <T> Request request(Class<T> tClass, int id) {
        return new Request.Builder()
                .get()
                .url("https://api.deezer.com/" + tClass.getSimpleName().toLowerCase() + "/" + id)
                .build();
    }
}
