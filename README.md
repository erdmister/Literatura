# 📚 Busqueda de libros

## ✨ Descripción

Este proyecto implementa una aplicación para gestionar información sobre libros y autores, consumiendo datos de la API Gutendex. Permite buscar libros por título, listar libros y autores registrados, buscar autores vivos en un año específico y listar libros por idioma.

## 📁 Estructura del Proyecto

El proyecto está estructurado en los siguientes paquetes:
com.alura.Literatura
- │── model           # Clases de modelo (Autor, Libro, Datos, etc.)
- │── principal       # Clase Principal (gestiona la ejecución del programa)
- │── repository      # Interfaces JPA Repository para la persistencia
- │── services        # Servicios para consumir la API y procesar datos
- │── DesafioLiteraturaApplication.java  # Clase principal de Spring Boot

## 📚 Dependencias

El proyecto utiliza las siguientes dependencias principales:

*   Spring Boot Starter Data JPA
*   Spring Boot Starter Web
*   Jackson (para la conversión de JSON)

## 🔧 Uso

Cuando ejecutas la aplicación, se muestra un menú en la terminal con las siguientes opciones:

1- Buscar libro por título

2- Listar libros registrados

3- Listar autores registrados

4- Listar autores vivos en un determinado año

5- Listar libros por idioma

0- Salir

### Ejemplo de uso

1️⃣ Ingresar el título de un libro para buscarlo en la API de Gutendex.

2️⃣ Si el libro existe y no está registrado, se guardará en la base de datos.

3️⃣ Se pueden listar libros o autores registrados en la base de datos.

4️⃣ Se puede filtrar autores vivos en un determinado año o libros por idioma.

## 🌍 API Externa Utilizada

La aplicación obtiene los datos de libros desde la API pública de Gutendex: 📚
🔗 [Gutendex API](http://gutendex.com)

### 📜 ¿Qué información proporciona Gutendex?

La API de Gutendex permite acceder a un amplio catálogo de libros de dominio público y proporciona datos como:a

📖 Título del libro

✍️ Nombre del autor

📅 Año de publicación

🌍 Idioma

Los datos obtenidos se almacenan en la base de datos para facilitar consultas posteriores.

### 🔧 ¿Cómo funciona la integración con la API?

1️⃣ Se realiza una solicitud HTTP a la API de Gutendex utilizando RestTemplate o WebClient en Spring Boot.

2️⃣ La API responde con un JSON que contiene la información de los libros solicitados.

3️⃣ Los datos relevantes se extraen y transforman en objetos Libro y Autor.

4️⃣ Si el libro no está en la base de datos, se guarda automáticamente.

5️⃣ Se pueden realizar consultas sobre los datos almacenados en la base de datos para recuperar información sin necesidad de hacer nuevas peticiones a la API.
