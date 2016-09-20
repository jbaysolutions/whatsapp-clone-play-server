package com.jbaysolutions.whatsappclone.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import play.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * (c) JBay Solutions 2010-2012 All rights reserved.
 * <p>
 * User: jbay
 * Date: 9/7/16
 * Time: 7:14 PM
 */
public class QRCodeUtil {

    private static final play.Logger.ALogger logger = Logger.of("QRCodeUtil");

    public static BufferedImage generateQRCode(UUID uuid) {

        int size = 250;
        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix byteMatrix = qrCodeWriter.encode(
                    uuid.toString(),
                    BarcodeFormat.QR_CODE,
                    size,
                    size,
                    hintMap
            );

            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            logger.debug("You have successfully created QR Code.");

            return image;

        } catch (WriterException e) {
            logger.error("Problem generating QR Code" , e);
            return null;
        }
    }

}
