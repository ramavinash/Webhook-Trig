package org.util.webhook.WebhookTrig;

import hudson.EnvVars;
import java.net.URI;
import java.util.UUID;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;

public class RegisterCustomWebhookExecution
        extends AbstractSynchronousStepExecution<WebhookToken>
{
    private static final long serialVersionUID = -6718328636399912927L;

    public RegisterCustomWebhookExecution(StepContext context)
    {
        super(context);
    }

    public WebhookToken run()
            throws Exception
    {
        String token = "jenkins-rest-test";
        String jenkinsUrl = (String)((EnvVars)getContext().get(EnvVars.class)).get("JENKINS_URL");
        if ((jenkinsUrl == null) || (jenkinsUrl.isEmpty())) {
            throw new RuntimeException("JENKINS_URL must be set in the Manage Jenkins console");
        }
        URI baseUri = new URI(jenkinsUrl);
        URI relative = new URI("webhook-step/" + token);
        URI path = baseUri.resolve(relative);

        return new WebhookToken(token, path.toString());
    }
}
