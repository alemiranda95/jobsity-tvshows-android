package com.jobsity.tvshows.util.exception;

public class TvShowException extends Exception {

    public TvShowException(String message) {
        super(message);
    }

    public TvShowException(String message, Throwable cause) {
        super(message, cause);
    }
}
