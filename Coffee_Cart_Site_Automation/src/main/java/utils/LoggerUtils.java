package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtils {
	
	private final Logger logger;
	
	public LoggerUtils(Class<?> loggingClass) {
		this.logger = LogManager.getLogger(loggingClass);
	}
	
	public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }
    
    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public void fatal(String message) {
        logger.fatal(message);
    }

    public void trace(String message) {
        logger.trace(message);
    }

}
