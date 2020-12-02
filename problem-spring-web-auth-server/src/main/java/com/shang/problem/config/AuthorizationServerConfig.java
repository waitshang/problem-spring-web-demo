package com.shang.problem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import javax.annotation.Resource;

/**
 * Created by shangwei2009@hotmail.com on 2020/11/2 16:46
 */
@Configuration
// supply /oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * inject authenticationManager
     * to support password grant type
     */
    @Resource
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    /*
     * auth the client
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .authorizedGrantTypes("password")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // support password grant type
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // url:/oauth/check_token allow check token
                .checkTokenAccess("isAuthenticated()")
                // url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .tokenKeyAccess("permitAll()");
    }
}
