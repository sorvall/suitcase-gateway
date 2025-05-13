package ru.sorokin.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.WebFilter;
import java.net.URI;

@Configuration
public class RedirectConfig {

    @Bean
    public WebFilter httpsRedirectFilter() {
        return (exchange, chain) -> {
            if ("http".equals(exchange.getRequest().getURI().getScheme())) {
                String httpsUrl = "https://" +
                        exchange.getRequest().getURI().getHost() +
                        exchange.getRequest().getURI().getPath();
                exchange.getResponse().setStatusCode(HttpStatus.PERMANENT_REDIRECT);
                exchange.getResponse().getHeaders().setLocation(URI.create(httpsUrl));
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }
}
