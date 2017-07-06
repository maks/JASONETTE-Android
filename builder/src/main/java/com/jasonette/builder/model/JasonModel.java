package com.jasonette.builder.model;

/**
 * Jasonette root level model
 */

public class JasonModel {

    private HeadModel head;
    private BodyModel body;

    public JasonModel(String appName) {
        head = new HeadModel();
        body = new BodyModel();

        head.setTitle(appName);
    }

    public BodyModel getBody() {
        return body;
    }

    public HeadModel getHead() {
        return head;
    }
}
