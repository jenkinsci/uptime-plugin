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
			getUptimePercentage:{ iterator ->
				def buildList = iterator.toList()
				assert buildList == [build]
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