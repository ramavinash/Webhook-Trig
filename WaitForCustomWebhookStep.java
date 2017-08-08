package org.util.webhook.WebhookTrig;

import hudson.Extension;
import java.io.Serializable;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

public class WaitForCustomWebhookStep
        extends Step
        implements Serializable
{
    private static final long serialVersionUID = -667001655472658819L;
    private final String token;
    private final static Class<? extends StepExecution> executionType = null;

    @DataBoundConstructor
    public WaitForCustomWebhookStep(WebhookToken webhookToken)
    {
        this.token = webhookToken.getToken();
    }

    public String getToken()
    {
        return this.token;
    }

    public StepExecution start(StepContext context)
            throws Exception
    {
        return new WaitForCustomWebhookExecution(context, this);
    }

    @Extension
    public static class DescriptorImpl
            extends AbstractStepDescriptorImpl
    {
        public DescriptorImpl(){
            super(executionType);
        }

        public String getFunctionName()
        {
            return "waitForCustomWebhook";
        }

        public String getDisplayName()
        {
            return "Wait for Custom webhook to be posted to by external system";
        }
    }
}
