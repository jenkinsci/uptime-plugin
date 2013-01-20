package org.jenkinsci.plugins.uptime;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author Chris Mair
 */
class GroovyUptimeColumnTest extends AbstractGroovyTestCase {

	private uptimeColumn = new UptimeColumn()
	
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
