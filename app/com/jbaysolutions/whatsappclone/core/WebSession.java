package com.jbaysolutions.whatsappclone.core;

import play.mvc.WebSocket;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/8/16
 * Time: 12:31 AM
 */
public class WebSession {

    private String user = null;
    private String qrCode = null;
    private play.mvc.WebSocket.In<String> in;
    private WebSocket.Out<String> out;

    WebSession(String qrCode, WebSocket.In<String> in, WebSocket.Out<String> out) {
        this.qrCode = qrCode;
        this.in = in;
        this.out = out;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public WebSocket.In<String> getIn() {
        return in;
    }

    public void setIn(WebSocket.In<String> in) {
        this.in = in;
    }

    public WebSocket.Out<String> getOut() {
        return out;
    }

    public void setOut(WebSocket.Out<String> out) {
        this.out = out;
    }
}
