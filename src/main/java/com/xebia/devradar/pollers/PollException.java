/**
 * 
 */
package com.xebia.devradar.pollers;


/**
 * @author Alexandre Dutra
 *
 */
public class PollException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -190023799835496167L;

    public PollException() {
        super();
    }

    public PollException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PollException(final String message) {
        super(message);
    }

    public PollException(final Throwable cause) {
        super(cause);
    }

}
