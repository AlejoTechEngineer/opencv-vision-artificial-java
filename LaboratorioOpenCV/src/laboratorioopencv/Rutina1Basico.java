/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorioopencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Rutina1Basico {

    static String ruta = "C:/Users/P1 3-3/Documents/Estudio/Carrera Ingenieria Informatica/"
        + "Septimo Semestre/Sistemas Multiagente y Percepcion Computacional/"
        + "Laboratorio No. 2/Imagenes/";

    public static void main(String[] args) {
        System.out.println("Ruta: " + ruta);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat imgOriginal = Imgcodecs.imread(ruta + "foto1.png");

        if (imgOriginal.empty()) {
            System.out.println("Error: no se pudo cargar la imagen.");
            return;
        }

        // Conversion a escala de grises
        Mat imgGris = new Mat();
        Imgproc.cvtColor(imgOriginal, imgGris, Imgproc.COLOR_BGR2GRAY);

        // Filtro gaussiano
        Mat imgSuavizada = new Mat();
        Imgproc.GaussianBlur(imgGris, imgSuavizada, new Size(15, 15), 0);

        // Umbralización binaria
        Mat imgBinaria = new Mat();
        Imgproc.threshold(imgGris, imgBinaria, 127, 255, Imgproc.THRESH_BINARY);

        Imgcodecs.imwrite(ruta + "foto1_gris.png", imgGris);
        Imgcodecs.imwrite(ruta + "foto1_suavizada.png", imgSuavizada);
        Imgcodecs.imwrite(ruta + "foto1_binaria.png", imgBinaria);

        System.out.println("Rutina 1 completada.");
        System.out.println("Dimensiones: " + imgOriginal.rows() + " x " + imgOriginal.cols());
    }
}