package com.example.deezerpullingservice;

import com.example.deezerpullingservice.model.Album;
import com.example.deezerpullingservice.model.Artist;
import com.example.deezerpullingservice.model.Genre;
import com.example.deezerpullingservice.model.Track;
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
public class DeezerPullingService {

    private static final String ERROR = "{\"error\":{\"type\":\"DataException\",\"message\":\"no data\",\"code\":800}}";

    private final OkHttpClient okHttpClient;

    private final Gson gson;

    public CompletableFuture<Album> album(int id) {
        return this.request(Album.class, id);
    }

    public CompletableFuture<Artist> artist(int id) {
        return this.request(Artist.class, id);
    }

    public CompletableFuture<Genre> genre(int id) {
        return this.request(Genre.class, id);
    }

    public CompletableFuture<Track> track(int id) {
        return this.request(Track.class, id);
    }

    private <T> CompletableFuture<T> request(Class<T> tClass, int id) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        Request request = new Request.Builder()
                .url("https://api.deezer.com/" + tClass.getSimpleName().toLowerCase() + "/" + id)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error(request.toString(), e);
//                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    String string = responseBody.string();
                    if (string.equals(ERROR)) {
                        completableFuture.complete(null);
                        return;
                    }
                    T t = gson.fromJson(string, tClass);
                    completableFuture.complete(t);
                } catch (Exception e) {
                    log.error(request.toString(), e);
//                    completableFuture.completeExceptionally(e);
                }
            }
        });
        return completableFuture;
    }
}
