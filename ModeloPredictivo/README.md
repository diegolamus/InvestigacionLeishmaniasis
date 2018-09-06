# Modelo predictivo para el prediagnóstico de leishmaniasis cutánea en campo

Esta carpeta contiene el modelo predictivo que será usado en la herramienta no invasiva para el prediagnóstico de leihmaniasis cutanea en campo.

## Requisitos del sistema

* [Python 3.5](https://www.python.org/) 
* [TensorFlow](https://www.tensorflow.org/)
* [Keras](https://keras.io/) 
* [Pilow](https://pillow.readthedocs.io/en/5.1.x/) - utilizado en la carga de imágenes

## Entendimiento de lo datos

Los datos para desarrollar el modelo en cuestión fueron proporcionados por el centro internacional de entrenamiento e investigación medica (CIDEIM). Los datos consisten en un conjunto de imágenes de los casos reportados, los cuales están clasificados en verdaderos positivos, verdaderos negativos e indeterminados. 

 **Verdaderos positivos:** casos de pacientes que tienen lesiones que son producto leishmaniasis cutanea.  
 **Verdaderos negativos:** casos de pacientes que tienen lesiones que no son producto de de leishmaniasis.  
 **Indeterminados:** casos de los cuales no hay registro virtual ya sea por que se omitio en el diagnóstico, o por que se perdio la información.  
 

## Preparación de los datos

Para utlizar estos datos se realizó una preparación previa de los mismos eliminando datos repetidos e inconsistentes, reclasificando imagenes mal clasificadas, buscando información de los casos indeterminados  y segmentando las lesiones de las imagenes.  
  
El resultado de la preparación de los datos fue un data set compuesto por 1541 imagenes de casos positivos y 481 imagenes de casos negativos. Los datos no serán publicados por cuestiones éticas. Para entrenar el modelo se dividen los datos en dos conjuntos, uno de entrenamiento y otro de validación, los cuales contienen el 80% (1618 imagenes, 1233 positivos - 385 negativos) y el 20% (404 imagenes, 308 positivos - 96 negativos) de los datos respectivamente.

## Metricas de evaluación

Para evaluar el desempeño del modelo, y para comparar que tan bien desempeña un modelo en relación a otro es importante establecer metricas de evaluación. En el conexto medico, en el diagnóstico de enfermedades, se utilizan dos metricas importantes, sensibilidad y especificidad [3]; la sensibilidad nos indica que proproción de los casos que se reporteron como positivos eran realmente positivos y la especificidad..

## Modelado

Para la etapa de modelado se experimentará con varias aproximaciones. Primero, se construye una red neuronal convolucional desde cero. Segundo, se utiliza la parte convolucional de redes ya construidas, como Alexnet, y se entrena solo las capas densas de clasificación (Transfer learning). Finalmente, se utilizará la parte convolucional de redes ya construidas, pero se entrenará no solo las capas de clasificación sino algunas capas convolucionales (Fine tuning). 

Para determinar que aproximación es la mejor para resolver este problema se establecen 3 metricas relevantes para el analisis ( precision, recall y sensibilidad). Se corre un número determinado de epocas, por ejemplo 500, y se extraen las metricas, analizando su evolución un punto máximo.

### 1. Construcción de red neuronal desde cero

### 2. Transfer learning

### 3. Fine tuning


## Referencias
[1] Francois Chollet. Building powerful image classification models using very little data. June 05, 2016. Disponible en: https://blog.keras.io/building-powerful-image-classification-models-using-very-little-data.html  
[2] Vikas Gupta. Keras turorial: Fine-tuning using pre-trained models. Feb 06, 2018. Disponible en: https://www.learnopencv.com/keras-tutorial-fine-tuning-using-pre-trained-models/
[3] Comunicación personal. Tutora
