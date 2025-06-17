package com.takku.project.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.takku.project.domain.AIResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class AIServiceImpl implements AIService {

	@Value("${groq.api.url}")
	private String apiUrl;

	@Value("${groq.api.key}")
	private String apiKey;

	@Value("${groq.api.model}")
	private String model;

	private final OkHttpClient client;
	private final ObjectMapper mapper;

	public AIServiceImpl() {
		this.client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS).build();
		this.mapper = new ObjectMapper();
	}

	@Override
	public AIResponse generateText(String keyword, String target) {
		String prompt = makePrompt(keyword, target);

		try {
			ObjectNode jsonNode = mapper.createObjectNode();
			jsonNode.put("model", model);

			ArrayNode messages = mapper.createArrayNode();
			ObjectNode systemMsg = mapper.createObjectNode();
			systemMsg.put("role", "system");
			systemMsg.put("content", "�ݵ�� JSON �������θ� ������.");
			messages.add(systemMsg);

			ObjectNode userMsg = mapper.createObjectNode();
			userMsg.put("role", "user");
			userMsg.put("content", prompt);
			messages.add(userMsg);

			jsonNode.set("messages", messages);
			jsonNode.put("temperature", 0.7);
			jsonNode.put("max_tokens", 2048);
			jsonNode.put("stream", false);

			String jsonBody = mapper.writeValueAsString(jsonNode);

			Request request = new Request.Builder().url(apiUrl).addHeader("Authorization", "Bearer " + apiKey)
					.post(RequestBody.create(jsonBody, MediaType.parse("application/json"))).build();

			try (Response response = client.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new RuntimeException("Groq API ȣ�� ����: HTTP " + response.code());
				}

				String responseBody = response.body().string();

				JsonNode rootNode = mapper.readTree(responseBody);
				if (rootNode.has("error")) {
					throw new RuntimeException("Groq API ����: " + rootNode.get("error").get("message").asText());
				}

				JsonNode choicesNode = rootNode.path("choices");
				if (!choicesNode.isArray() || choicesNode.size() == 0) {
					throw new RuntimeException("Groq API ���信 choices�� ����");
				}

				String content = choicesNode.get(0).path("message").path("content").asText(null);
				if (content == null) {
					throw new RuntimeException("Groq API ���信�� content�� ����");
				}

				content = content.replaceAll("(?s)<think>.*?</think>", "").trim();
				String cleanedJson = content.replaceAll("(?i)```json\\n?", "").replaceAll("\\n?```", "")
						.replaceAll("\n", "").replaceAll("\r", "").trim();

				JsonNode jsonResult = mapper.readTree(cleanedJson);

				AIResponse aiResponse = new AIResponse();
				aiResponse.setTitle(jsonResult.path("title").asText(""));
				aiResponse.setContent(jsonResult.path("content").asText(""));
				aiResponse.setHashtags(jsonResult.path("hashtags").asText(""));

				return aiResponse;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Groq API ȣ�� ����", e);
		}
	}

	private String makePrompt(String keyword, String target) {
		return "�ʴ� ��ǰ ȫ���� �ۼ� ��������.\n" + "�Ʒ� ��Ģ�� �ݵ�� ���Ѽ� �ۼ���:\n"
				+ "1. ����� �ݵ�� JSON �������θ� �����. �ΰ� ����, �ȳ� ����, �ڵ� �� (��: ```json ��), ��Ÿ ��� �ؽ�Ʈ�� ���� �߰����� ��.\n"
				+ "2. ��� ����: {\"title\":\"...\", \"content\":\"...\", \"hashtags\":\"...\"}\n"
				+ "3. JSON�� key �̸��� ������ �״�� ������ ��.\n" + "4. content�� �� ���̴� �ּ� 200�� �̻� �ۼ��� ��.\n"
				+ "5. �� ��ü�� �̸�Ƽ���� �ڿ������� ������ ��.\n" + "6. Ÿ�� ���� ���� �ȿ��� ���� ������� ����, Ÿ�� ���� ���ɻ�� ��ȣ�� �ݿ��ؼ� �ۼ��� ��.\n"
				+ "7. ��� ����� �ѱ���� �ۼ��� ��.\n\n" + "��ǰ: " + keyword + "\n" + "Ÿ�� ��: " + target;
	}

}
