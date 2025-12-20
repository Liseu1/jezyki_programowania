package pl.edu.pwr.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForeignerResponse(String unitName, int aggregateId, List<ForeignerSeries> results) { }
