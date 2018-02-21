package io.jenkins.plugins.gcr.parsers;

import javax.swing.text.html.parser.Parser;

public class ParserException extends Exception {

    public ParserException(String message, Throwable ex) {
        super(message, ex);
    }

}
