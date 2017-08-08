package org.util.webhook.WebhookTrig;

import org.jenkinsci.plugins.workflow.steps.AbstractStepExecutionImpl;
import org.jenkinsci.plugins.workflow.steps.StepContext;

public class WaitForCustomWebhookExecution
        extends AbstractStepExecutionImpl
{
    private static final long serialVersionUID = -148119134567863021L;
    WaitForCustomWebhookStep step;

    public WaitForCustomWebhookExecution(StepContext context, WaitForCustomWebhookStep step)
    {
        super(context);
        this.step = step;
    }

    public String getToken()
    {
        return this.step.getToken();
    }

    public boolean start()
    {
        String content = WebhookRootAction.registerWebhook(this);
        if (content != null)
        {
            getContext().onSuccess(content);
            return true;
        }
        return false;
    }

    public void stop(Throwable cause)
            throws Exception
    {
        WebhookRootAction.deregisterWebhook(this);
        getContext().onFailure(cause);
    }

    public void onResume()
    {
        super.onResume();
        start();
    }

    public void onTriggered(String content)
    {
        getContext().onSuccess(content);
    }
}
