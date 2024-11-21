**Aplicación de prueba para el proceso de selección**

### Desarrollar una aplicación móvil que permita gestionar propiedades.

**Author: Herson Viveros (Semi Sr. Android Developer)**

- [La aplicación aplicara las siguientes normas:]

## Arquitectura MVVM
La arquitectura MVVM (Modelo-Vista-ViewModel) es una forma de diseñar aplicaciones
que separa la lógica de la vista y el modelo de datos. En este proyecto,
he utilizado esta arquitectura para separar claramente las responsabilidades
de cada componente de la aplicación.

**Model**
En el paquete "model" se encuentran los modelos de datos y las clases encargadas
de la comunicación con el servidor (si la aplicación lo requiere).

**View**
En el paquete "view" se encuentran las clases encargadas de
la interfaz de usuario (Activity, Fragment, etc.) y su correspondiente diseño XML.

**ViewModel**
En el paquete "viewmodel" se encuentran las clases encargadas
de la lógica de la aplicación y de proporcionar los datos necesarios a la vista.
Los viewmodels se comunican con el modelo y exponen datos a la vista a través de LiveData.

**HILT**
Framework de inyección de dependencias para Android que se encarga de simplificar
y automatizar el proceso de creación, configuración y gestión de objetos dependientes.

**Room**
Biblioteca de persistencia de datos que simplifica el acceso y almacenamiento de
datos en una base de datos SQLite en dispositivos Android, utilizando una arquitectura
basada en componentes para integrarse fácilmente con otras bibliotecas de Android.

**Kotlin**
Kotlin es un lenguaje de programación moderno y seguro que se ha convertido en
la opción preferida de nosotros los desarrolladores para el desarrollo de aplicaciones
Android. En este proyecto, he utilizado Kotlin para escribir todo el código de la aplicación.

**Git**
Git es un sistema de control de versiones que nos permite mantener un registro de todos
los cambios realizados en el código de la aplicación. En este proyecto, he utilizado
Git para gestionar el código fuente de la aplicación.


## ¡Gracias por leer!