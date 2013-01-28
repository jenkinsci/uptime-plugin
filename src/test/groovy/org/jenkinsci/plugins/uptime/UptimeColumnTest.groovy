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

import static org.junit.Assert.*;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * 
 * @author Chris Mair
 */
class UptimeColumnTest extends AbstractGroovyTestCase {

	private UptimeColumn uptimeColumn = new UptimeColumn()
	
	@Test
	void testGetShortName() {
		def build = Mockito.mock(AbstractBuild)
		def runList = RunList.fromRuns([build])
		def job = Mockito.mock(AbstractProject)
		Mockito.when(job.getBuilds()).thenReturn(runList)
		
		uptimeColumn.uptimeService = [
			getUptimePercentage:{ inRunList ->
				assert inRunList == runList
				return 0.234 
			}] as UptimeService
		
		def shortName = uptimeColumn.getShortName(job)
		log("shortName=$shortName")
		assert shortName == '23.4%'
	}

	@Test
	void testGetDescriptor() {
		def descriptor = uptimeColumn.getDescriptor()
		assert descriptor.newInstance(null, null) instanceof UptimeColumn
		assert descriptor.getDisplayName() == Messages.UptimeColumn_DisplayName()
	}

	
	@Test
	void testPercentageString() {
		assert uptimeColumn.percentageString(null) == '-'
		assert uptimeColumn.percentageString(0.00) == '0.0%'
		assert uptimeColumn.percentageString(0.12) == '12.0%'
		assert uptimeColumn.percentageString(0.99) == '99.0%'
		assert uptimeColumn.percentageString(0.994) == '99.4%'
		assert uptimeColumn.percentageString(0.9944) == '99.4%'
		assert uptimeColumn.percentageString(0.9949) == '99.4%'
		assert uptimeColumn.percentageString(0.999) == '99.9%'
		assert uptimeColumn.percentageString(1.00) == '100.0%'
	}

}