# Modelo predictivo para el prediagnóstico de leishmaniasis cutánea en campo

Esta carpeta contiene el modelo predictivo que será usado en la herramienta no invasiva para el prediagnóstico de leihmaniasis cutanea en campo.

## Requisitos del sistema

* [Python 3.5](https://www.python.org/) 
* [TensorFlow](https://www.tensorflow.org/)
* [Keras](https://keras.io/) 
* [Pilow](https://pillow.readthedocs.io/en/5.1.x/) - utilizado en la carga de imágenes

## Entendimiento de lo datos

Los datos para desarrollar el modelo en cuestión fueron proporcionados por el centro internacional de entrenamiento e investigación medica (CIDEIM). Los datos consisten en un conjunto de imágenes de los casos reportados, los cuales están clasificados en verdaderos positivos, verdaderos negativos e indeterminados. 

 **verdaderos positivos:** casos de pacientes que tienen lesiones que son producto leishmaniasis cutanea.  
 **verdaderos negativos:** casos de pacientes que tienen lesiones que no son producto de de leishmaniasis.  
 **indeterminados:** casos de los cuales no hay registro virtual ya sea por que se omitio en el diagnóstico, o por que se perdio la información.  
 

## Preparación de los datos

Para utlizar estos datos se realizó una preparación previa de los mismos eliminando datos repetidos e inconsistentes, reclasificando imagenes mal clasificadas, buscando información de los casos indeterminados  y segmentando las lesiones de las imagenes.  
  
El resultado de la preparación de los datos fue un DataSet compuesto por 1541 imagenes de casos positivos y 481 imagenes de casos negativos. Los datos no serán publicados por cuestiones éticas. Para entrenar el modelo se dividen los datos en dos conjuntos, uno de entrenamiento y otro de validación, los cuales contienen el 80% (1618 imagenes, 1233 positivos - 385 negativos) y el 20% (404 imagenes, 308 positivos - 96 negativos) de los datos respectivamente.

## Modelado

## Referencias
[1] Francois Chollet. Building powerful image classification models using very little data. June 05, 2016. Disponible en: https://blog.keras.io/building-powerful-image-classification-models-using-very-little-data.html  
[2] Vikas Gupta. Keras turorial: Fine-tuning using pre-trained models. Feb 06, 2018. Disponible en: https://www.learnopencv.com/keras-tutorial-fine-tuning-using-pre-trained-models/
