package org.jenkinsci.plugins.uptime;

import hudson.model.Result;
import hudson.model.Run;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Default implementation of the UptimeService 
 * 
 * @author Chris Mair
 */
public class DefaultUptimeService implements UptimeService {

	@Override
	public BigDecimal getUptimePercentage(Iterator<Run> iterator) {

		boolean failed = false;
        while (iterator.hasNext()) {
			Run run = (Run) iterator.next();
			System.out.println("Run: " + run + " time=" + run.getTime() + " result=" + run.getResult() + " duration=" + run.getDuration());
			failed = run.getResult() == Result.FAILURE;
		}

		BigDecimal percentage = failed ? new BigDecimal("0.0") : new BigDecimal("1.0");
		return percentage;
	}

}
