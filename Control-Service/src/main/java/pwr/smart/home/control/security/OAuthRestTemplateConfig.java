package pwr.smart.home.control.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class OAuthRestTemplateConfig {

    public static final String OAUTH_REST_TEMPLATE_WEBCLIENT = "OAUTH_REST_TEMPLATE";

    private final RestTemplateBuilder restTemplateBuilder;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean(OAUTH_REST_TEMPLATE_WEBCLIENT)
    RestTemplate oAuthRestTemplate() {
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");

        return restTemplateBuilder
                .additionalInterceptors(new OAuthClientCredentialsRestTemplateInterceptor(authorizedClientManager(), clientRegistration))
                .setReadTimeout(Duration.ofSeconds(5))
                .setConnectTimeout(Duration.ofSeconds(1))
                .build();
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager() {
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

}
