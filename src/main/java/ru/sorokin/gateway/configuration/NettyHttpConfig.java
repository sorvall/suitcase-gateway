package ru.sorokin.gateway.configuration;

import io.netty.channel.ChannelOption;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;

@Configuration
public class NettyHttpConfig {

    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyCustomizer() {
        return factory -> factory.addServerCustomizers(httpServer -> {
            HttpServer http = HttpServer.create()
                    .port(80)  // Открыть HTTP порт
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            return httpServer
                    .route(routes -> routes.route(req -> true, (request, response) ->
                            response.status(301)
                                    .header("Location", "https://" + request.requestHeaders().get("Host"))
                                    .send()));
        });
    }
}
