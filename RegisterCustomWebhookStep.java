package org.util.webhook.WebhookTrig;

import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

public class RegisterCustomWebhookStep
        extends Step
{

    private final static Class<? extends StepExecution> executionType = null;

    @DataBoundConstructor
    public RegisterCustomWebhookStep(){}

    public StepExecution start(StepContext context)
            throws Exception
    {
        return new RegisterCustomWebhookExecution(context);
    }

    public DescriptorImpl getDescriptor()
    {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl
            extends AbstractStepDescriptorImpl
    {
        public DescriptorImpl()
        {
            super(executionType);
        }

        public String getFunctionName()
        {
            return "registerCustomWebhook";
        }

        public String getDisplayName()
        {
            return "Creates and returns a Custom webhook that can be used by an external system to notify a pipeline";
        }
    }
}
