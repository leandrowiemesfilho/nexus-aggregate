package com.aggregate.nexus.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA Attribute Converter to serialize and deserialize a Map<String, BigDecimal> to JSONB.
 * This allows storing the map of source prices in a single JSONB column in PostgreSQL.
 */
@Converter(autoApply = true)
public class HashMapConverter implements AttributeConverter<Map<String, BigDecimal>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts the Map attribute to a JSON string for database storage.
     *
     * @param attribute the entity attribute value to be converted
     * @return a JSON representation of the map, or null if the input is null
     */
    @Override
    public String convertToDatabaseColumn(final Map<String, BigDecimal> attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return this.objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    /**
     * Converts the JSON string from the database to a Map attribute.
     *
     * @param dbData the data from the database column, should be a valid JSON string
     * @return a Map representation of the JSON data, or null if the input is null
     */
    @Override
    public Map<String, BigDecimal> convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return this.objectMapper.readValue(dbData, new TypeReference<HashMap<String, BigDecimal>>() {
            });
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
