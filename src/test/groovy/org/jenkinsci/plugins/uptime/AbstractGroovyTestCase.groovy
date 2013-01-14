package org.jenkinsci.plugins.uptime

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

class AbstractGroovyTestCase {

	protected static final String FORMAT = 'mm/dd/yy HH:mm'
	protected static final MS_PER_MINUTE = 1000 * 60
	
	@Rule public TestName testName = new TestName();

	private long startTime

	//------------------------------------------------------------------------------------
	// Setup and Tear-down
	//------------------------------------------------------------------------------------
	
	@Before
	public void beforeAbstractJUnitTestCase() {
		startTime = System.currentTimeMillis();
	}
 
	@After
	public void afterAbstractJUnitTestCase() {
		logEndOfTest();
	}

	//------------------------------------------------------------------------------------
	// Helper Methods
	//------------------------------------------------------------------------------------
	
	protected long minutes(int numMinutes) {
		return MS_PER_MINUTE * numMinutes
	}
	
	protected long hours(int numHours) {
		return MS_PER_MINUTE * 60 * numHours
	}
	

	protected void log(Object message) {
		println getName() + ": " + message
	}
 
	protected String getName() {
		return testName.getMethodName();
	}
 
	protected void logEndOfTest() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		String className = getClass().name - (getClass().getPackage().getName() + '.')
		System.out.println("---------- [ END: " + className + "." + getName() + " ]  Time=" + elapsedTime + "ms ----------");
	}

}