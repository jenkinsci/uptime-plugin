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
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractBuild.AbstractRunner;
import hudson.model.Run;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for DefaultUptimeServiceTest
 * 
 * @author Chris Mair
 */
class DefaultUptimeServiceTest extends AbstractGroovyTestCase {

	private static final long TIME_NOW = System.currentTimeMillis()
	
	private DefaultUptimeService service = new DefaultUptimeService() {
		@Override
		protected long getTimeNow() {
			return TIME_NOW
		}
	}
	
	@Test
	void getUptimePercentage_NoBuilds_Null() {
		Iterator<Run> runs = [].iterator()
		assert service.getUptimePercentage(runs) == null
	}

	@Test
	void getUptimePercentage_OneBuild_Success_100_Percent() {
		Run build = createBuild(Result.SUCCESS, hoursAgo(1))
		Iterator<Run> runs = [build].iterator()
		
		assert service.getUptimePercentage(runs) == 1.0
	}

	@Test
	void getUptimePercentage_OneBuild_Failure_0_Percent() {
		Run build = createBuild(Result.FAILURE, hoursAgo(1))
		Iterator<Run> runs = [build].iterator()
		
		assert service.getUptimePercentage(runs) == 0.0
	}

	@Test
	void getUptimePercentage_Failure_Then_Success() {
		Run build1 = createBuild(Result.FAILURE, minutesAgo(10))
		Run build2 = createBuild(Result.SUCCESS, minutesAgo(9))
		Iterator<Run> runs = [build2, build1].iterator()
		
		assert service.getUptimePercentage(runs) == 0.9
	}


	@Test
	void getUptimePercentage_Success_Then_Failure() {
		Run build1 = createBuild(Result.SUCCESS, minutesAgo(100))
		Run build2 = createBuild(Result.FAILURE, minutesAgo(37))
		Iterator<Run> runs = [build2, build1].iterator()
		
		assert service.getUptimePercentage(runs) == 0.63
	}

	@Test
	void getUptimePercentage_Mixed() {
		def builds =[
	        createBuild(Result.FAILURE, minutesAgo(1)),
	        createBuild(Result.SUCCESS, minutesAgo(8)),
	        createBuild(Result.FAILURE, minutesAgo(85)),
	        createBuild(Result.SUCCESS, minutesAgo(89)),
	        createBuild(Result.FAILURE, minutesAgo(91)),
	        createBuild(Result.FAILURE, minutesAgo(93)),
			createBuild(Result.FAILURE, minutesAgo(100)) ]
		Iterator<Run> runs = builds.iterator()
		
		assert service.getUptimePercentage(runs) == 0.11
	}

	@Test
	void getUptimePercentage_RoundsTo100Percent() {
		def builds =[
	        createBuild(Result.FAILURE, minutesAgo(158)),
			createBuild(Result.SUCCESS, hoursAgo(1000*24)) ]	// 1000 days ago
		Iterator<Run> runs = builds.iterator()
		
		assert service.getUptimePercentage(runs) == 1.00
	}

	@Test
	void getUptimePercentage_ScaleGreaterThanTwo() {
		def builds =[
			createBuild(Result.FAILURE, minutesAgo(1)),
			createBuild(Result.SUCCESS, minutesAgo(3)) ]
		Iterator<Run> runs = builds.iterator()
		
		assert service.getUptimePercentage(runs) == 0.667
	}


	@Test
	void getTimeNow() {
		assert System.currentTimeMillis() - new DefaultUptimeService().getTimeNow() <= 10L
	}
	
	//------------------------------------------------------------------------------------
	// Helper methods
	//------------------------------------------------------------------------------------

	private long minutesAgo(int numMinutes) {
		return TIME_NOW - minutes(numMinutes)
	}
	
	private long hoursAgo(int numHours) {
		return TIME_NOW - hours(numHours)
	}

	private Run createBuild(Result result, long startTime) {
		AbstractBuild build = Mockito.mock(AbstractBuild.class)
		Mockito.when(build.getResult()).thenReturn(result)
		Mockito.when(build.getTimestamp()).thenReturn(new Date(startTime).toCalendar())
		return build
	}
	
}