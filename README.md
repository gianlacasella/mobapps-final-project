# MobApps_project

Proyecto Aplicaciones Móbiles 2021, grupo 7

Ignacio F. Garcés – Gianfranco Lacasella

Universidad de los Andes.


## Notas entrega 3

Las siguiente funcionalidades no alcanzaron a ser implementadas:

- Distinguir a nivel de UI entre sala unida y salas a las que no se está unido.
- Ver los votos de otros (puntajes de las cartas) en la sala unida.
- Publicar los votos propios a las cartas de la sala unida.
- Llamada "Get Rooms": no podemos hacer un [`PokerApiHandler.getAllRoomsCall`](./app/src/main/java/com/ifgarces/courseproject/networking/PokerApiHandler.kt) al comienzo para cargar las `PokerRoom`s, porque la contraseña es algo que se conoce solo al crearla, no se puede obtener por medio de la API, solo a través de la base de datos local. Entonces no podría unirme ni hacer coincidir con el modelo local (`PokerRoom`), porque requiere `password`.

Todo el resto, incluyendo sincronización correcta entre las llamadas de API y la DB local, están implementadas.

Nota: ara entender cómo se crean las PokerRooms: [`RoomCreateFragment`](./app/src/main/java/com/ifgarces/courseproject/fragments/RoomCreateFragment.kt) línea 68 y 76.
