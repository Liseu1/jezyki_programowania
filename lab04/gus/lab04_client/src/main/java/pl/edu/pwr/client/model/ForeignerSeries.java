package pl.edu.pwr.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record ForeignerSeries(String name, List<ForeignerRecord> values) { }
