/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorioopencv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Rutina2Bordes {

    static String ruta = "C:/Users/P1 3-3/Documents/Estudio/Carrera Ingenieria Informatica/"
            + "Septimo Semestre/Sistemas Multiagente y Percepcion Computacional/"
            + "Laboratorio No. 2/Imagenes/";

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat imgOriginal = Imgcodecs.imread(ruta + "foto1.png");

        if (imgOriginal.empty()) {
            System.out.println("Error: no se pudo cargar la imagen.");
            return;
        }

        Mat imgGris = new Mat();
        Imgproc.cvtColor(imgOriginal, imgGris, Imgproc.COLOR_BGR2GRAY);

        // Detector Canny
        Mat imgCanny = new Mat();
        Imgproc.Canny(imgGris, imgCanny, 80, 200);

        // Detector Sobel X
        Mat imgSobelX = new Mat();
        Imgproc.Sobel(imgGris, imgSobelX, CvType.CV_16S, 1, 0);
        Core.convertScaleAbs(imgSobelX, imgSobelX);

        // Detector Sobel Y
        Mat imgSobelY = new Mat();
        Imgproc.Sobel(imgGris, imgSobelY, CvType.CV_16S, 0, 1);
        Core.convertScaleAbs(imgSobelY, imgSobelY);

        // Combinar Sobel X + Y
        Mat imgSobelTotal = new Mat();
        Core.addWeighted(imgSobelX, 0.5, imgSobelY, 0.5, 0, imgSobelTotal);

        Imgcodecs.imwrite(ruta + "foto1_canny.png", imgCanny);
        Imgcodecs.imwrite(ruta + "foto1_sobel.png", imgSobelTotal);

        System.out.println("Rutina 2 completada.");
        System.out.println("Bordes detectados con Canny y Sobel.");
    }
}