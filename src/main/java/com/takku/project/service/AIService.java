package com.takku.project.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.takku.project.domain.AIResponse;
import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.FundingPromotionRequestDto;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.stats.SummaryResponse;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AIService {

	// ======= [Config Values] =======
	@Value("${groq.api.url}")
	private String apiUrl;

	@Value("${groq.api.key}")
	private String apiKey;

	@Value("${groq.api.model}")
	private String model;

	@Value("${external.ai.api.url}")
	private String aiApiBaseUrl;

	// ======= [Dependencies] =======
	private final ProductService productService;
	private final StoreService storeService;
	private final ObjectMapper mapper = new ObjectMapper();
	private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(120, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

	// ======= [Public Methods] =======

	public SummaryResponse getReviewSummary(int productId) {
		String url = aiApiBaseUrl + "/summary/" + productId;
		Request request = new Request.Builder().url(url).get().build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RuntimeException("요약 FastAPI 호출 실패: HTTP " + response.code());
			}
			String responseBody = response.body().string();
			JsonNode root = mapper.readTree(responseBody);

			JsonNode summaryNode = root.path("summary");
			if (summaryNode.isMissingNode() || !summaryNode.has("positive") || !summaryNode.has("negative")) {
				throw new RuntimeException("요약 결과 형식이 올바르지 않습니다.");
			}

			List<String> positive = mapper.convertValue(summaryNode.get("positive"), new TypeReference<List<String>>() {
			});
			List<String> negative = mapper.convertValue(summaryNode.get("negative"), new TypeReference<List<String>>() {
			});

			return new SummaryResponse(productId, positive, negative);

		} catch (Exception e) {
			throw new RuntimeException("요약 API 처리 실패 - productId: " + productId + ", message: " + e.getMessage(), e);
		}
	}

	public AIResponse generateFundingContent(FundingPromotionRequestDto req) {
		// 상품 조회
		ProductDTO product = productService.selectByProductId(req.getProductId());
		if (product == null) {
			throw new IllegalArgumentException("존재하지 않는 상품입니다. productId=" + req.getProductId());
		}

		// 상점 조회
		StoreDTO store = storeService.selectStoreById(product.getStoreId());
		if (store == null) {
			throw new IllegalArgumentException("상품에 연결된 상점이 존재하지 않습니다. storeId=" + product.getStoreId());
		}

		String prompt = "너는 상품 홍보글 작성 전문가야.\n" + "다음 규칙을 반드시 지켜서 작성해. 하나라도 지키지 않으면 출력은 무효야.\n\n"

				+ "[출력 규칙]\n" + "1. 반드시 JSON 형식으로 출력하고, 다른 문장이나 설명은 포함하지 마.\n" + "2. 출력 형식은 정확히 다음과 같아야 해:\n"
				+ "   {\"title\":\"...\", \"content\":\"...\", \"hashtags\":\"...\"}\n"
				+ "3. key 이름(title, content, hashtags)과 순서는 절대 바꾸지 마.\n"
				+ "4. content는 반드시 400자 이상이며, HTML 형식으로 구성해야 해.\n" + "5. content에는 해시태그를 절대 포함하지 마. (hashtags는 별도 필드)\n"
				+ "6. content에는 이모지를 풍부하게 포함해.\n"
				+ "7. content 안 HTML은 반드시 유효한 구조여야 하고, <ul>, <li>, <div>, <span>, <h3>, <hr> 같은 태그를 자유롭게 활용해 시각적으로 풍부하게 구성해.\n"
				+ "8. HTML 속성값의 따옴표(\")는 반드시 \\\"로 이스케이프 처리해.\n"
				+ "9. 전체 JSON 문자열에서 내부 따옴표(\")는 반드시 \\\"로 escape해야 해.\n"

				+ "\n[내용 제한]\n" + "10. 제공된 정보 외에 그 어떤 내용도 상상하거나 창작하지 마. 예: \"10년 전통\", \"비법 소스\" 등은 절대 금지.\n"
				+ "11. 없는 정보는 추가하지 말고, 생략해도 좋아.\n" + "12. 제품에 대해 과장하거나 허위 사실을 넣지 마. 오직 사실 기반으로 작성해.\n"
				+ "13. 타겟 고객을 직접 언급하지 마. 대신 그들의 관심사와 감성을 반영해서 자연스럽게 설득력 있는 표현을 써.\n"
				+ "14. 모든 출력은 반드시 한국어로만 작성하고, 한자나 중국어는 절대 사용하지 마.\n"

				+ "\n[제공 정보]\n" + "상품명: " + product.getProductName() + "\n" + "상품설명: " + product.getDescription() + "\n"
				+ "원가: " + product.getPrice() + "원\n" + "판매가: " + req.getSalePrice() + "원\n" + "상점명: "
				+ store.getStoreName() + "\n" + "상점 설명: " + store.getDescription() + "\n" + "카테고리: "
				+ store.getCategoryName() + "\n" + "지역: " + store.getSido() + " " + store.getSigungu() + "\n" + "키워드: "
				+ req.getKeyword() + "\n" + "타겟층: " + req.getTarget();

		try {
			String requestBody = buildGroqRequestBody(prompt);
			Request request = new Request.Builder().url(apiUrl).addHeader("Authorization", "Bearer " + apiKey)
					.post(RequestBody.create(requestBody, MediaType.parse("application/json"))).build();

			System.out.println("🔍 요청 프롬프트:\n" + prompt);
			System.out.println("🧾 Groq 응답:\n" + requestBody);

			try (Response response = client.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new RuntimeException("Groq API 호출 실패: HTTP " + response.code());
				}
				return parseGroqResponse(response.body().string());
			}
		} catch (Exception e) {
			throw new RuntimeException("AI 홍보글 생성 실패 - " + e.getMessage(), e);
		}
	}

	public List<FundingDTO> getRecommendations(int userId) {
		String json = getRecommendationsAsString(userId);
		return parseRecommendationList(json);
	}

	public String getRecommendationsAsString(int userId) {
		String fullUrl = aiApiBaseUrl + "/recommend/" + userId;

		Request request = new Request.Builder().url(fullUrl).get().build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RuntimeException("추천 FastAPI API 호출 실패: HTTP " + response.code() + " - userId: " + userId);
			}
			return response.body().string();
		} catch (Exception e) {
			throw new RuntimeException("추천 FastAPI API 호출 실패 - userId: " + userId + ", message: " + e.getMessage(), e);
		}
	}

	// ======= [Private Helpers] =======

	private String buildGroqRequestBody(String prompt) throws Exception {
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

		return mapper.writeValueAsString(jsonNode);
	}

	private AIResponse parseGroqResponse(String responseBody) throws Exception {
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
				.replaceAll("[\\n\\r]", "").trim();

		JsonNode jsonResult = mapper.readTree(cleanedJson);

		AIResponse aiResponse = new AIResponse();
		aiResponse.setTitle(jsonResult.path("title").asText(""));
		aiResponse.setContent(jsonResult.path("content").asText(""));
		aiResponse.setHashtags(jsonResult.path("hashtags").asText(""));

		return aiResponse;
	}

	private List<FundingDTO> parseRecommendationList(String json) {
		try {
			return mapper.readValue(json, new TypeReference<List<FundingDTO>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException("추천 FastAPI API 결과 파싱 실패 - message: " + e.getMessage(), e);
		}
	}

}
