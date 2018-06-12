import tensorflow as tf
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense
from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img

# Parametros de la carga de datos
batch_size = 16
imgage_size = 300
train_directory = 'data/train'
test_directory = 'data/validation'
train_images = 1200
test_images = 200
class_mode = 'binary'

# Hiper Parametros de la CNN
epochs = 50

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

# Crear modelo usando keras
model = Sequential()

# Construir el modelo utilizando keras







# Compilar modelo
# Parametros
#   funcion de perdida = binary_crossentropy [ y*log(y') + (1-y)*log(1-y') ] y=etuiqueta, y'=predición
#   optimizador = rmsprop -> tasa de parendizaje adaptativa
model.compile(loss='binary_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])


# Agregar flujes de entrenamiento y prueba al modelo e inciar el entrenamiento
model.fit_generator(
    generator=train_flow,
    steps_per_epoch=train_images//batch_size,
    epochs=epochs,
    validation_data=test_flow,
    validation_steps=test_images//batch_size)
    
# Guardar los pesos de los parametros resultantes del entrenamiento
model.save_weigths("Pesos_1.h5")
