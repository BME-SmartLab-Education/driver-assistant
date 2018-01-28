package tmit.bme.selfdrivingphone.CameraUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import tmit.bme.selfdrivingphone.DataProcess.ConvNet;

/**
 * Created by atig on 12/3/17.
 */

public class CameraImageListener implements ImageReader.OnImageAvailableListener {

    private static boolean isConvNetNetworkReady = false;
    private final String TAG = getClass().toString();
    int[] pixels = new int[ConvNet.INPUT_HEIGHT * ConvNet.INPUT_WIDTH * ConvNet.INPUT_DEEP];
    float[] inputPicture = new float[ConvNet.INPUT_HEIGHT * ConvNet.INPUT_WIDTH * ConvNet.INPUT_DEEP];
    private Context context;
    private ConvNet convNet;
    private boolean isProcessing;
    private Thread loadYoloNetworkThread = new Thread(new Runnable() {
        public void run() {
            loadYoloNetwork();
            isConvNetNetworkReady = true;
        }
    });
    private ImageProcessing imageProcessing;

    private Handler detectionHandler;
    private HandlerThread detectionThread;


    public CameraImageListener(Context context) {
        this.context = context;
        loadYoloNetworkThread.start();
        startUp();
    }

    private void startUp() {
        detectionThread = new HandlerThread("Classifier Background Thread");
        detectionThread.start();
        detectionHandler = new Handler(detectionThread.getLooper());
    }

    public void shutDown() {
        detectionThread.quitSafely();
        try {
            detectionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detectionThread = null;
        detectionHandler = null;
    }

    public void loadYoloNetwork() {
        convNet = new ConvNet(context.getApplicationContext());
        isConvNetNetworkReady = true;
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        //Log.d(TAG, "Image received - " + counter++);
        //Log.d(TAG, "isProcessing = " + isProcessing);

        Image image = imageReader.acquireNextImage();
        if (isProcessing || !isConvNetNetworkReady) {
            image.close();
            return;
        }

        if (image == null || isProcessing || !isConvNetNetworkReady)
            return;

        if (!isProcessing && isConvNetNetworkReady) {
            isProcessing = true;
            detectionHandler.post(new InferenceThread(image));

        }
    }

    class InferenceThread implements Runnable {

        Image image;

        InferenceThread(Image image) {
            this.image = image;
        }

        public void run() {
            long timeStart = System.currentTimeMillis();

            Bitmap bitMapImage = new ImageProcessing().YUV_420_888_toRGB(image, 640, 640, context);

            if (bitMapImage == null) {
                Log.e(TAG, "errrror");
                isProcessing = false;
                image.close();
                return;
            }

            Matrix matrix = new Matrix();

            matrix.postRotate(+90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitMapImage, ConvNet.INPUT_WIDTH, ConvNet.INPUT_HEIGHT, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);


            pixels = new int[ConvNet.INPUT_WIDTH * ConvNet.INPUT_WIDTH * ConvNet.INPUT_DEEP];
            inputPicture = new float[ConvNet.INPUT_WIDTH * ConvNet.INPUT_WIDTH * ConvNet.INPUT_DEEP];
            rotatedBitmap.getPixels(pixels, 0, ConvNet.INPUT_WIDTH, 0, 0, ConvNet.INPUT_WIDTH, ConvNet.INPUT_HEIGHT);

            int counter = 0;
            for (int i = 0; i < ConvNet.INPUT_WIDTH * ConvNet.INPUT_HEIGHT; i++) {
                int R = (pixels[i] & 0xff0000) >> 16;
                int G = (pixels[i] & 0x00ff00) >> 8;
                int B = (pixels[i] & 0x0000ff) >> 0;

                inputPicture[counter] = R;
                inputPicture[counter + 1] = G;
                inputPicture[counter + 2] = B;
                counter += 3;

            }

            isProcessing = false;
            if (image != null)
                image.close();

            float[] res = convNet.runInference(inputPicture);

            long timeEnd = System.currentTimeMillis();


            String carOrNotCar = "notcar";
            if (res[0] > res[1] && res[1] < 0.3)
                carOrNotCar = "car";

            Intent intent = new Intent();
            intent.setAction("isCar");
            intent.putExtra("isCar", carOrNotCar);
            intent.putExtra("res", res);

            context.sendBroadcast(intent);

            Log.i(TAG, "InferenceThread took: " + (timeEnd - timeStart) + " ms");
        }
    }


}
