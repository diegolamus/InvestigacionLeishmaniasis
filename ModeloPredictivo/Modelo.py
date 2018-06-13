import tensorflow as tf
import matplotlib.pyplot as plt
from keras.applications import VGG16, VGG19, ResNet50, InceptionV3, InceptionResNetV2
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img

# Parametros de la carga de datos
batch_size = 16
imgage_size = 500
train_directory = 'datos/entrenamiento'
test_directory = 'data/prueba'
train_images = 928
test_images = 233
class_mode = 'binary'

# Hiper Parametros entrenamiento
epochs = 500
conv_trainable_layers =0
guardar_como= 'Prueba_VGG16' #Con que nombre se van a aguardar los pesos y exportar la historia

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

# Cargar modelo pre-entrenado (Transfer Learning). Solo se carga uno.
#conv_layers = VGG16(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
#conv_layers = VGG19(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
#conv_layers = ResNet50(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
#conv_layers = InceptionV3(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))
#conv_layers = InceptionResNetV2(weights='imagenet',include_top=False,input_shape=(imgage_size, imgage_size, 3))

# Congelar las capas que no se quieren entrenar
for layer in conv_layers.layers[:-conv_trainable_layers]:
    layer.trainable = False

#Imprimir las capas que se van a entrenar
for layer in conv_layers.layers:
    print(layer, layer.trainable)
    
# Crear modelo usando keras
model = Sequential()
model.add(conv_layers) # Se agrega el modelo pre cargado al actual
model.add(layers.Flatten())
model.add(layers.Dense(1024, activation='relu'))
model.add(layers.Dropout(0.5))
model.add(Dense(1, activation='sigmoid'))

# Compilar modelo
# Parametros
#   funcion de perdida = binary_crossentropy [ y*log(y') + (1-y)*log(1-y') ] y=etuiqueta, y'=predición
#   optimizador = rmsprop -> tasa de parendizaje adaptativa
model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])


# Agregar flujes de entrenamiento y prueba al modelo e inciar el entrenamiento
historia = model.fit_generator(
    generator=train_flow,
    steps_per_epoch=train_images//batch_size,
    epochs=epochs,
    validation_data=test_flow,
    validation_steps=test_images//batch_size)

# Guardar los pesos de los parametros resultantes del entrenamiento
model.save_weigths('pesosEntrenamiento/'+guardar_como+'.h5")

# Exportar los pesos a un archivo de texto
precision_entrenamiento = historia.history['acc'] # recuperar precision datos de entrenamiento
precision_validacion = historia.history['val_acc'] # recuperar precision datos de prueba
archivo = open('historia/'+guardar_como+'.txt','w')
archivo.write('Precisión Entrenamiento,Precisión Prueba')
for indice in range(0, epochs):
    archivo.write(str(precision_entrenamiento[indice])+','+str(precision_validacion[indice]))
archivo.close()

