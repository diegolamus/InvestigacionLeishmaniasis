﻿import tensorflow as tf
import os
from keras.applications import Xception, VGG16, VGG19, ResNet50, InceptionV3, InceptionResNetV2, MobileNet, DenseNet121
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
from keras.callbacks import ModelCheckpoint

# Parametros de la carga de datos
batch_size = 16
imgage_size = 300
train_directory = 'src/datos/entrenamiento'
test_directory = 'src/datos/prueba'
train_images = 1618
test_images = 404
class_mode = 'binary'

# HiperParametros entrenamiento
epochs = 1
guardar_como= 'Prueba_Xception' #Con que nombre se van a aguardar los pesos y exportar la historia

def precision(y_true, y_pred):
    TP,FP,TN,FN = 0,0,0,0
    if (y_true == and y_true==y_pred):
        TP +=1
    elif (y_true == and y_true!=y_pred):
        FP +=1
    elif (
        
    
def recall(y_true, y_pred):

def specificity(y_true, y_pred):

def kappa(y_true, y_pred):
    
    
def get_CNN_model():
    return None

def construir_modelo():
    # Cargar modelo pre-entrenado (Transfer Learning). Solo se carga uno.
    conv_layers = Xception(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = VGG16(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = VGG19(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = ResNet50(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = InceptionV3(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = InceptionResNetV2(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = MobileNet(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    #conv_layers = DenseNet121(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
    
    # Congelar las capas que no se quieren entrenar
    for layer in conv_layers.layers[:]:
        layer.trainable = False
        
    # Imprimir las capas que se van a entrenar
    for layer in conv_layers.layers:
        print(layer, layer.trainable)
        
    # Crear modelo usando keras
    modelo = Sequential()
    modelo.add(conv_layers) # Se agrega el modelo pre-cargado al actual
    modelo.add(Flatten())
    modelo.add(Dense(1024, activation='relu'))
    modelo.add(Dropout(0.5))
    modelo.add(Dense(1, activation='sigmoid'))
    modelo.summary()
    return modelo


def entrenar_modelo(modelo):
    # Generador de datos para transformar y redimensionar las imagenes de entrenamiento existentes
    # Salida: 4 posibles transformaciones de la imagen original
    #         (original, rotación horizontal, rotación vertical, rotación horizontal + vertical)
    #         redimensionadas a 1./255
    train_datagen = ImageDataGenerator(
        rescale=1./255,
        horizontal_flip=True,
        vertical_flip=True)
    # Generador de datos para redimensionar las imagenes de prueba
    # Salida: imagenes de prueba redimensionadas a 1./255
    test_datagen = ImageDataGenerator(rescale=1./255)
    # flujo de datos de entrenamiento
    # Salida: lotes de imagenes transformadas mediante train_datagen con etiquetas
    #         correspondientes al nombre de su subfolder ubicado en train_directory (positivo, negativo).
    train_flow = train_datagen.flow_from_directory(
        directory=train_directory,  
        target_size=(imgage_size, imgage_size),  
        batch_size=batch_size,
        class_mode=class_mode)
    # flujo de datos de prueba
    # Salida: lotes de imagenes redimensionadas mediante test_datagen con etiquetas
    #         correspondientes al nombre de su subfolder ubicado en train_directory (positivo, negativo).
    test_flow = test_datagen.flow_from_directory(
        directory=test_directory,
        target_size=(imgage_size, imgage_size),
        batch_size=batch_size,
        class_mode=class_mode)
    # Compilar modelo
    # Parametros
    #   funcion de perdida = binary_crossentropy [ y*log(y') + (1-y)*log(1-y') ] y=etuiqueta, y'=predición
    #   optimizador = rmsprop -> tasa de parendizaje adaptativa
    modelo.compile(
        loss='binary_crossentropy',
        optimizer='rmsprop',
        metrics=['accuracy'])
    #Agregar checkpoint para cargar mejores pesos
    rutaMejoresPesos='src/pesosEntrenamiento/MejoresPesos_' + guardar_como+ '.hdf5'
    checkpoint = ModelCheckpoint(rutaMejoresPesos, monitor='val_acc', verbose=1, save_best_only=True, mode='max')
    # Agregar flujes de entrenamiento y prueba al modelo e inciar el entrenamiento
    historia = modelo.fit_generator(
        generator=train_flow,
        steps_per_epoch=3,#train_images//batch_size,
        epochs=epochs,
        validation_data=test_flow,
        validation_steps=3,#test_images//batch_size,
        callbacks=[checkpoint],
        verbose=1)
    return historia


def exportar(modelo,historia):
    # Exportar la historia a un archivo de texto
    precision_entrenamiento = historia.history['acc'] # recuperar precision datos de entrenamiento
    precision_validacion = historia.history['val_acc'] # recuperar precision datos de prueba
    archivo = open('src/historia/'+guardar_como+'.txt','w')
    archivo.write('Precisión Entrenamiento,Precisión Prueba' + '\n')
    for indice in range(0, epochs):
        archivo.write(str(precision_entrenamiento[indice])+','+str(precision_validacion[indice])+ '\n')
    archivo.close()
    print('historia exportada')

    #Exportar modelo a h5
    modelo.load_weights('src/pesosEntrenamiento/MejoresPesos_' + guardar_como+ '.hdf5')
    os.remove('src/pesosEntrenamiento/MejoresPesos_' + guardar_como+ '.hdf5')
    modelo.save('src/pesosEntrenamiento/MejoresPesos_' + guardar_como+ '.h5')

def main():
    modelo = construir_modelo()
    historia = entrenar_modelo(modelo)
    exportar(modelo, historia)


if __name__ == '__main__':
    main()


