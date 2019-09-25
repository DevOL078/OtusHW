package ru.otus.salamandra.server.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private int fileSize;

    @Column(nullable = false)
    private LocalDateTime lastModifiedTime;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(){}

    public File(String fileName,
                int fileSize,
                LocalDateTime lastModifiedTime,
                User user) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModifiedTime = lastModifiedTime;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public String toString() {
        return String.format("File [id = %d, fileName = %s, fileSize = %d, lastModifiedTime = %s, createdAt = %s, updatedAt = %s]",
                id, fileName, fileSize, lastModifiedTime, createdAt, updatedAt);
    }
}
