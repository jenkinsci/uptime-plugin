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
//	private static final long T00_00 = Date.parse(FORMAT, '1/10/2013 00:00').time
	
	private DefaultUptimeService service = new DefaultUptimeService() {
		@Override
		protected long getTimeNow() {
			return TIME_NOW
		}
	}
	
	@Test
	void getUptimePercentage_NoBuilds_100_Percent() {
		Iterator<Run> runs = [].iterator()
		assert service.getUptimePercentage(runs) == 1.0
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
		Iterator<Run> runs = [build1, build2].iterator()
		
		assert service.getUptimePercentage(runs) == 0.9
	}


	@Test
	void getUptimePercentage_Success_Then_Failure() {
		Run build1 = createBuild(Result.SUCCESS, minutesAgo(100))
		Run build2 = createBuild(Result.FAILURE, minutesAgo(37))
		Iterator<Run> runs = [build1, build2].iterator()
		
		assert service.getUptimePercentage(runs) == 0.63
	}

	@Test
	void getUptimePercentage_Mixed() {
		def builds =[
			createBuild(Result.FAILURE, minutesAgo(100)),
			createBuild(Result.FAILURE, minutesAgo(93)),
			createBuild(Result.FAILURE, minutesAgo(91)),
			createBuild(Result.SUCCESS, minutesAgo(89)),
			createBuild(Result.FAILURE, minutesAgo(85)),
			createBuild(Result.SUCCESS, minutesAgo(8)),
			createBuild(Result.FAILURE, minutesAgo(1)) ]
		Iterator<Run> runs = builds.iterator()
		
		assert service.getUptimePercentage(runs) == 0.11
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

	private static final MS_PER_MINUTE = 1000 * 60
	
	private long minutes(int numMinutes) {
		return MS_PER_MINUTE * numMinutes
	}
	
	private long hours(int numHours) {
		return MS_PER_MINUTE * 60 * numHours
	}
	
	private Run createBuild(Result result, long startTime) {
		AbstractBuild build = Mockito.mock(AbstractBuild.class)
		Mockito.when(build.getResult()).thenReturn(result)
		Mockito.when(build.getTimestamp()).thenReturn(new Date(startTime).toCalendar())
		return build
	}
	
}