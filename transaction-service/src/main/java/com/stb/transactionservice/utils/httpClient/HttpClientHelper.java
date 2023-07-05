package com.stb.transactionservice.utils.httpClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HttpClientHelper {

    private WebClient webClient;

    public HttpClientHelper() {
        this.webClient = WebClient.builder().build();
    }

    public void setAuthorizationHeader(String token) {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

    public Mono<String> get(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> post(String url, String requestBody) {
        return webClient.post()
                .uri(url)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> put(String url, String requestBody) {
        return webClient.put()
                .uri(url)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> patch(String url, String requestBody) {
        return webClient.patch()
                .uri(url)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Void> delete(String url) {
        return webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
