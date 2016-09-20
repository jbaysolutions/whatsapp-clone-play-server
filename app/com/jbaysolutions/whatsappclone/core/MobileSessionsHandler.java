package com.jbaysolutions.whatsappclone.core;

import java.util.Collection;
import java.util.Hashtable;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/14/16
 * Time: 3:07 PM
 */
public class MobileSessionsHandler {
    private static MobileSessionsHandler ourInstance = new MobileSessionsHandler();

    public static MobileSessionsHandler getInstance() {
        return ourInstance;
    }

    private MobileSessionsHandler() {
    }

    private Hashtable<String, MobileSession> mobileIdSessionsTable = new Hashtable<>();
    private Hashtable<String, MobileSession> sessionsTable = new Hashtable<>();


    public MobileSession createNewMobileSession(String mobileid, String firebaseToken) {
        clearMobileSession(mobileid);

        for (MobileSession s : sessionsTable.values())  {
            if (s.getMobileid().equals(mobileid)) {
                System.out.println("Already found session, so, updating!");
                s.setFirebaseToken(firebaseToken);
                return s;
            }
        }

        MobileSession session  = new MobileSession(mobileid, firebaseToken);

        mobileIdSessionsTable.put(mobileid,
                session
        );

        return session ;
    }

    public MobileSession assignSessionToUser(String user, String mobileid) throws NoValidMobileIdException {
        if (mobileIdSessionsTable.containsKey(mobileid)) {
            MobileSession session = mobileIdSessionsTable.get(mobileid);
            session.setUser(user);

            sessionsTable.put(user, session);
            mobileIdSessionsTable.remove(mobileid);

            return session;
        } else {

            for ( MobileSession session : sessionsTable.values()) {

                if (session.getMobileid().equals(mobileid)) {

                    String user2 = session.getUser();
                    sessionsTable.remove(user2);

                    session.setUser(user);
                    session.setMobileid(mobileid);

                    sessionsTable.put(user, session);
                    return session;
                }

            }

            throw new NoValidMobileIdException();

        }

    }

    public void clearMobileSession(String user) {
        if (mobileIdSessionsTable.containsKey(user)) {

            mobileIdSessionsTable.remove(user);

        }
    }

    public Collection<MobileSession> getAllSessions() {
        return sessionsTable.values();
    }
}
