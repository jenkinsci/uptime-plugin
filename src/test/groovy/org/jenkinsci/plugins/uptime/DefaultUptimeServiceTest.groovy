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

	private static final String FORMAT = 'mm/dd/yy HH:mm'
	private static final long T00_00 = Date.parse(FORMAT, '1/10/2013 00:00').time
	private static final long T01_00 = Date.parse(FORMAT, '1/10/2013 01:00').time
	private static final long DUR_1_MINUTE = 1000 * 60
	private static final long DUR_1_HOUR = DUR_1_MINUTE *60
	
	private DefaultUptimeService service = new DefaultUptimeService()
	
	@Test
	void getUptimePercentage_NoBuilds_100_Percent() {
		Iterator<Run> runs = [].iterator()
		assert service.getUptimePercentage(runs) == 1.0
	}

	@Test
	void getUptimePercentage_OneBuild_Success_100_Percent() {
		Run build = createBuild(Result.SUCCESS, T00_00)
		Iterator<Run> runs = [build].iterator()
		
		assert service.getUptimePercentage(runs) == 1.0
	}

	@Test
	void getUptimePercentage_OneBuild_Failure_0_Percent() {
		Run build = createBuild(Result.FAILURE, T00_00)
		Iterator<Run> runs = [build].iterator()
		
		assert service.getUptimePercentage(runs) == 0.0
	}
/*
	@Test
	void getUptimePercentage_Failure_Then_Success() {
		Run build = createBuild(Result.FAILURE, T00_00)
		Iterator<Run> runs = [build].iterator()
		
		assert service.getUptimePercentage(runs) == 0.0
	}
*/

	private Run createBuild(Result result, long startTime) {
		AbstractBuild build = Mockito.mock(AbstractBuild.class)
		Mockito.when(build.getResult()).thenReturn(result)
		Mockito.when(build.getTimestamp()).thenReturn(new Date(startTime).toCalendar())
		return build
	}
	
}