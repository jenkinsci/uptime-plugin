package org.jenkinsci.plugins.uptime

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

class AbstractGroovyTestCase {

	protected static final String FORMAT = 'mm/dd/yy HH:mm'
	
	@Rule public TestName testName = new TestName();

	private long startTime
	
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