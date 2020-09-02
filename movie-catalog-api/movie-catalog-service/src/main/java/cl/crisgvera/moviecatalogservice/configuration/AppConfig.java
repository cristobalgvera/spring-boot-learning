package cl.crisgvera.moviecatalogservice.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        // Set timeout in RestTemplate object
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        // This object brings possibility to make a rest call to another service
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        // Set timeout in WebClient.Builder object
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(3000,
                            TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(3000,
                            TimeUnit.MILLISECONDS));
                });
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)));
    }
}
