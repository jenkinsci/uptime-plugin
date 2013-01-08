package org.jenkinsci.plugins.uptime

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

class AbstractGroovyTestCase {

	@Rule public TestName testName = new TestName();

	private long startTime
	
	/**
	 * Write the specified message out to the log. Application-specific subclasses
	 * can override with application-specific logging behavior, if desired.
	 * @param message the message to write
	 */
	protected void log(Object message) {
		println getName() + ": " + message
	}
 
	protected String getName() {
		return testName.getMethodName();
	}
 
	@Before
	public void beforeAbstractJUnitTestCase() {
		startTime = System.currentTimeMillis();
	}
 
	@After
	public void afterAbstractJUnitTestCase() {
		logEndOfTest();
	}
 
	protected void logEndOfTest() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		String className = getClass().name - (getClass().getPackage().getName() + '.')
		System.out.println("---------- [ END: " + className + "." + getName() + " ]  Time=" + elapsedTime + "ms ----------");
	}

}
