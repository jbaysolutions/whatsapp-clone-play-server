package com.jbaysolutions.whatsappclone.util;

import com.jbaysolutions.whatsappclone.core.MobileSession;
import com.pushraven.Pushraven;
import play.Play;

import java.util.Collection;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/14/16
 * Time: 1:44 PM
 */
public class FirebaseUtil {

    private static Pushraven raven = new Pushraven(
            Play.application().configuration().getString("your.key")
    );

    public synchronized static void sendPushMessage(String client_key, String message) {
        raven
                .text(message)
                .to(client_key);
        raven.push();

        raven.clear();
        raven.clearAttributes();
        raven.clearTargets();
    }

    public static void sendPushMessage(Collection<MobileSession> sessions, String message) {

        Pushraven raven = new Pushraven(
                Play.application().configuration().getString("your.key")
        );

        for ( MobileSession session : sessions) {
            raven
                    .text(message)
                    .to(session.getFirebaseToken());
            raven.push();

            raven.clear();
            raven.clearAttributes();
            raven.clearTargets();
        }
    }

}
