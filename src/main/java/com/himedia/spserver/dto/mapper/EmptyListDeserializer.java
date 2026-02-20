package com.himedia.spserver.dto.mapper;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EmptyListDeserializer extends JsonDeserializer<List> {

    @Override
    public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        // 값 그대로 읽음
        String value = p.getValueAsString();

        // exlist: "" 같은 경우
        if (value != null && value.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 나머지 경우(정상 JSON array)는 Jackson에게 맡김
        return (List<?>) ctxt.readValue(p, List.class);
    }
}
