package org.util.webhook.WebhookTrig;

import java.io.Serializable;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

public class WebhookToken
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String token;
    private final String url;

    public WebhookToken(String token, String url)
    {
        this.token = token;
        this.url = url;
    }

    @Whitelisted
    public String getToken()
    {
        return this.token;
    }

    @Whitelisted
    public String getURL()
    {
        return this.url;
    }
}
