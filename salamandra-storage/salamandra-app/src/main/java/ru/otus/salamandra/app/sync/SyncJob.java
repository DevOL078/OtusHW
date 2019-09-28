package ru.otus.salamandra.app.sync;

import com.google.gson.Gson;
import okhttp3.*;
import ru.otus.salamandra.app.config.AppConfigManager;
import ru.otus.salamandra.app.store.FileNameStorage;
import ru.otus.salamandra.app.store.SessionStore;
import ru.otus.salamandra.dto.SyncRequestDto;
import ru.otus.salamandra.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SyncJob {

    private final String baseDirPath = SessionStore.getInstance().getBaseDirPath();
    private final int delayMs = AppConfigManager.getInstance().getIntConf("sync.delayMs");
    private AtomicBoolean isActive = new AtomicBoolean();

    public void startJob() {
        isActive.set(true);
        new Thread(() -> {
            while (isActive.get()) {
                doPostRequest(getSyncRequestDto());
                try {
                    Thread.sleep(delayMs);
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
        String appId = SessionStore.getInstance().getAppId();
        SyncRequestDto syncRequestDto = new SyncRequestDto(appId);
        try {
            List<File> filesInBaseDir = Files.walk(Paths.get(baseDirPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            filesInBaseDir.forEach(f -> {
                List<String> relativePath = FileUtil.getRelativePath(baseDirPath, f.getAbsolutePath());
                Integer version = FileNameStorage.getInstance().getVersion(f.getAbsolutePath());
                if (version != null) {
                    //Если файл уже обработался UpdateProcessor
                    syncRequestDto.addFileProperties(
                            new SyncRequestDto.FileProperties(relativePath, version));
                }
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
            String login = SessionStore.getInstance().getUserLogin();
            Request request = new Request.Builder()
                    .url("http://localhost:8080/sync/" + login)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println("SYNC RESPONSE: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
