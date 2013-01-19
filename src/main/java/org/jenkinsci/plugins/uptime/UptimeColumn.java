/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Alan Harder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.uptime;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import org.jenkinsci.plugins.uptime.Messages;

import hudson.util.RunList;
import hudson.views.ListViewColumn;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Iterator;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

/**
 * View column that shows 
 * 
 * @author Chris Mair
 */
public class UptimeColumn extends ListViewColumn {

	private UptimeService uptimeService = new DefaultUptimeService();
	
    public String getShortName(Job job) {
        Run lastFailedBuild = job.getLastFailedBuild();
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("lastFailedBuild=" + lastFailedBuild);
        
        RunList builds = job.getBuilds();
        System.out.println("builds=" + builds);
        
        Iterator<Run<?,?>> iterator = builds.iterator();
        
        BigDecimal uptimePercentage = uptimeService.getUptimePercentage(iterator); 
        
//        while (iterator.hasNext()) {
//			Run run = (Run) iterator.next();
//			System.out.println("Run: " + run + " time=" + run.getTime() + " result=" + run.getResult() + " duration=" + run.getDuration());
//		}
        
        System.out.println("Uptime=" + uptimePercentage);
        stringBuilder.append("Uptime: " + (uptimePercentage == null ? "-" : uptimePercentage));
        
        return stringBuilder.toString();
    }

    @Extension
    public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

    public Descriptor<ListViewColumn> getDescriptor() {
        return DESCRIPTOR;
    }

    private static class DescriptorImpl extends Descriptor<ListViewColumn> {
        @Override
        public ListViewColumn newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return new UptimeColumn();
        }

        @Override
        public String getDisplayName() {
            return Messages.UptimeColumn_DisplayName();
        }
    }
}
