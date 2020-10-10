# LABORATORIO 7 ARSW STOMP WEBSOCKETS

#### Integrantes
- Sergio Alejandro Bohórquez Alzate
- Angi Paola Jimenez Pira

### Prerequisitos
Para correr el proyecto debe tener instalados los siguientes programas en su computador:

- Java SE Development Kit 8
- Java SE Runtime Environment 8
- Maven para poder construir el proyecto.
- Git para clonar el repositorio.

### Instalación y pruebas

Clone este repositorio y compilelo con los siguientes comandos:

    git clone https://github.com/AlejandroBohal/LAB7-ARSW-SOCKETS
    cd LAB7-ARSW-SOCKETS
    mvn package

Una vez clonado puede correr el servidor con el siguiente comando:

    mvn exec:java -Dexec.mainClass="edu.eci.arsw.cinema.CinemaAPIApplication";

Puede dirigirse al localhost:8080/index.html o localhost:8080/stomp.html,
Para correr las pruebas use el siguiente comando

    mvn test

## Parte I.
Para las partes I y II, usted va a implementar una herramienta que permita integrarse al proyecto de el proyecto de compra/reserva de tickets, basada en el siguiente diagrama de actividades:

Para esto, realice lo siguiente:

1. Agregue en la parte inferior del canvas dos campos para la captura de las posiciones de los asientos a comprar (row, col), y un botón 'Buy ticket' para hacer efectiva la compra

    ![](https://media.discordapp.net/attachments/352624122301513730/764292894843600916/unknown.png)
    
2. Haga que la aplicación HTML5/JS al ingresarle en los campos de row y col y oprimir el botón, si el asiento está disponible, los publique en el tópico: /topic/buyticket . Para esto tenga en cuenta (1) usar el cliente STOMP creado en el módulo de JavaScript y (2) enviar la representación textual del objeto JSON (usar JSON.stringify).
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764293078172303400/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764293280946716672/unknown.png)

3. Dentro del módulo JavaScript modifique la función de conexión/suscripción al WebSocket, para que la aplicación se suscriba al tópico "/topic/buyticket" (en lugar del tópico /TOPICOXX). Asocie como 'callback' de este suscriptor una función que muestre en un mensaje de alerta (alert()) el evento recibido. Como se sabe que en el tópico indicado se publicarán sólo ubicaciones de asientos, extraiga el contenido enviado con el evento (objeto JavaScript en versión de texto), conviértalo en objeto JSON, y extraiga de éste sus propiedades (row y col). Para extraer el contenido del evento use la propiedad 'body' del mismo, y para convertirlo en objeto, use JSON.parse.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764294963373801544/unknown.png)

4. Compile y ejecute su aplicación. Abra la aplicación en varias pestañas diferentes (para evitar problemas con el caché del navegador, use el modo 'incógnito' en cada prueba).
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764295358393876500/unknown.png)

## Parte II.

Para hacer mas útil la aplicación, en lugar de capturar las coordenadas con campos de formulario, las va a capturar a través de eventos sobre el elemento de tipo <canvas>. De la misma manera, en lugar de simplemente mostrar las coordenadas enviadas en los eventos a través de 'alertas', va a cambiar el color de dichos asientos en el canvas simulando la compra de los mismos.

1. Haga que el 'callback' asociado al tópico /topic/buyticket en lugar de mostrar una alerta, cambie de color a rojo el asiento en el canvas en la ubicación fila - columna enviadas con los eventos recibidos.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764298095436759060/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764298285375815690/unknown.png)

2. Haga uso del método 'getMousePosition' provisto y agregue al canvas de la página un manejador de eventos que permita capturar los 'clicks' realizados, bien sea a través del mouse, o a través de una pantalla táctil.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764299476139114536/unknown.png)

3. Elimine los inputs de entrada de "row" y "col" y agregue lo que haga falta en sus módulos para que cuando se capturen nuevos 'clicks' en el canvas: (si no se ha seleccionado un canvas NO se debe hacer nada):
    1. Se calcule de acuerdo a las coordenadas del canvas y a la ubicación de los asientos, la fila y la columna del asiento sobre el cual se dio 'click'.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764301018409467904/unknown.png)
    
    En el método draw seats:
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764301396106936351/unknown.png)
    
    2. Cambie la funcionalidad del botón 'Buy Ticket' para que ahora cuando se oprima habilite el EventListener de los clicks sobre el canvas.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764299187625000960/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764300525620559882/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764299476139114536/unknown.png)
    
    3. Utilice las coordenadas sobre las cuales el usuario dio click para identificar el asiento y, si el asiento está disponible realizar la compra del mismo y publique las ubicaciones en el tópico: /topic/buyticket, (Por ahora solo modificando los asientos del js).
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764301765079203840/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764302355562102814/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764302478174453780/unknown.png)

4. Ejecute su aplicación en varios navegadores (y si puede en varios computadores, accediendo a la aplicación mendiante la IP donde corre el servidor). Compruebe que a medida que selecciona un asiento (es decir realiza la compra del mismo ahora sin necesidad del botón), la compra del mismo es replicada en todas las instancias abiertas de la aplicación (el color de las sillas verdes disponibles debe cambiar a rojo).
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764302863400304660/unknown.png)

## Parte III.
Ajuste la aplicación anterior para que pueda manejar la compra de asientos en más de una sala a la vez, manteniendo tópicos independientes. Para esto:

1. Agregue tres campo en la vista: nombre del cinema, fecha de la función y nombre de la película. La concatenación de estos datos corresponderá al identificador de la función.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764304125546463302/unknown.png)

2. Modifique la aplicación para que, en lugar de conectarse y suscribirse automáticamente (en la función init()), lo haga a través de botón 'conectarse'. Éste, al oprimirse debe realizar la conexión y suscribir al cliente a un tópico que tenga un nombre dinámico, asociado el identificador mencionado anteriormente, por ejemplo: /topic/buyticket.cinemaX.2018-12-19.SuperHeroes_Movie, /topic/buyticket.cinemaY.2018-12-19.The_Enigma, para las funciones del CinemaX y CinemaY respectivas.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764305982020386836/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764306107173699584/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764306339252928533/unknown.png)

3. De la misma manera, haga que las publicaciones se realicen al tópico asociado al identificador ingresado por el usuario.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764306471331954729/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764306649883607081/unknown.png)

4. Rectifique que se puedan realizar dos compras de asientos de forma independiente, cada uno de éstos entre dos o más clientes.
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764306735971958824/unknown.png)

### Parte IV.
Para la parte IV, usted va a implementar una versión extendida del modelo de actividades y eventos anterior, en la que el servidor (que hasta ahora sólo fungía como Broker o MOM -Message Oriented Middleware-) se volverá también suscriptor de ciertos eventos, para a partir de los mismos agregar la funcionalidad de 'compra/reserva de entradas de cine':

1. Cree una nueva clase que haga el papel de 'Controlador' para ciertos mensajes STOMP (en este caso, aquellos enviados a través de "/app/buyticket.{cinemaName}.{functionDate}.{movieName}"). A este controlador se le inyectará un bean de tipo SimpMessagingTemplate, un Bean de Spring que permitirá publicar eventos en un determinado tópico. Por ahora, se definirá que cuando se intercepten los eventos enviados a "/app/buyticket.{cinemaName}.{functionDate}.{movieName}" (que se supone deben incluir un asiento), se mostrará por pantalla el asiento recibido, y luego se procederá a reenviar el evento al tópico al cual están suscritos los clientes "/topic/buyticket".

    ![](https://media.discordapp.net/attachments/352624122301513730/764312358880936027/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764312743766654996/unknown.png)

2. Ajuste su cliente para que, en lugar de publicar los puntos en el tópico /topic/buyticket.{cinemaname}, lo haga en /app/buyticket.{cinemaname}. Ejecute de nuevo la aplicación y rectifique que funcione igual, pero ahora mostrando en el servidor los detalles de los puntos recibidos.

    ![](https://media.discordapp.net/attachments/352624122301513730/764313087666814986/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764313190217285648/unknown.png)
    
    ![](https://media.discordapp.net/attachments/352624122301513730/764313262586069002/unknown.png)
    

3. Una vez rectificado el funcionamiento, se quiere aprovechar este 'interceptor' de eventos para cambiar ligeramente la funcionalidad :

    2. Volviendo a la aplicación alojada en index.html y app.js, modifique lo que sea necesario para que a la hora de que se consulten las funciones de un determinado cine y se oprima el botón 'Open Seats' de una función, la aplicación se suscriba al tópico respectivo.
    
    3. Agregue y habilite el botón 'buy ticket', con la misma funcionalidad de la parte II Punto 3.2, y la funcionalidad de la parte II Punto 3.3
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764316621229129748/unknown.png)
    
    4. El manejador de eventos del servidor /app/buyticket.{cinemaName}.{functionDate}.{movieName} , además de propagar los asientos a través del tópico '/topic/buyticket', llevará el control de los asientos recibidos (que podrán haber sido comprados por diferentes clientes) para esto debe utilizar la implementación de la clase 'CinemaPersistence' y garantizar el adecuado control de la persistencia, recuerde que se realizará concurrentemente, de manera que REVISE LAS POSIBLES CONDICIONES DE CARRERA!.

        Existe una condición de carrera de tipo read-write-modify en el momento de comprar el ticket, para solucionarla identificamos las regiones criticas y sincronizamos el acceso al recurso de las sillas de la función.
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764314872233066516/unknown.png)
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764315651421634570/unknown.png)
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764311782524977162/unknown.png)
    
    5. Verifique la funcionalidad 
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764317306112180254/unknown.png)
        
        ![](https://media.discordapp.net/attachments/352624122301513730/764317500707700746/unknown.png?width=878&height=475)

4. A partir de los diagramas dados en el archivo ASTAH incluido, haga un nuevo diagrama de actividades correspondiente a lo realizado hasta este punto, teniendo en cuenta el detalle de que ahora se tendrán tópicos dinámicos para manejar diferentes salas simultáneamente y que desde el servidor se centraliza la información de las asientos de las salas.

    ![](https://media.discordapp.net/attachments/352624122301513730/764318444396871710/Client.png?width=1026&height=446)

