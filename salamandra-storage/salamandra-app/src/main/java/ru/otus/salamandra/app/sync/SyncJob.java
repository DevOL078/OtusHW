package ru.otus.salamandra.app.sync;

import com.google.gson.Gson;
import okhttp3.*;
import ru.otus.salamandra.dto.SyncRequestDto;
import ru.otus.salamandra.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SyncJob {

    private final int SYNC_PERIOD = 5000;
    private final String BASE_DIR_PATH = "C:\\Users\\Dmitry\\Desktop\\Otus\\OtusHW\\salamandra-storage\\salamandra-app\\files";
    private AtomicBoolean isActive = new AtomicBoolean();

    public void startJob() {
        isActive.set(true);
        new Thread(() -> {
            while (isActive.get()) {
                doPostRequest(getSyncRequestDto());
                try {
                    Thread.sleep(SYNC_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopJob() {
        isActive.set(false);
    }

    private SyncRequestDto getSyncRequestDto() {
        SyncRequestDto syncRequestDto = new SyncRequestDto();
        try {
            List<File> filesInBaseDir = Files.walk(Paths.get(BASE_DIR_PATH))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            filesInBaseDir.forEach(f -> {
                List<String> relativePath = FileUtil.getRelativePath(BASE_DIR_PATH, f.getAbsolutePath());
                LocalDateTime lastModifiedTime = FileUtil.getLastModifiedTime(f.getAbsolutePath());
                syncRequestDto.addFileProperties(
                        new SyncRequestDto.FileProperties(relativePath, lastModifiedTime));
            });
            return syncRequestDto;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return syncRequestDto;
    }

    private void doPostRequest(SyncRequestDto syncRequestDto) {
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();

            String json = new Gson().toJson(syncRequestDto);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url("http://localhost:8080/sync/test")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println("SYNC RESPONSE: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
