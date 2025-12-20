package pl.edu.pwr.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public abstract class Fetcher<T> {
    protected final HttpClient httpClient;
    protected final ObjectMapper objectMapper;

    public Fetcher() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    public abstract T fetchData(int startYear, int endYear) throws Exception;

}
