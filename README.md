# Laboratorio No. 2 — Visión Artificial con OpenCV + Java

<div align="center">

![Java](https://img.shields.io/badge/Java-8%2F17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OpenCV](https://img.shields.io/badge/OpenCV-4.10.0-5C3EE8?style=for-the-badge&logo=opencv&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-IDE-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)
![Apache Ant](https://img.shields.io/badge/Apache%20Ant-Build-A81C7D?style=for-the-badge&logo=apache&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)

**Procesamiento y análisis de imágenes con OpenCV 4.10 en Java**  
*Laboratorio académico de Sistemas Multiagente y Percepción Computacional*

</div>

---

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Rutinas Implementadas](#rutinas-implementadas)
  - [Rutina 1 — Procesamiento Básico](#rutina-1--procesamiento-básico)
  - [Rutina 2 — Detección de Bordes](#rutina-2--detección-de-bordes)
  - [Rutina 3 — Detección de Rostros](#rutina-3--detección-de-rostros)
  - [Rutina 4 — Transformaciones Geométricas](#rutina-4--transformaciones-geométricas)
- [Resultados Visuales](#resultados-visuales)
- [Fundamentos Teóricos](#fundamentos-teóricos)
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Autor](#autor)

---

## Descripción del Proyecto

Este repositorio contiene la implementación del **Laboratorio No. 2** de la asignatura *Sistemas Multiagente y Percepción Computacional* del séptimo semestre de Ingeniería Informática. El proyecto explora el procesamiento digital de imágenes mediante la librería **OpenCV 4.10.0** integrada con **Java**, utilizando **NetBeans IDE** como entorno de desarrollo y **Apache Ant** como sistema de construcción.

El trabajo está dividido en **4 rutinas independientes** que cubren las operaciones más fundamentales en visión por computador: desde transformaciones básicas de espacio de color, pasando por algoritmos de detección de bordes basados en gradientes, hasta técnicas de detección de objetos en tiempo real mediante clasificadores en cascada Haar.

Cada rutina está diseñada como una clase ejecutable independiente (`main` propio), lo que permite su evaluación de forma modular y facilita la comprensión aislada de cada conjunto de técnicas.

---

## Arquitectura del Sistema

```
LaboratorioOpenCV/
│
├── src/laboratorioopencv/
│   ├── LaboratorioOpenCV.java      ← Entry point / verificación de instalación
│   ├── Rutina1Basico.java          ← Espacio de color, suavizado, umbralización
│   ├── Rutina2Bordes.java          ← Detección de bordes (Canny + Sobel)
│   ├── Rutina3Rostros.java         ← Detección de rostros (Haar Cascade)
│   └── Rutina4Transformaciones.java ← Redimensionado, rotación, volteo
│
├── Imagenes/
│   ├── foto1.png                   ← Imagen base (rutinas 1, 2, 4)
│   ├── foto2.jpg                   ← Imagen con personas (rutina 3)
│   └── [resultados generados]      ← Salidas PNG de cada rutina
│
├── nbproject/                      ← Configuración NetBeans / Ant
│   ├── project.xml
│   ├── project.properties          ← Classpath, JVM args, plataforma
│   └── build-impl.xml
│
└── build.xml                       ← Script Ant principal
```

**Decisiones de arquitectura clave:**

| Decisión | Justificación |
|---|---|
| Clase ejecutable independiente por rutina | Permite evaluación y depuración modular sin acoplamiento entre ejercicios |
| Carga nativa explícita con `System.loadLibrary` | OpenCV requiere cargar la DLL nativa (`opencv_java4100.dll`) antes de cualquier llamada a la API |
| Rutas de archivo hardcodeadas | Laboratorio académico; la portabilidad no es objetivo; simplifica el código de demostración |
| Java 1.8 como source/target con JDK 17 | Garantiza compatibilidad con versiones antiguas de la JVM manteniendo el toolchain moderno |
| Ant como sistema de build | NetBeans genera automáticamente `build.xml`; integración nativa sin configuración adicional |

---

## Rutinas Implementadas

### Rutina 1 — Procesamiento Básico

**Archivo:** `Rutina1Basico.java`

Implementa el pipeline de procesamiento de imagen más fundamental en visión computacional: conversión de espacio de color, suavizado y segmentación por umbral.

```
foto1.png ──┬──► cvtColor(BGR→GRAY) ──────────────────► foto1_gris.png
            │         │
            │         └──► GaussianBlur(15×15, σ=0) ──► foto1_suavizada.png
            │         │
            │         └──► threshold(127, BINARY) ────► foto1_binaria.png
            │
            └──► [dimensiones en consola]
```

**Operaciones implementadas:**

| Operación | API OpenCV | Parámetros | Efecto |
|---|---|---|---|
| Conversión a grises | `Imgproc.cvtColor()` | `COLOR_BGR2GRAY` | Reduce 3 canales a 1 canal de intensidad |
| Filtro Gaussiano | `Imgproc.GaussianBlur()` | Kernel 15×15, σ=0 | Eliminación de ruido de alta frecuencia |
| Umbralización binaria | `Imgproc.threshold()` | umbral=127, maxVal=255 | Segmentación binaria global |

**Por qué Gaussian Blur con kernel 15×15:** Un kernel grande garantiza un suavizado agresivo que elimina artefactos de compresión y ruido; σ=0 hace que OpenCV calcule automáticamente la desviación estándar óptima a partir del tamaño del kernel.

---

### Rutina 2 — Detección de Bordes

**Archivo:** `Rutina2Bordes.java`

Implementa dos de los algoritmos más importantes en detección de bordes: el operador de Canny (óptimo según criterios de Canny, 1986) y el operador de Sobel (basado en gradiente discreto de primer orden).

```
foto1.png ──► cvtColor(BGR→GRAY) ──┬──► Canny(80, 200) ─────────────────────────► foto1_canny.png
                                   │
                                   ├──► Sobel(CV_16S, dx=1, dy=0) ──► convertScaleAbs() ──┐
                                   │                                                        ├──► addWeighted(0.5, 0.5) ──► foto1_sobel.png
                                   └──► Sobel(CV_16S, dx=0, dy=1) ──► convertScaleAbs() ──┘
```

**Operaciones implementadas:**

| Detector | API OpenCV | Parámetros | Descripción |
|---|---|---|---|
| Canny | `Imgproc.Canny()` | low=80, high=200 | Detección óptima con supresión de no-máximos e histéresis |
| Sobel X | `Imgproc.Sobel()` | `CV_16S`, dx=1, dy=0 | Gradiente horizontal (detecta bordes verticales) |
| Sobel Y | `Imgproc.Sobel()` | `CV_16S`, dx=0, dy=1 | Gradiente vertical (detecta bordes horizontales) |
| Fusión Sobel | `Core.addWeighted()` | α=0.5, β=0.5 | Magnitud de gradiente total: G = 0.5·Gx + 0.5·Gy |

**Nota técnica:** El uso de `CvType.CV_16S` en Sobel es fundamental — el gradiente puede producir valores negativos que no caben en un `uint8`. `convertScaleAbs()` convierte a valor absoluto y normaliza a `uint8` para escritura en PNG.

**Relación umbral Canny (80:200 = 1:2.5):** La proporción recomendada entre umbral bajo y alto está entre 1:2 y 1:3 para equilibrar sensibilidad y precisión en la detección.

---

### Rutina 3 — Detección de Rostros

**Archivo:** `Rutina3Rostros.java`

Implementa detección de rostros humanos en tiempo real usando el clasificador en cascada **Viola-Jones (Haar Cascade)**, uno de los algoritmos más influyentes en visión computacional, propuesto por Viola y Jones en 2001.

```
foto2.jpg ──► cvtColor(BGR→GRAY) ──► equalizeHist() ──► detectMultiScale() ──► [bounding boxes] ──► rectangle() + putText() ──► foto2_rostros.png
                                          │
                                    haarcascade_frontalface_default.xml
                                    (C:/opencv/build/etc/haarcascades/)
```

**Pipeline de detección:**

| Paso | API OpenCV | Propósito |
|---|---|---|
| Conversión a grises | `Imgproc.cvtColor()` | Haar funciona sobre intensidad, no color |
| Ecualización de histograma | `Imgproc.equalizeHist()` | Normaliza el contraste; mejora detección en condiciones de iluminación variable |
| Detección multi-escala | `CascadeClassifier.detectMultiScale()` | Busca rostros en múltiples escalas de la imagen |
| Dibujado de rectángulos | `Imgproc.rectangle()` | Marca visualmente cada rostro detectado en verde (0,255,0) |
| Etiquetado | `Imgproc.putText()` | Añade la etiqueta "Rostro" sobre cada bounding box |

**Parámetros de `detectMultiScale`:**

| Parámetro | Valor | Significado |
|---|---|---|
| `scaleFactor` | 1.05 | Escala de reducción por nivel de la pirámide (1.05 = 5% más pequeño en cada nivel) |
| `minNeighbors` | 2 | Número mínimo de detecciones adyacentes para confirmar un rostro |
| `minSize` | 20×20 px | Tamaño mínimo de rostro a detectar |
| `maxSize` | sin límite | No se aplica límite superior de tamaño |

**El clasificador Haar:** Usa `haarcascade_frontalface_default.xml`, un modelo preentrenado incluido con la instalación de OpenCV que contiene miles de características de Haar organizadas en cascadas de clasificadores débiles (AdaBoost).

---

### Rutina 4 — Transformaciones Geométricas

**Archivo:** `Rutina4Transformaciones.java`

Implementa las cuatro transformaciones geométricas afines más comunes en el preprocesamiento de imágenes para visión artificial.

```
foto1.png ──┬──► resize(50%) ────────────────────────────────────────────► foto1_redim.png
            │
            ├──► getRotationMatrix2D(centro, 45°, escala=1.0) ──► warpAffine() ──► foto1_rotada.png
            │
            ├──► flip(flipCode=1) [volteo horizontal] ───────────────────► foto1_espejo_h.png
            │
            └──► flip(flipCode=0) [volteo vertical] ─────────────────────► foto1_espejo_v.png
```

**Transformaciones implementadas:**

| Transformación | API OpenCV | Parámetros | Resultado |
|---|---|---|---|
| Redimensionado | `Imgproc.resize()` | cols×0.5, rows×0.5 | Imagen al 50% del tamaño original |
| Rotación 45° | `Imgproc.getRotationMatrix2D()` + `warpAffine()` | centro, ángulo=45°, escala=1.0 | Rotación alrededor del centro geométrico |
| Volteo horizontal | `Core.flip()` | `flipCode=1` | Espejo sobre eje vertical (izq↔der) |
| Volteo vertical | `Core.flip()` | `flipCode=0` | Espejo sobre eje horizontal (arr↕aba) |

**Sobre la rotación con `warpAffine`:** Se preservan las dimensiones originales del canvas, lo que implica que las esquinas de la imagen rotada quedan recortadas. Si se deseara que la imagen completa quepa en el canvas, se ajustarían `cols` y `rows` de destino con trigonometría.

**Convención de `flip`:**
- `flipCode = 0` → volteo sobre eje X (vertical)
- `flipCode = 1` → volteo sobre eje Y (horizontal)
- `flipCode = -1` → volteo sobre ambos ejes simultáneamente

---

## Resultados Visuales

Las imágenes generadas por cada rutina se almacenan en la carpeta `Imagenes/`:

### Rutina 1 — Procesamiento Básico

| Imagen Original | Escala de Grises | Suavizado Gaussiano | Umbralización Binaria |
|:---:|:---:|:---:|:---:|
| `foto1.png` | `foto1_gris.png` | `foto1_suavizada.png` | `foto1_binaria.png` |
| Imagen RGB original | Canal de luminancia | Kernel 15×15, ruido eliminado | Umbral global=127 |

### Rutina 2 — Detección de Bordes

| Detector Canny | Gradiente Sobel (X+Y) |
|:---:|:---:|
| `foto1_canny.png` | `foto1_sobel.png` |
| Bordes finos con histéresis (80-200) | Magnitud del gradiente combinada |

### Rutina 3 — Detección de Rostros

| Imagen de Entrada | Resultado con Detecciones |
|:---:|:---:|
| `foto2.jpg` | `foto2_rostros.png` |
| Fotografía con personas | Rostros detectados con bounding boxes verdes |

### Rutina 4 — Transformaciones Geométricas

| Redimensionada 50% | Rotada 45° | Espejo Horizontal | Espejo Vertical |
|:---:|:---:|:---:|:---:|
| `foto1_redim.png` | `foto1_rotada.png` | `foto1_espejo_h.png` | `foto1_espejo_v.png` |

---

## Fundamentos Teóricos

### Espacio de Color BGR y Conversión a Grises

OpenCV usa internamente el orden **BGR** (Blue-Green-Red) en lugar del convencional RGB. La conversión a escala de grises aplica la fórmula de luminancia ponderada:

```
Y = 0.114·B + 0.587·G + 0.299·R
```

Los coeficientes son diferentes porque el ojo humano es más sensible al verde que al rojo y al azul.

### Filtrado Gaussiano

El filtro Gaussiano es una convolución con un kernel G(x,y) definido por:

```
G(x,y) = (1 / 2πσ²) · e^(-(x²+y²) / 2σ²)
```

Actúa como un filtro pasa-bajos que elimina frecuencias altas (ruido) mientras preserva estructuras de baja frecuencia (gradientes suaves). Es separable, lo que permite implementarlo eficientemente como dos convoluciones 1D.

### Operador de Canny

El algoritmo de Canny (1986) es el detector de bordes óptimo según tres criterios:
1. **Buena detección:** mínima probabilidad de falsos positivos y negativos
2. **Buena localización:** mínima distancia entre borde detectado y borde real
3. **Respuesta única:** un solo punto de respuesta por borde real

Pipeline: suavizado Gaussiano → gradiente Sobel → supresión de no-máximos → umbralización por histéresis.

### Operador de Sobel

Aproxima el gradiente de primer orden de la imagen usando kernels 3×3:

```
Gx = [-1  0  +1]    Gy = [-1  -2  -1]
     [-2  0  +2]         [ 0   0   0]
     [-1  0  +1]         [+1  +2  +1]
```

La magnitud total del gradiente es G = √(Gx² + Gy²), aproximada aquí como G ≈ 0.5|Gx| + 0.5|Gy|.

### Clasificador Haar en Cascada (Viola-Jones)

Viola y Jones (2001) propusieron un método de detección de objetos en tiempo real basado en:
1. **Características rectangulares de Haar:** diferencias de sumas de píxeles en regiones rectangulares adyacentes
2. **Imagen integral:** permite calcular cualquier característica rectangular en tiempo O(1)
3. **AdaBoost:** selecciona las características más discriminativas (de 180.000+ posibles, solo unas 6.000 son relevantes)
4. **Cascada de clasificadores:** rechaza rápidamente regiones sin objeto (>99% de ventanas de fondo son eliminadas en los primeros 2-3 estadios)

### Transformaciones Geométricas Afines

Las transformaciones afines preservan paralelismo y ratios de distancias. Se representan como matrices 2×3:

```
[x']   [a  b  tx] [x]
[y'] = [c  d  ty] [y]
                  [1]
```

Para rotación de ángulo θ con escala s: a=s·cos(θ), b=-s·sin(θ), c=s·sin(θ), d=s·cos(θ). El punto de pivote se controla mediante tx y ty.

---

## Requisitos del Sistema

| Componente | Versión Requerida | Descripción |
|---|---|---|
| **Sistema Operativo** | Windows 10/11 x64 | Las DLL nativas de OpenCV son para Windows x64 |
| **JDK** | Java 8+ (probado en Zulu 17.0.19) | Compatible con `javac.source=1.8` |
| **OpenCV** | 4.10.0 | Instalado en `C:\opencv\` |
| **NetBeans IDE** | 12+ | Para abrir el proyecto directamente |
| **Apache Ant** | 1.10+ | Incluido en NetBeans; para build desde CLI |

### Estructura de OpenCV requerida en disco

```
C:\opencv\
├── build\
│   ├── java\
│   │   ├── opencv-4100.jar         ← Bindings Java
│   │   └── x64\
│   │       └── opencv_java4100.dll ← Librería nativa (cargada en runtime)
│   └── etc\
│       └── haarcascades\
│           └── haarcascade_frontalface_default.xml  ← Requerido por Rutina3
```

---

## Instalación y Configuración

### 1. Instalar OpenCV 4.10.0

1. Descargar el instalador desde [opencv.org/releases](https://opencv.org/releases/)
2. Ejecutar y extraer en `C:\opencv\`
3. Verificar que existan los archivos:
   - `C:\opencv\build\java\opencv-4100.jar`
   - `C:\opencv\build\java\x64\opencv_java4100.dll`

### 2. Clonar el repositorio

```bash
git clone https://github.com/AlejoTechEngineer/opencv-vision-artificial-java.git
cd opencv-vision-artificial-java
```

### 3. Abrir en NetBeans IDE

1. `File` → `Open Project`
2. Navegar hasta la carpeta `LaboratorioOpenCV/`
3. NetBeans detectará automáticamente el proyecto Ant
4. El classpath ya apunta a `C:\opencv\build\java\opencv-4100.jar` (ver `project.properties`)

### 4. Verificar la JVM flag

En `nbproject/project.properties` la línea:
```
run.jvmargs=-Djava.library.path=C:\\opencv\\build\\java\\x64
```
apunta al directorio que contiene `opencv_java4100.dll`. Si OpenCV está en otra ruta, actualizar este valor.

### 5. Preparar las imágenes de entrada

Colocar en `Imagenes/`:
- `foto1.png` — cualquier imagen PNG para las rutinas 1, 2 y 4
- `foto2.jpg` — fotografía con uno o más rostros frontales para la rutina 3

---

## Ejecución del Proyecto

### Verificar instalación de OpenCV

Ejecutar `LaboratorioOpenCV.java` (clase principal del proyecto):

```
OpenCV version: 4.10.0-dev
Instalacion correcta.
```

### Ejecutar cada rutina individualmente

Cada clase tiene su propio `main`. En NetBeans, click derecho sobre el archivo → `Run File`.

**Rutina 1:**
```
Ruta: C:/Users/.../Imagenes/
Rutina 1 completada.
Dimensiones: 1080 x 1920
```
Genera: `foto1_gris.png`, `foto1_suavizada.png`, `foto1_binaria.png`

**Rutina 2:**
```
Rutina 2 completada.
Bordes detectados con Canny y Sobel.
```
Genera: `foto1_canny.png`, `foto1_sobel.png`

**Rutina 3:**
```
Rostros detectados: N
Rutina 3 completada.
```
Genera: `foto2_rostros.png` con bounding boxes verdes sobre cada rostro

**Rutina 4:**
```
Rutina 4 completada.
Tam. original:       1920 x 1080
Tam. redimensionado:  960 x  540
```
Genera: `foto1_redim.png`, `foto1_rotada.png`, `foto1_espejo_h.png`, `foto1_espejo_v.png`

### Build desde línea de comandos (Ant)

```bash
cd LaboratorioOpenCV
ant clean
ant compile
ant run
```

---

## Estructura del Proyecto

```
opencv-vision-artificial-java/
│
├── README.md
│
├── LaboratorioOpenCV/                          ← Proyecto NetBeans/Ant
│   ├── build.xml                               ← Script de build Ant
│   ├── manifest.mf                             ← Manifest del JAR
│   │
│   ├── src/
│   │   └── laboratorioopencv/
│   │       ├── LaboratorioOpenCV.java          ← Main: verificación de OpenCV
│   │       ├── Rutina1Basico.java              ← Grises, Gaussiano, umbral
│   │       ├── Rutina2Bordes.java              ← Canny, Sobel X+Y
│   │       ├── Rutina3Rostros.java             ← Haar Cascade face detection
│   │       └── Rutina4Transformaciones.java    ← Resize, rotate, flip
│   │
│   ├── nbproject/
│   │   ├── project.xml                         ← Metadata del proyecto
│   │   ├── project.properties                  ← Configuración de build/run
│   │   ├── build-impl.xml                      ← Implementación Ant generada
│   │   └── private/
│   │       └── private.properties              ← Rutas locales de la máquina
│   │
│   └── build/                                  ← Directorio generado (compilación)
│       └── classes/
│           └── laboratorioopencv/
│               ├── LaboratorioOpenCV.class
│               ├── Rutina1Basico.class
│               ├── Rutina2Bordes.class
│               ├── Rutina3Rostros.class
│               └── Rutina4Transformaciones.class
│
├── Imagenes/                                   ← Imágenes de entrada y resultados
│   ├── foto1.png                               ← Imagen base (entrada)
│   ├── foto2.jpg                               ← Fotografía con rostros (entrada)
│   ├── foto1_gris.png                          ← Salida: escala de grises
│   ├── foto1_suavizada.png                     ← Salida: Gaussian blur
│   ├── foto1_binaria.png                       ← Salida: umbralización binaria
│   ├── foto1_canny.png                         ← Salida: detector Canny
│   ├── foto1_sobel.png                         ← Salida: gradiente Sobel
│   ├── foto2_rostros.png                       ← Salida: rostros detectados
│   ├── foto1_redim.png                         ← Salida: redimensionada 50%
│   ├── foto1_rotada.png                        ← Salida: rotación 45°
│   ├── foto1_espejo_h.png                      ← Salida: volteo horizontal
│   └── foto1_espejo_v.png                      ← Salida: volteo vertical
│
└── Desarrollo Proyecto Alejandro De Mendoza.pdf  ← Informe académico del laboratorio
```

---

## Tecnologías Utilizadas

| Tecnología | Versión | Rol en el proyecto |
|---|---|---|
| **Java SE** | 8/17 (Zulu 17.0.19) | Lenguaje de programación principal |
| **OpenCV** | 4.10.0 | Librería de visión computacional (core, imgproc, imgcodecs, objdetect) |
| **NetBeans IDE** | 12+ | Entorno de desarrollo; gestión del proyecto |
| **Apache Ant** | 1.10+ | Sistema de automatización de build |
| **Haar Cascade XML** | OpenCV built-in | Modelo preentrenado para detección de rostros frontales |

### Módulos OpenCV utilizados

| Módulo | Clases importadas | Propósito |
|---|---|---|
| `core` | `Core`, `Mat`, `MatOfRect`, `Point`, `Rect`, `Scalar`, `Size`, `CvType` | Tipos de datos fundamentales y operaciones matriciales |
| `imgproc` | `Imgproc` | Procesamiento de imágenes (filtros, detección, transformaciones) |
| `imgcodecs` | `Imgcodecs` | Lectura y escritura de imágenes en disco |
| `objdetect` | `CascadeClassifier` | Detección de objetos con clasificadores en cascada |

---

## Autor

**Alejandro De Mendoza**  
Ingeniería Informática  
*Sistemas Multiagente y Percepción Computacional*

[![GitHub](https://img.shields.io/badge/GitHub-AlejoTechEngineer-181717?style=flat-square&logo=github)](https://github.com/AlejoTechEngineer)

---

<div align="center">
<sub>Laboratorio No. 2 · Sistemas Multiagente y Percepción Computacional · Ingeniería Informática</sub>
</div>
