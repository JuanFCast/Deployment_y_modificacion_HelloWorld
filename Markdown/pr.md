# Implementación de HelloWorld-ICE: Documentación

**Curso de Arquitectura de Software**

## Participantes

- Juan Felipe Castillo Gomez
- Alexander Echeverry Ramirez

## Asignación de Responsabilidades

- **Juan Felipe Castillo Gomez**
    - Desarrolló el despliegue en servidor y cliente.
    - Realizó modificaciones al código original.
    - Elaboró la documentación.

- **Alexander Echeverry Ramirez**
    - Lideró el despliegue en servidor y cliente.
    - Trabajó en la modificación y mejora del nuevo código.
    - Diseñó e implementó pruebas para medir la calidad.

## Descripción del Proyecto

HelloWorld-ICE es un sistema de comunicación cliente-servidor fundamentado en ICE. Su objetivo es permitir que el cliente se comunique con el servidor a través de una interfaz de línea de comandos. Se han incorporado pruebas que evalúan diversos atributos de calidad del sistema de comunicación, como rendimiento, tiempo de respuesta y tasas de eventos perdidos o no procesados.

## Uso

### Configuración Inicial:
- SDK Java 11
- Herramientas middleware de ICE descargadas
- Gradle

### Compilación:
Ejecute primero el servidor y luego el cliente.

---

# Documentación de la clase *Client*

Esta clase sirve como cliente principal que se comunica con un servidor utilizando el middleware Ice.

## Métodos

### `main(String[] args)`
Inicializa la comunicación con el servidor usando el archivo `config.client`.

### `menu(Demo.PrinterPrx twoway, Scanner scanner)`
Ofrece al usuario un menú con opciones, como enviar un mensaje o realizar pruebas.

### `send(Demo.PrinterPrx twoway, Scanner scanner)`
Facilita al usuario enviar un mensaje formateado al servidor.

### `send(Demo.PrinterPrx twoway, String message)`
Variante del método anterior que acepta un mensaje directamente.

### `evaluateThroughput(Demo.PrinterPrx twoway, Scanner scanner)`
Evalúa el rendimiento del sistema midiendo el tiempo que tarda en enviar una cantidad específica de solicitudes.

### `evaluateResponseTime(Demo.PrinterPrx twoway)`
Calcula el tiempo que tarda el servidor en responder a una solicitud.

### `evaluateMissingRate(Demo.PrinterPrx twoway, Scanner scanner)`
Determina la tasa de solicitudes perdidas al enviar una cantidad específica de solicitudes y ver cuántas no obtienen respuesta.

### `evaluateUnprocessedRate(Demo.PrinterPrx twoway, Scanner scanner)`
Calcula la tasa de solicitudes que el servidor no pudo procesar.

### `getHostname()`
Obtiene el nombre de host del sistema local.

## Notas
La clase utiliza la biblioteca de middleware Ice y los mensajes se formatean antes de ser enviados.

---

# Documentación de la clase *Server*

Esta clase actúa como el servidor principal, escuchando y procesando solicitudes de clientes a través del middleware Ice.

## Métodos

### `main(String[] args)`
Es el método inicial del servidor que configura y arranca usando `config.server`.

1. Inicializa la comunicación con el cliente.
2. Si hay argumentos extra después de la inicialización, los muestra.
3. Crea un objeto adaptador con el nombre `Printer`.
4. Agrega un objeto del tipo `PrinterI` al adaptador con la identidad `SimplePrinter`.
5. Activa el adaptador para que comience a escuchar las solicitudes.
6. El servidor espera a que se le indique que se apague.

### `f(String m)`

Este método recibe un comando en forma de cadena de texto, lo ejecuta y devuelve su salida.

1. Ejecuta el comando proporcionado utilizando el método `exec()` de la clase `Runtime`.
2. Lee y devuelve la salida del comando.
3. Si ocurre una excepción al ejecutar el comando, devuelve un mensaje de error con la excepción.

## Notas
Se utiliza la biblioteca middleware Ice y la configuración se basa en el archivo `config.server`.

---

# Clase *PrinterI*

Implementación de la interfaz `Printer`. Gestiona diferentes tipos de mensajes y realiza acciones según la entrada.

## Métodos

### `printString(String s, com.zeroc.Ice.Current current)`

Implementa el método de la interfaz `Printer`. Analiza y procesa la cadena de entrada y devuelve una respuesta adecuada.

- Parámetros:
    - `s`: Cadena en el formato `clientUsername:clientHostname:message`.
    - `current`: Información sobre la invocación del método actual (proporcionada por el framework Ice).

- Devuelve: Una respuesta basada en el mensaje de entrada.

### Métodos Privados

#### `handlePositiveInteger(...)`

Maneja un mensaje que contiene un número entero positivo y calcula los factores primos del número, añadiendo el resultado a la respuesta.

#### `handleListInterfaces(...)`

Maneja una solicitud para listar las interfaces lógicas de red del sistema y las añade a la respuesta.

#### `handleListPorts(...)`

Maneja una solicitud para listar los puertos abiertos en una dirección IP específica y añade los resultados a la respuesta.

#### `handleExecuteCommand(...)`

Maneja una solicitud para ejecutar un comando del sistema y añade la salida del comando o un mensaje de error a la respuesta.

#### `handleDefaultMessage(...)`

Maneja cualquier otro tipo de mensaje predeterminado y construye una respuesta que contiene el nombre de usuario del cliente, el nombre del host y el mensaje.

#### `formatLogMessage(...)`

Formatea un mensaje de registro usando el nombre de usuario del cliente, el nombre del host y la acción realizada.

- Devuelve: Una cadena de mensaje de registro formateada.

#### `isPositiveInteger(String s)`

Verifica si una cadena dada es un número entero positivo.

- Parámetros:
    - `s`: Cadena a verificar.

- Devuelve: Verdadero si 's' es un entero positivo, falso en caso contrario.

#### `primeFactors(int n)`

Calcula los factores primos de un número entero.

- Parámetros:
    - `n`: Número para el cual calcular los factores primos.

- Devuelve: Una lista de factores primos de 'n'.

#### `listInterfaces()`

Lista las interfaces de red lógicas en el sistema anfitrión.

- Devuelve: Una lista de interfaces de red lógicas o un mensaje de error.

#### `listOpenPorts(String ipAddress)`

Lista los puertos abiertos en una dirección IP dada.

- Parámetros:
    - `ipAddress`: Dirección IP para la cual listar los puertos abiertos.

- Devuelve: Una lista de puertos abiertos o un mensaje de error.

#### `executeSystemCommand(String command)`

Ejecuta un comando del sistema y devuelve su salida.

- Parámetros:
    - `command`: Comando a ejecutar.

- Devuelve: La salida del comando o un mensaje de error.

## Notas

- Esta implementación de la interfaz `Printer` ofrece varias funcionalidades específicas como listar interfaces de red, puertos abiertos y ejecución de comandos del sistema.
