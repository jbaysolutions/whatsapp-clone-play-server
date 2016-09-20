package controllers;


import com.jbaysolutions.whatsappclone.core.*;
import com.jbaysolutions.whatsappclone.util.FirebaseUtil;
import com.pushraven.Pushraven;
import play.Logger;
import play.mvc.*;

import com.jbaysolutions.whatsappclone.util.QRCodeUtil;
import views.html.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final play.Logger.ALogger logger = Logger.of("HomeController");

    public Result index() {
        return ok(index.render("WhatsApp Web App Clone Tutorial"));
    }

    /**
     * Returns an image of a QR Code for a specific given UUID
     *
     * @param inputUUID the UUID to generate the QR Code for
     * @return QR Code image
     * @throws IOException
     */
    public Result getQRCode(String inputUUID) throws IOException {

        UUID uuid = UUID.fromString(inputUUID);

        BufferedImage image = QRCodeUtil.generateQRCode(uuid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

        return ok(stream)
                .as("image/png");

    }


    /**
     * Making a WebSocket for chat application available.
     *
     * @return WebSocket
     */
    public LegacyWebSocket<String> socket() {

        return new LegacyWebSocket<String>() {
            @Override
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {

                in.onMessage(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        // Never Used
                    }
                });

                in.onClose(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("WebSocket Disconnected");
                    }
                });

                // Now we do some of or logic
                // 1- We generate a UUID that we'll associate with this WebSocket
                String uuid = UUID.randomUUID().toString();

                // 2- Now we store the UUID that we just created, in some fashion
                // to associate it with the IN and OUT streams of the Web Socket
                WebSessionsHandler.getInstance().createNewQRSession(
                        uuid,
                        in,out
                );

                // 3- We send the UUID to the browser, through the WebSocket
                // we just opened with the browser
                out.write("wsready###" + uuid);

            }
        };

    }

    /**
     * Obviously on a real system, checks would be make here to validade that a mobile user
     * is really authenticated, etc etc etc. For simplicity sake, we skip all that, and just assign
     * a user to the
     *
     * @param user the username of the User
     * @param qrCode the QR Code uuid scanned
     * @return
     */
    public Result authenticateUser(String user, String qrCode) {

        try {
            WebSession session = WebSessionsHandler.getInstance().assignQRtoUser(qrCode, user);
            session.getOut().write("authed###"+user);
            return ok();

        } catch (NoValidQrCodeException e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    /**
     * Method to associate an Android Id with a firebaseToken for cloud messaging
     *
     * @param androidid the android ID
     * @param firebaseToken the firebase Token
     * @return
     */
    public Result authenticateMobileDevice(String androidid, String firebaseToken) {
        logger.debug("authenticateMobileDevice :  androidid: " + androidid + "  - firebaseToken: "+firebaseToken);

        MobileSession session = MobileSessionsHandler.getInstance().createNewMobileSession(androidid, firebaseToken);
        return ok();
    }

    /**
     * Method for assigning a mobile User to a QR Code, and therefore a WebSocket.
     *
     * @param user the username
     * @param androidid the android id
     * @param qrCode the UUID of the qr code that was scanned
     * @return
     */
    public Result assignMobileDevice(String user, String androidid, String qrCode) {
        logger.debug("assignMobileDevice :  user: " + user + "  - androidid: "+androidid + "   - qrCode: " + qrCode);
        try {
            MobileSession session = MobileSessionsHandler.getInstance().assignSessionToUser(user, androidid);
            authenticateUser(user, qrCode);

        } catch (NoValidMobileIdException e) {
            e.printStackTrace();
        }
        return ok();

    }

    /**
     * Method used for Clients to send messages to the Chat.
     *
     * @param user the username that is sending the message
     * @param message the message itself
     * @return
     */
    public Result broadcastMessage(String user, String message) {

        // Send for all WebSessions
        for ( WebSession session : WebSessionsHandler.getInstance().getAllAuthSessions()) {
            try {
                session.getOut().write(
                        "msg###"+user+"###"+message
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Send for all Mobile Sessions
        for (MobileSession session : MobileSessionsHandler.getInstance().getAllSessions()) {
            FirebaseUtil.sendPushMessage(session.getFirebaseToken(), user + " : " +message);
        }

        return ok();

    }

}
