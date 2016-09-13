package com.show.specialshow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.show.specialshow.R;

import java.util.Hashtable;


/**
 * 生成带logo的工具类
 */
public class CreateQRImage {
	private Context mContext;
	private int QR_WIDTH = 400, QR_HEIGHT = 400;

	public CreateQRImage(Context context) {
		mContext = context;
	}

	public Bitmap createQRImage(String url) {
		try {
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			Bitmap bitmap_icon = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.ic_launcher);
			Matrix mMatrix = new Matrix();
			float width = bitmap_icon.getWidth();
			float height = bitmap_icon.getHeight();
			mMatrix.setScale(40 / width, 40 / height);
			createQRCodeBitmapWithPortrait(bitmap,
					Bitmap.createBitmap(bitmap_icon, 0, 0, (int) width,
							(int) height, mMatrix, true));
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void createQRCodeBitmapWithPortrait(Bitmap qr, Bitmap portrait) {
		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();

		int left = (QR_WIDTH - portrait_W) / 2;
		int top = (QR_WIDTH - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;
		Rect rect1 = new Rect(left, top, right, bottom);

		Canvas canvas = new Canvas(qr);

		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
		canvas.drawBitmap(portrait, rect2, rect1, null);
	}
}
