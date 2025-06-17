package com.takku.project.domain;

import java.util.Arrays;
import java.util.List;

public class AIResponse {
    private String title;
    private String content;
    private List<String> hashtags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getHashtags() { return hashtags; }
    public void setHashtags(String hashtagsStr) {
        if (hashtagsStr != null && !hashtagsStr.isEmpty()) {
            this.hashtags = Arrays.asList(hashtagsStr.trim().split("\\s+"));
        }
    }
}
