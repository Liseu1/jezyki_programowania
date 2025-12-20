package pl.edu.pwr.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForeignerRecord(int year, int val) { }
