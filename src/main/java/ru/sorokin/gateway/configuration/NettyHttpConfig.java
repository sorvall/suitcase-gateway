package ru.sorokin.gateway.configuration;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyHttpConfig {
    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyCustomizer() {
        return factory -> factory.addServerCustomizers(httpServer -> httpServer
                .port(80)
                .handle((request, response) -> {
                    String host = request.requestHeaders().get("Host");
                    return response.status(301)
                            .header("Location", "https://" + host + request.uri())
                            .send();
                }));
    }
}
