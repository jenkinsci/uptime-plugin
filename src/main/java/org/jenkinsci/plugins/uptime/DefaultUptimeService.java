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

	/**
	 * Calculate the fraction of time that the job (represented by the set of Runs) has been successful
	 * over the total time since its first Run (build). The times are measured in a granularity of milliseconds.
	 * 
	 * @return a BigDecimal representing the percentage, with a scale of 3 (i.e., 0.000 to 1.000)
	 */
	@Override
	public BigDecimal getUptimePercentage(Iterator<Run<?,?>> iterator) {

		if (!iterator.hasNext()) {
			return null;
		}
		
		long timeNow = getTimeNow();
		long previousTime = timeNow;
		long startTime = 0L;
		long totalFailedMillis = 0L;
		
        while (iterator.hasNext()) {
			Run<?,?> run = (Run<?,?>) iterator.next();
			long runStartTime = run.getTimestamp().getTimeInMillis();
			System.out.println("Run: " + run + " runStartTime=" + new Date(runStartTime) + " result=" + run.getResult());

			// Always overwrite with oldest build so far
			startTime = runStartTime;
			
			if (isFailed(run)) {
				totalFailedMillis += previousTime - runStartTime;
			}
			previousTime = runStartTime;
		}

        long totalMinutes = timeNow - startTime;
        BigDecimal failedPercentage = calculatePercentage(totalFailedMillis, totalMinutes); 
		return new BigDecimal("1.000").subtract(failedPercentage);
	}

	private BigDecimal calculatePercentage(long value, long total) {
		return BigDecimal.valueOf(value).setScale(3).divide(BigDecimal.valueOf(total), BigDecimal.ROUND_HALF_DOWN);
	}
	
	private boolean isFailed(Run<?,?> run) {
		return run.getResult() == Result.FAILURE;
	}

	// Enable test to override and specify exact time
	protected long getTimeNow() {
		return System.currentTimeMillis();
	}
	
}