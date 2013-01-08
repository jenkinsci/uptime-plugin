package org.jenkinsci.plugins.uptime;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;

import org.apache.commons.io.FileUtils;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * 
 * @author Chris Mair
 */
public class UptimeColumn_IntegrationTest extends HudsonTestCase {

    public void test1() throws Exception {
        FreeStyleProject project = createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));

        FreeStyleBuild build = project.scheduleBuild2(0).get();
        System.out.println(build.getDisplayName()+" completed");

        // TODO Actual test of plugin
        
        // TODO: change this to use HtmlUnit
        String s = FileUtils.readFileToString(build.getLogFile());
        System.out.println(s);
        assertTrue(s.contains("+ echo hello"));
    }

}
