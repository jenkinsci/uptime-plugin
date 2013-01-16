package org.jenkinsci.plugins.uptime;

import hudson.model.Result;
import hudson.model.Run;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

/**
 * Default implementation of the UptimeService 
 * 
 * @author Chris Mair
 */
public class DefaultUptimeService implements UptimeService {

	@Override
	public BigDecimal getUptimePercentage(Iterator<Run<?,?>> iterator) {

		if (!iterator.hasNext()) {
			return null;
		}
		
		long timeNow = getTimeNow();
		long startTime = 0L;
		long totalFailedMillis = 0L;
		long startFailedTime = 0L;
		
		boolean failing = false;
        while (iterator.hasNext()) {
			Run<?,?> run = (Run<?,?>) iterator.next();
			long runStartTime = run.getTimestamp().getTimeInMillis();
			System.out.println("Run: " + run + " runStartTime=" + new Date(runStartTime) + " result=" + run.getResult());
			if (startTime == 0L) {
				startTime = runStartTime;
			}
			
			if (isFailed(run)) {
				if (!failing) {
					startFailedTime = runStartTime;
				}
			}
			else {	// SUCCESS
				if (failing) {
					totalFailedMillis += runStartTime - startFailedTime;
				}
			}
			
			failing = isFailed(run);
		}

		if (failing) {
			totalFailedMillis += timeNow - startFailedTime;
		}

        long totalMinutes = timeNow - startTime;
        BigDecimal failedPercentage = calculatePercentage(totalFailedMillis, totalMinutes); 
		return new BigDecimal("1.00").subtract(failedPercentage);
	}

	private BigDecimal calculatePercentage(long value, long total) {
		return BigDecimal.valueOf(value).setScale(2).divide(BigDecimal.valueOf(total), BigDecimal.ROUND_HALF_DOWN);
	}
	
	private boolean isFailed(Run<?,?> run) {
		return run.getResult() == Result.FAILURE;
	}

	// Enable test to override and specify exact time
	protected long getTimeNow() {
		return System.currentTimeMillis();
	}
	
}