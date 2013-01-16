package org.jenkinsci.plugins.uptime;

import hudson.model.Run;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Interface for service that provides "uptime" information for a Job. 
 * 
 * @author Chris Mair
 */
public interface UptimeService {

	BigDecimal getUptimePercentage(Iterator<Run<?,?>> iterator);
}
