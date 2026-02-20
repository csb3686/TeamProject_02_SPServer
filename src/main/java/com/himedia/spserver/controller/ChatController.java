package com.himedia.spserver.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ChatController {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public ChatController(VectorStore vectorStore, ChatClient.Builder chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient.build();
    }

    @PostMapping("/question")
    public HashMap<String , Object> qusetion(@RequestParam("question") String question ) {

        HashMap<String, Object> map = new HashMap<>();
        System.out.println("question: " + question);

        List<Document> list = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question) // 검색할 질의
                        .similarityThreshold(0.3) // 유사도 임계값
                        .topK(3) // 가져올 문서 개수
                        .build()
        );

        String template = """
          당신은 여행사의 관리직원입니다. 문맥에 따라 고객의 질문에 정중하게 답변해 주십시오. 컨텍스트가 질문에 대답할 수 없는 경우 '해당 질문에 대해서는 챗봇 응답이 준비되어 있지 않습니다. 사이트 하단의 전화번호 (010-1111-2222)로 직접 문의해 주시면 자세히 안내해 드리겠습니다.' 라고 대답하세요.
          
          컨텍스트:
          {context}
          질문:
          {question}
          
          답변:
          """;
        String answer = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("context", list)
                        .param("question", question))
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
        map.put("answer", answer);
        return map;
    }

}
