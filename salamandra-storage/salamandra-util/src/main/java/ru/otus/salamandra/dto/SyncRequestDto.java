package ru.otus.salamandra.dto;

import java.util.ArrayList;
import java.util.List;

public class SyncRequestDto {

    private List<FileProperties> filePropertiesList = new ArrayList<>();
    private String appId;

    public SyncRequestDto() {
    }

    public SyncRequestDto(String appId) {
        this.appId = appId;
    }

    public void addFileProperties(FileProperties fileProperties) {
        filePropertiesList.add(fileProperties);
    }

    public List<FileProperties> getFilePropertiesList() {
        return new ArrayList<>(filePropertiesList);
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "SyncRequestDto " + filePropertiesList.toString();
    }

}
