/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorioopencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Rutina4Transformaciones {

    static String ruta = "C:/Users/P1 3-3/Documents/Estudio/Carrera Ingenieria Informatica/"
            + "Septimo Semestre/Sistemas Multiagente y Percepcion Computacional/"
            + "Laboratorio No. 2/Imagenes/";

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img = Imgcodecs.imread(ruta + "foto1.png");

        if (img.empty()) {
            System.out.println("Error: no se pudo cargar la imagen.");
            return;
        }

        // 1. Redimensionado al 50%
        Mat imgRedim = new Mat();
        Imgproc.resize(img, imgRedim,
                new Size(img.cols() * 0.5, img.rows() * 0.5));

        // 2. Rotacion 45 grados
        Point centro = new Point(img.cols() / 2.0, img.rows() / 2.0);
        Mat matRot = Imgproc.getRotationMatrix2D(centro, 45, 1.0);
        Mat imgRotada = new Mat();
        Imgproc.warpAffine(img, imgRotada, matRot,
                new Size(img.cols(), img.rows()));

        // 3. Volteo horizontal
        Mat imgEspejoH = new Mat();
        Core.flip(img, imgEspejoH, 1);

        // 4. Volteo vertical (modificacion adicional)
        Mat imgEspejoV = new Mat();
        Core.flip(img, imgEspejoV, 0);

        Imgcodecs.imwrite(ruta + "foto1_redim.png", imgRedim);
        Imgcodecs.imwrite(ruta + "foto1_rotada.png", imgRotada);
        Imgcodecs.imwrite(ruta + "foto1_espejo_h.png", imgEspejoH);
        Imgcodecs.imwrite(ruta + "foto1_espejo_v.png", imgEspejoV);

        System.out.println("Rutina 4 completada.");
        System.out.println("Tam. original:    " + img.cols() + " x " + img.rows());
        System.out.println("Tam. redimensionado: " + imgRedim.cols() + " x " + imgRedim.rows());
    }
}
