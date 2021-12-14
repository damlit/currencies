package com.pocket.currencies.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotesResponse {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("source")
    private String source;
    @JsonProperty("quotes")
    private Map<String, BigDecimal> quotes;
}
