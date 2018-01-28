package tmit.bme.selfdrivingphone.DataProcess;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by atig on 11/13/17.
 */

public class ImageProcessor {

    private final static int NEURAL_NETWORK_WIDTH = 64;
    private final static int NEURAL_NETWORK_HEIGHT = 64;
    private final static int NEURAL_NETWORK_DEPTH = 3;


    private Color getRGBFromBitmapPixel(int pixel) {

        Color pixelColor = new Color();

        pixelColor.setR((pixel & 0xff0000) >> 16);//R
        pixelColor.setG((pixel & 0x00ff00) >> 8);//G
        pixelColor.setB((pixel & 0x0000ff) >> 0);//B

        return pixelColor;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth, int rotate) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.setRotate(rotate);
        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }

    public void writeBitmapToFile(Bitmap b, String name) {

        String filename = name + ".png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
