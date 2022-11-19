# Cafetería UCC 
 Un sistema de punto de venta como proyecto final de la
 materia de Programación Avanzada. El usuario puede iniciar sesión, efectuar
 órdenes, agregar productos, y administrar usuarios, así como visualizar
 estadísticas acerca de su desempeño, haciendo uso de los tópicos vistos
 durante la materia, como estructuras de datos, eventos, programación
 multihilos y manejo de archivos y excepciones.
 
 ## Tecnologías 
 El sistema fue realizado en Java 1.8, sin hacer uso de
 librerías externas, aprovechando el diseñador gráfico de interfaces de
 Netbeans, Matisse.
 
 Un archivo de valores separados por comas (CSV) funge como base de datos
 local.
 
 ## Modelos 
 Los modelos diseñados para este proyecto son:
 
 ### Orden 
 Representa una orden efectuada por un usuario para un cliente. Dado
 que se toma como temática una cafetería informal, no se cuenta con el modelo
 de cliente. Los productos solicitados se almacenan en un arreglo, y la hora
 de inicio y fin como milisegundos desde el tiempo Epoch Unix.
 
 Sus campos son:
 
 - ID: Entero 
 - Número de mesa: Entero 
 - Clientes: Entero 
 - Orden: Arreglo de enteros 
 - Estado: Una enumeración con los valores "Proceso", "Cancelado" o
 "Finalizado" 
 - Hora de inicio: Entero largo 
 - Hora de fin: Entero largo 
 - Subtotal: Flotante 
 - Total: Flotante 
 - Comentario: Cadena 
 - ID de empleado: Entero
 
 ### Producto 
 Representa un producto a ser vendido al cliente. Sus campos son:
 
 - ID: Entero 
 - Nombre: Cadena 
 - Precio: Flotante 
 - Stock: Entero
 
 ### Usuario 
 Representa el empleado haciendo uso del sistema de punto de
 venta. Un usuario administrador podrá administrar a otros usuarios, mientras
 que otro usuario sin estos permisos sólo podrá modificar órdenes y productos.
 La contraseña del usuario se almacena como el resultado de una función hash
 (SHA-256), para proveer de mayor seguridad.
 
 Sus campos son: 
 - ID: Entero 
 - Nombre: Cadena 
 - Apellidos: Cadena 
 - Rol: Una enumeración con los valores "Usuario" o "Administrador" 
 - Fecha de inicio: Entero largo 
 - RFC: Cadena 
 - Usuario: Cadena 
 - Hash: Cadena
 
 ## Uso 
 Al ejecutar el programa, aparecerá una pantalla de inicio de sesión,
 por defecto se incluye el super usuario "admin" con el nombre de usuario y
 contraseña homónimos, así como el usuario regular "usuario". El programa le
 informará de haber ingresado un usuario o contraseña incorrectos.
 
 Al ingresar a la ventana principal se observan una serie de enlaces laterales
 para navegar entre las distintas pantallas, entre las cuales se encuentran:
 
 - Dashboard: Provee de algunas métricas generales relevantes, así como de un
 mecanismo para cancelar o finalizar pedidos de forma rápida. 
 - Órdenes: Permite visualizar un histórico de las órdenes realizadas, así como crear,
 editar o eliminarlas. 
 - Productos: Lista los productos disponibles dentro del
 punto de venta, así como permite agregar, alterar o eliminar productos. 
 - Estadísticas: Genera un análisis a fondo acerca de las métricas relevantes
 para las órdenes realizadas, los productos guardados y los usuarios
 registrados. 
 - Usuarios: Permite administrar los usuarios del sistema.
 
 Al crear o editar un registro, aparecen los cuadros de diálogo modales para
 capturar la información pertinente y permiten cancelar o realizar la
 aprobación, tras verificar los campos ingresados.
 
 Las pantallas con vistas de tablas cuentan con un sistema de refresco rápido
 de información para permitir procesar información entrante de otros medios,
 como por ejemplo, la edición directa del archivo de base de datos.
 
 Al crear, editar o eliminar un registro, se actualizan de forma instantánea
 las métricas relevantes dentro del Dashboard y de la pantalla de
 Estadísticas.
 
 Modificar los stock o precios de productos no existentes no alteran las
 órdenes anteriores, permitiendo un control más exacto de las ganancias y de
 promociones recurrentes. No es posible colocar órdenes con las cual no se
 cuente con el stock suficiente.
 
 ### Puntos de mejora 
 Dado que para la presente asignación sólo se permite la
 subida de dos archivos dentro de la plataforma escolar, algunos de los puntos
 a desarrollar para contar con un producto más pulido son:
 
 - La realización de una API con las tecnologías de Spring Boot y Hibernate,
 separando responsabilidades, basada en una instancia en la nube de PostgreSQL
 o MongoDB. 
 - El uso de librerías de terceros para la interconexión con dicha
 API. 
 - Algunos diseños responsivos.
