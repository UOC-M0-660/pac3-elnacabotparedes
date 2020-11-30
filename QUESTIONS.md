# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
- Porque la respuesta que devuelven las peticiones de internet se tienen que mostrar en nuestra pantalla y la unica forma es actualizando los componentes UI.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
- La aplicación no se actualizaría porque esa actividad con el recycleview la hubiera destruido.

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
- onStart() -> Prepara la app para que esta activity entre en primer plano
- onCreate() -> El sistema crea la actividad por primera vez.
- onResume() -> Este es el estado en que la app interactúa con el usuario.
- onPause() -> Avisa el sistema que el usuario esta abandonando la actividad y la quita del primer plano.
- onStop() -> Cuando el usuario ya no puede ver tu actividad.
- onDestroy() -> Se llama antes de que finalicé la actividad.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
- El uso de la paginación es principalmente para limitar la cantidad de resultados que nos envia una petición y así mantener el tráfico de la red bajo control.

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
- Un problema que podría presentar una aplicación sin paginación, es que el tiempo de carga de la aplicación sería excesivamente lento. Si la petición trae muchos datos.


##### Lista algunos ejemplos de aplicaciones que usan paginación.
- Un ejemplo de aplicación sería el Instagram. A la hora de cargar las fotografías en el muro inicial. 
Otro ejemplo podría ser la aplicación como Youtube, para cargar los videos.
