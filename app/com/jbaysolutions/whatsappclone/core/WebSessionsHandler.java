package com.jbaysolutions.whatsappclone.core;

import play.mvc.WebSocket;

import java.util.Collection;
import java.util.Hashtable;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/8/16
 * Time: 12:32 AM
 */
public class WebSessionsHandler {
    private static WebSessionsHandler ourInstance = new WebSessionsHandler();

    public static WebSessionsHandler getInstance() {
        return ourInstance;
    }

    private WebSessionsHandler() {
    }

    private Hashtable<String, WebSession> qrSessionsTable = new Hashtable<>();
    private Hashtable<String, WebSession> authSessionsTable = new Hashtable<>();

    public void createNewQRSession(String qrCode, WebSocket.In<String> in, WebSocket.Out<String> out) {
        clearQRSession(qrCode);

        qrSessionsTable.put(qrCode,
                new WebSession(qrCode, in,out)
        );
    }

    public WebSession assignQRtoUser(String qrCode, String user) throws NoValidQrCodeException {
        if (qrSessionsTable.containsKey(qrCode)) {

            WebSession session = qrSessionsTable.get(qrCode);
            qrSessionsTable.remove(qrCode);

            session.setUser(user);
            authSessionsTable.put(user, session);

            return session;
        } else {
            throw new NoValidQrCodeException();
        }
    }

    public void clearQRSession(String qrCode) {
        if (qrSessionsTable.containsKey(qrCode)) {

            WebSession session = qrSessionsTable.get(qrCode);
            session.setIn(null);
            session.setOut(null);
            qrSessionsTable.remove(qrCode);

        }
    }

    public void clearAuthSession(String user) {
        if (authSessionsTable.containsKey(user)) {

            WebSession session = authSessionsTable.get(user);
            session.setIn(null);
            session.setOut(null);
            authSessionsTable.remove(user);

        }
    }


    public Collection<WebSession> getAllAuthSessions() {
        return authSessionsTable.values();
    }
}
