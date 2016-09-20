package com.jbaysolutions.whatsappclone.core;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/14/16
 * Time: 3:05 PM
 */
public class MobileSession {

    private String user= null;
    private String mobileid = null;
    private String firebaseToken = null;

    public MobileSession(String mobileid, String firebaseToken) {
        this.mobileid = mobileid;
        this.firebaseToken = firebaseToken;
    }

    public String getMobileid() {
        return mobileid;
    }

    public void setMobileid(String mobileid) {
        this.mobileid = mobileid;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
