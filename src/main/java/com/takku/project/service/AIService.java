package com.takku.project.service;

import com.takku.project.domain.AIResponse;

public interface AIService {
    AIResponse generateText(String keyword, String target);
}
