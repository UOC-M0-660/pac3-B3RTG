# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Es importante vincular estas tareas al los componentes UI de la app ya que sino, podríamos dejar rutinas lanzadas en threads externos y no finalizar aunque la aplicación estubiera cerrada o parada.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?


##### Describe brevemente los principales estados del ciclo de vida de una Activity.
Escribe aquí tu respuesta

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.
Twitch nos retorna en cada una de las peticiones que enviamos un ìndice de la página actual en la que hemos consultado datos. Este indice en la respues da de datos se llama **cursor**
A partir de el podemos realizar las peticiones para pedir datos a partir de ese valor.
Tendremos que pasar el valor cursor como parametro en la petición y ademas decir el número de registros que queremos. Ademas de esto, tenemos la posibilidad de pedir la información con varias opciones:
- **After**: Si ponemos el cursor en after, nos dará la información a parti de ese punto en adelante
- **before**: Si ponemos el cursor en before, nos dará la información de las *paginas* anteriores

##### ¿Qué ventajas ofrece la paginación a la aplicación?
La paginación nos permite mostrar la información de manera mas ordenada y poder pedir poco a poco aquellos datos que necesita el usuario y no mas de los necesarios.

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
Si no pudieramos pagínar y nos devolviera por defecto toda la información que tienen, podriamos saturar los recursos de nuestro dispositivo. Esto afectaria al rendimiento tanto de nuestra aplicación como del resto de funciones del mismo.


##### Lista algunos ejemplos de aplicaciones que usan paginación.
Algunos ejemplos serian:
- Cliente correo electronico (Gmail): Nos muestra los correos pero no nos muestra todos a la vez, sino que va cargandolos bajo demanda.
- App bancaria (CaixaBank): En la carga de movimientos no muestra todos de golpe, sino que va cargando bajo demanda al arrastrar hacia abajo.
- Twitter: La app para twitter muestra unos cuantos por orden cronológico y va haciendo mas peticiones conforme vamos bajando y no quedan registros a mostrar
