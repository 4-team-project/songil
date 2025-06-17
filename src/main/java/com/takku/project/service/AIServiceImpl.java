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
			systemMsg.put("content", "반드시 JSON 형식으로만 응답해.");
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
					throw new RuntimeException("Groq API 호출 실패: HTTP " + response.code());
				}

				String responseBody = response.body().string();

				JsonNode rootNode = mapper.readTree(responseBody);
				if (rootNode.has("error")) {
					throw new RuntimeException("Groq API 에러: " + rootNode.get("error").get("message").asText());
				}

				JsonNode choicesNode = rootNode.path("choices");
				if (!choicesNode.isArray() || choicesNode.size() == 0) {
					throw new RuntimeException("Groq API 응답에 choices가 없음");
				}

				String content = choicesNode.get(0).path("message").path("content").asText(null);
				if (content == null) {
					throw new RuntimeException("Groq API 응답에서 content가 없음");
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
			throw new RuntimeException("Groq API 호출 실패", e);
		}
	}

	private String makePrompt(String keyword, String target) {
		return "너는 상품 홍보글 작성 전문가야.\n" + "아래 규칙을 반드시 지켜서 작성해:\n"
				+ "1. 출력은 반드시 JSON 형식으로만 출력해. 부가 설명, 안내 문구, 코드 블럭 (예: ```json 등), 기타 모든 텍스트는 절대 추가하지 마.\n"
				+ "2. 출력 형식: {\"title\":\"...\", \"content\":\"...\", \"hashtags\":\"...\"}\n"
				+ "3. JSON의 key 이름과 순서는 그대로 유지할 것.\n" + "4. content의 글 길이는 최소 200자 이상 작성할 것.\n"
				+ "5. 글 전체에 이모티콘을 자연스럽게 포함할 것.\n" + "6. 타겟 고객을 문장 안에서 직접 언급하지 말고, 타겟 고객의 관심사와 선호를 반영해서 작성할 것.\n"
				+ "7. 모든 결과는 한국어로 작성할 것.\n\n" + "상품: " + keyword + "\n" + "타겟 고객: " + target;
	}

}
