/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorioopencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Rutina3Rostros {

    static String ruta = "C:/Users/P1 3-3/Documents/Estudio/Carrera Ingenieria Informatica/"
            + "Septimo Semestre/Sistemas Multiagente y Percepcion Computacional/"
            + "Laboratorio No. 2/Imagenes/";

    static String rutaClasif = "C:/opencv/build/etc/haarcascades/"
            + "haarcascade_frontalface_default.xml";

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CascadeClassifier detector = new CascadeClassifier(rutaClasif);

        Mat img = Imgcodecs.imread(ruta + "foto2.jpg");

        if (img.empty()) {
            System.out.println("Error: no se pudo cargar la imagen.");
            return;
        }

        Mat imgGris = new Mat();
        Imgproc.cvtColor(img, imgGris, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(imgGris, imgGris);

        MatOfRect rostros = new MatOfRect();
        detector.detectMultiScale(imgGris, rostros, 1.05, 2,
            0, new Size(20, 20), new Size());

        System.out.println("Rostros detectados: " + rostros.toArray().length);

        for (Rect r : rostros.toArray()) {
            Imgproc.rectangle(img,
                    new Point(r.x, r.y),
                    new Point(r.x + r.width, r.y + r.height),
                    new Scalar(0, 255, 0), 3);
            Imgproc.putText(img, "Rostro",
                    new Point(r.x, r.y - 10),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    0.8, new Scalar(0, 255, 0), 2);
        }

        Imgcodecs.imwrite(ruta + "foto2_rostros.png", img);
        System.out.println("Rutina 3 completada.");
    }
}