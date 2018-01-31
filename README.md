# Autók detektálása mély neurális hálózat segítségével Android készüléken vizuális médiumon

## SelfDrivingPhone
Android alkalmazás amely a kamera képén autót detektál mély neurális hálózat segítségével.

Az alkalmazást letöltése után az Android Studio-ban meglévő projektként kell importálni és utána futtatható.

Az alkalmazás legfontosabb osztályai/csomagjai:

- assets: Ebben a mappában található meg a mély neurális hálózat modell.
- CameraActivity: A kamera modul életciklusának kezelése (indítása, leállítása).
- ConvNet: A mély neurális hálózat adatait tároló osztály, a modell betöltése itt történik és a predikció számolását is végzi.
- ImageProcessing: A kamera képét dolgozza fel a mély neurális hálózatnak megfelelő formátumra konvertálja át.
- Manifest: A szükséges engedélyek deklarálása.

## Mély neurális hálózat
A mappa tartalmazza a modell építéséhez és tanításához szükséges kódot.
A tanító adatbázist nem tartalmazza, az innen letölthető: https://www.gti.ssr.upm.es/data/Vehicle_database.html

## Futtatáshoz szükséges szoftverek/könyvtárak:
- [TensorFlow](https://www.tensorflow.org/install/) 
- [Keras](https://keras.io/)
- [Android Studio](https://developer.android.com/studio/index.html)(SDK,NDK)
- [Anaconda](https://www.continuum.io/downloads)
- Jupyter notebook(Anaconda tartalmazza)
- [OpenCV](http://opencv-python-tutroals.readthedocs.io/en/latest/index.html)
- Numpy
- Glob


A témáról egy cikk is készült: https://medium.com/@SmartLabAI/285a38b78fd4
