# PARTE TEÓRICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.
##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Es importante vincular estas tareas al los componentes UI de la app ya que sino, podríamos dejar rutinas lanzadas en threads externos y no finalizar aunque la aplicación estuviera cerrada o parada.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
Realmente no me a pasado, pero entiendo que nos encontraríamos que se esta intentando actualizar una reciclerview que ya no existe, obtendremos una excepción o error de aplicación. No se si esto conllevaría algún problema para el SO.


##### Describe brevemente los principales estados del ciclo de vida de una Activity.
Una activity puede estar en varios estados mientras se esta ejecutando. Podemos resumirlo básicamente en tres:
1. Resumida(**running**): La actividad está en primer plano y tiene el foco del usuario
2. Pausada(**paussed**): Hay otra actividad en primer plano que está *runnig*, pero la pausada aun es visible. Esto puede suceder cuando la actividad en primer plano no cubre toda la pantalla o es parcialmente trasparente. La actividad mantiene toda la información en memoria y esta completamente viva, aunque puede llegar a ser destruida en caso extremo de necesidad de memoria.
3. Terminada(**finished**) La actividad esta totalmente ocultada por otra actividad, y está en segundo plano. La actividad esta totalmente viva pero no es visible y puede ser destruida cuando el sistema necesite más memoria.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.
Twitch nos retorna en cada una de las peticiones que enviamos un ìndice de la página actual en la que hemos consultado datos. Este indice en la respuesta da de datos se llama **cursor**
A partir de el podemos realizar las peticiones para pedir datos a partir de ese valor.

Tendremos que pasar el valor cursor como parámetro en la petición y ademas decir el número de registros que queremos. Ademas de esto, tenemos la posibilidad de pedir la información con varias opciones:
- **After**: Si ponemos el cursor en after, nos dará la información a partir de ese punto en adelante
- **before**: Si ponemos el cursor en before, nos dará la información de las *paginas* anteriores

##### ¿Qué ventajas ofrece la paginación a la aplicación?
La paginación nos permite mostrar la información de manera mas ordenada y poder pedir poco a poco aquellos datos que necesita el usuario y no mas de los necesarios.

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
Si no pudiéramos pagínar y nos devolviera por defecto toda la información que tienen, podríamos saturar los recursos de nuestro dispositivo. Esto afectaría al rendimiento tanto de nuestra aplicación como del resto de funciones del mismo.


##### Lista algunos ejemplos de aplicaciones que usan paginación.
Algunos ejemplos serian:
- Cliente correo electrónico (Gmail): Nos muestra los correos pero no nos muestra todos a la vez, sino que va cargándolos bajo demanda.
- App bancaria (CaixaBank): En la carga de movimientos no muestra todos de golpe, sino que va cargando bajo demanda al arrastrar hacia abajo.
- Twitter: La app para twitter muestra unos cuantos por orden cronológico y va haciendo mas peticiones conforme vamos bajando y no quedan registros a mostrar
