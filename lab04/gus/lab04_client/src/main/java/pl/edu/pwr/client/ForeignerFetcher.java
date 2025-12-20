package pl.edu.pwr.client;

import pl.edu.pwr.client.model.ForeignerResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ForeignerFetcher extends Fetcher<ForeignerResponse> {

    private static final String BASE_URL = "https://bdl.stat.gov.pl/api/v1/data/by-variable/148074?unit-level=2&page-size=100&format=JSON";

    private final Map<String, ForeignerResponse> cache = new HashMap<>();

    public ForeignerResponse fetchData(int startYear, int endYear) throws IOException, InterruptedException {

        if(endYear < startYear) {
            throw new IOException("Rok końcowy musi być po roku początkowym");
        }

        String cacheKey = startYear + "-" + endYear;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        StringBuilder sb = new StringBuilder(BASE_URL);
        for(int i = startYear; i <= endYear; i++) {
            sb.append("&year=").append(i);
        }
        String url = sb.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Błąd pobierania danych. Kod HTTP: " + response.statusCode());
        }

        ForeignerResponse result = objectMapper.readValue(response.body(), ForeignerResponse.class);

        cache.put(cacheKey, result);

        return result;
    }
}