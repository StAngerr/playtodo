package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import controllers.MyHttpAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.play.store.PlayCacheSessionStore;
import org.pac4j.play.store.PlaySessionStore;

public class SecurityModule extends AbstractModule {
    public final static String JWT_SALT = "123456789012345678909876543212345";

    @Override
    protected void configure() {
        bind(PlaySessionStore.class).to(PlayCacheSessionStore.class);
    }

    @Provides
    protected HeaderClient provideHeaderClient() {
        return new HeaderClient();
    }

    @Provides
    protected ParameterClient provideParameterClient() {
        final ParameterClient parameterClient = new ParameterClient("token",
                new JwtAuthenticator(new SecretSignatureConfiguration(JWT_SALT)));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);
        return parameterClient;
    }

    @Provides
    protected Config provideConfig(HeaderClient headerClient, ParameterClient parameterClient) {
        final Config config = new Config(headerClient, parameterClient);
        config.setHttpActionAdapter(new MyHttpAdapter());
        return config;
    }
}
