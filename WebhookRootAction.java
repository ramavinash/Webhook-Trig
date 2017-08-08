package org.util.webhook.WebhookTrig;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.security.csrf.CrumbExclusion;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class WebhookRootAction
        extends CrumbExclusion
        implements UnprotectedRootAction
{
    private static final HashMap<String, WaitForCustomWebhookExecution> webhooks = new HashMap();
    private static final HashMap<String, String> alreadyPosted = new HashMap();

    public String getDisplayName()
    {
        return null;
    }

    public String getIconFileName()
    {
        return null;
    }

    public String getUrlName()
    {
        return "webhook-step";
    }

    public void doDynamic(StaplerRequest request, StaplerResponse response)
    {
        String token = request.getOriginalRestOfPath().substring(1);

        CharBuffer dest = CharBuffer.allocate(request.getContentLength());
        try
        {
            BufferedReader reader = request.getReader();
            while (reader.read(dest) > 0) {}
        }
        catch (IOException e)
        {
            response.setStatus(400);
            return;
        }
        dest.rewind();
        String content = dest.toString();

        Logger.getLogger(WebhookRootAction.class.getName()).info("Webhook called with " + token);
        WaitForCustomWebhookExecution exec;
        synchronized (webhooks)
        {
            exec = (WaitForCustomWebhookExecution)webhooks.remove(token);
            if (exec == null) {
                alreadyPosted.put(token, content);
            }
        }
        if (exec != null)
        {
            exec.onTriggered(content);
            response.setHeader("Result", "WebhookTriggered");
            response.setStatus(200);
        }
        else
        {
            response.setStatus(202);
        }
    }

    public static String registerWebhook(WaitForCustomWebhookExecution exec)
    {
        Logger.getLogger(WebhookRootAction.class.getName()).info("Registering webhook with token " + exec.getToken());
        synchronized (webhooks)
        {
            if (alreadyPosted.containsKey(exec.getToken())) {
                return (String)alreadyPosted.remove(exec.getToken());
            }
            webhooks.put(exec.getToken(), exec);
        }
        return null;
    }

    public static void deregisterWebhook(WaitForCustomWebhookExecution exec)
    {
        Logger.getLogger(WebhookRootAction.class.getName()).info("Deregistering webhook with token " + exec.getToken());
        synchronized (webhooks)
        {
            webhooks.remove(exec.getToken());
        }
    }

    public boolean process(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException
    {
        String pathInfo = req.getPathInfo();
        if ((pathInfo != null) && (pathInfo.startsWith("/webhook-step/")))
        {
            chain.doFilter(req, resp);
            return true;
        }
        return false;
    }
}
