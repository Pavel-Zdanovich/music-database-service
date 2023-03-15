package com.example.deezerpullingservice.musicbrainz;

import com.example.deezerpullingservice.musicbrainz.model.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MusicBrainzService {

    private final OkHttpClient okHttpClient;

    private final Gson gson;

    public <T> CompletableFuture<T> getAsync(Class<T> tClass, String text) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        Request request = request(tClass, text);
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
                    Page<T> page = gson.fromJson(string, new TypeToken<>() {});
                    List<T> list = page.get(tClass);
                    completableFuture.complete(list.get(0));
                } catch (Exception e) {
                    log.error(request.toString(), e);
                    completableFuture.complete(null);
                }
            }
        });
        return completableFuture;
    }

    private <T> Request request(Class<T> tClass, String text) {
        String url = new StringBuilder("https://musicbrainz.org/ws/2/")
                .append(tClass.getSimpleName().toLowerCase())
                .append("?query=").append(URLEncoder.encode(text, StandardCharsets.UTF_8))
                .append("&limit=").append(1)
                .toString();
        return new Request.Builder()
                .get()
                .url(url)
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
