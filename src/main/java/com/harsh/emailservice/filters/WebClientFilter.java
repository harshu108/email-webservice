package com.harsh.emailservice.filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import reactor.core.publisher.Mono;

/**
 * This class implements the filters for WebClient to log headers and request detials
 */
public class WebClientFilter {

    private WebClientFilter(){}
    private static Logger logger = LoggerFactory.getLogger(WebClientFilter.class);


    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logMethodAndUrl(request);
            logHeaders(request);

            return Mono.just(request);
        });
    }


    private static void logHeaders(ClientRequest request) {
        request.headers().forEach((name, values) ->
            values.forEach(value ->
                logNameAndValuePair(name, value)
            )
        );
    }


    private static void logNameAndValuePair(String name, String value) {
        logger.debug("{}={}", name, value);
    }


    private static void logMethodAndUrl(ClientRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.method().name());
        sb.append(" to ");
        sb.append(request.url());

        logger.debug(sb.toString());
    }
}