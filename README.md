# Backend del Portafolio

API REST que alimenta mi portafolio personal. Me permite agregar, actualizar y eliminar proyectos desde una base de datos en lugar de editar archivos HTML a mano. Los visitantes pueden ver todo mi trabajo, pero solo yo puedo hacer cambios.

## Que problema resuelve

Un portafolio es algo vivo. Cada vez que termino un proyecto quiero mostrarlo en mi pagina web. Antes de que existiera este backend, tenia que abrir el codigo, escribir los detalles del proyecto manualmente y volver a desplegar todo el sitio. Eso no escala cuando construyes cosas seguido.

Esta API separa el contenido (guardado en una base de datos) de la presentacion (el frontend). El frontend simplemente le pide a la API "dame todos los proyectos" y los muestra. Yo puedo agregar nuevos proyectos a traves de endpoints protegidos sin tocar el codigo del frontend.

## Como funciona

Imagina que esto es la cocina de un restaurante. El frontend es el comedor donde los clientes se sientan y miran el menu. El backend es la cocina donde se prepara la comida.

- **Los clientes (visitantes)** pueden ver el menu (leer proyectos) pero no pueden entrar a la cocina.
- **El chef (yo)** tiene una llave (token JWT) que le permite entrar a la cocina para agregar, editar o quitar platillos (proyectos).
- Cada vez que alguien pide el menu, la cocina lo prepara fresco desde la base de datos.
- Si alguien toca la puerta de la cocina 11 veces en un minuto, la puerta se bloquea por un rato (limite de peticiones).
- Si alguien intenta pedir un platillo que no existe, la cocina responde claramente en lugar de colapsar.

## Tecnologias y por que elegi cada una

| Tecnologia | Que hace | Por que esta |
|---|---|---|
| Java 21 | Lenguaje de programacion | El tipado fuerte atrapa errores antes de que lleguen a produccion. Tiene un ecosistema enorme de librerias. Es el estandar de la industria para sistemas backend. |
| Spring Boot 3.4 | Framework | Se encarga de todo el cableado (HTTP, conexion a base de datos, seguridad) para que yo me concentre en la logica de negocio. Convencion sobre configuracion significa menos codigo. |
| PostgreSQL | Base de datos | Base de datos relacional que maneja bien los datos estructurados. Tiene capa gratuita en Render. Las transacciones ACID mantienen los datos consistentes. |
| Spring Security + JWT | Autenticacion | JWT (JSON Web Token) es un token autosuficiente. El servidor no necesita recordar quien inicio sesion. El token en si mismo prueba tu identidad. Al no tener estado, escala horizontalmente sin sesiones pegajosas. |
| Cloudinary | Alojamiento de imagenes | Guarda y optimiza las capturas de los proyectos. La capa gratuita cubre las necesidades de un portafolio. Maneja el redimensionamiento y la entrega via CDN automaticamente. |
| Bucket4j | Limite de peticiones | Implementa el algoritmo del cubo de fichas (token bucket). Cada direccion IP recibe 10 fichas por minuto. Si las gastas todas, esperas. Protege el servidor de abusos sin bloquear el trafico legitimo. |
| SpringDoc OpenAPI (Swagger) | Documentacion de la API | Genera documentacion interactiva automaticamente desde anotaciones en el codigo. Los desarrolladores del frontend pueden probar los endpoints directamente en el navegador en /swagger-ui.html. |
| Docker | Contenedores | Empaqueta la aplicacion con su entorno exacto. Funciona igual en mi laptop y en la nube. Se acabo el "en mi maquina si funciona". |
| Lombok | Generacion de codigo | Elimina el codigo repetitivo (getters, setters, builders, constructores) mediante anotaciones. Menos codigo que leer y mantener. |
| JPA / Hibernate | Acceso a base de datos | Mapea objetos Java directamente a tablas de la base de datos. Yo escribo clases Java, Hibernate escribe el SQL. Nada de armar consultas a mano para el CRUD basico. |
| Maven | Herramienta de construccion | Maneja las dependencias y compila el proyecto de forma consistente. El wrapper (mvnw) permite que cualquiera compile sin instalar Maven globalmente. |

## Patrones de arquitectura que use

### Arquitectura por capas

Cada carpeta tiene un unico trabajo y no sabe como funcionan las otras internamente.

```
controller/    -> Recibe peticiones HTTP, envia respuestas. Cero logica de negocio.
service/       -> Reglas de negocio y orquestacion. Habla con los repositorios.
repository/    -> Acceso a base de datos. Extiende JpaRepository para tener CRUD gratis.
model/         -> Tablas de la base de datos como objetos Java (entidades).
dto/           -> Data Transfer Objects. Lo que el cliente envia y recibe.
exception/     -> Excepciones personalizadas y el manejador global de errores.
security/      -> Creacion de JWT, validacion y filtrado de peticiones.
config/        -> Beans y configuracion del framework (CORS, Swagger, Cloudinary).
```

Esto significa que podria cambiar PostgreSQL por MySQL modificando solo la capa de repositorio. Los controladores y servicios ni se enterarian.

### Manejo global de excepciones

En lugar de envolver cada metodo del controlador en try-catch, todos los errores fluyen hacia un solo lugar: `GlobalExceptionHandler`. Atrapa las excepciones, las registra en el log y devuelve una respuesta JSON consistente.

Toda respuesta de error tiene la misma forma:

```json
{
  "timestamp": "2026-08-07T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Proyecto no encontrado con id: abc-123",
  "path": "/api/projects/abc-123"
}
```

Esto importa porque el frontend solo necesita una funcion para interpretar errores en lugar de adivinar que formato tiene cada error.

### Patron DTO

Las entidades de la base de datos (`Project`, `AdminUser`) nunca salen del backend directamente. En su lugar, viajan a traves de DTOs (`ProjectRequest`, `AuthResponse`, `ApiResponse`). Esto significa que:

- Puedo cambiar el esquema de la base de datos sin romper el contrato de la API.
- Puedo validar los datos que llegan antes de que toquen la base de datos.
- Nunca expongo accidentalmente campos como la contrasena hasheada.

### Autenticacion sin estado con JWT

1. El cliente envia email y contrasena a `/api/auth/login`.
2. El servidor valida las credenciales y devuelve un token JWT firmado.
3. El cliente guarda el token y lo envia en el encabezado `Authorization: Bearer <token>` en cada peticion protegida.
4. `JwtFilter` intercepta cada peticion, lee el encabezado, valida el token y establece el contexto de autenticacion.
5. Si el token falta o expiro, el filtro devuelve un 401 con un mensaje claro.

Sin sesiones. Sin cookies. Sin memoria del servidor gastada recordando quien inicio sesion.

### DRY en la capa de servicio

`ProjectService` tiene metodos privados auxiliares para la logica repetida:

- `normalizeTechStack(String)` toma un string separado por comas como `"React, Spring Boot, Docker"` y lo convierte en una lista limpia. Lo usan tanto crear como actualizar.
- `parseCategory(String)` convierte un string de categoria al enum `ProjectCategory`. Si la categoria no existe, lanza una excepcion especifica en lugar de un error generico.

Si el formato del tech stack cambia algun dia, lo arreglo en un solo lugar.

## Endpoints de la API

### Publicos (sin autenticacion)

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/health` | Verificacion de salud. Devuelve 200 si el servidor esta vivo. |
| GET | `/api/projects` | Devuelve todos los proyectos, ordenados del mas reciente al mas antiguo. |
| POST | `/api/auth/login` | Autentica al usuario y devuelve un token JWT. |

### Protegidos (requieren token JWT)

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/projects` | Crea un proyecto nuevo con una imagen. |
| PUT | `/api/projects/{id}` | Actualiza un proyecto existente. La imagen es opcional. |
| DELETE | `/api/projects/{id}` | Elimina un proyecto por su UUID. |

### Documentacion

Con el servidor corriendo, visita:

```
http://localhost:8080/swagger-ui/index.html
```

Ahi ves cada endpoint, el cuerpo esperado de la peticion, las posibles respuestas y puedes probarlos directamente desde el navegador.

## Como ejecutarlo localmente

### Lo que necesitas

- Java 21 (recomendado Eclipse Temurin)
- PostgreSQL corriendo en localhost:5432
- Una cuenta de Cloudinary (la capa gratuita funciona)
- Maven (o usa el wrapper `mvnw` incluido)

### Pasos

1. **Clona el repositorio**

```bash
git clone <url-de-tu-repo>
cd portafolioBackend
```

2. **Crea la base de datos**

```sql
CREATE DATABASE portafolio_db;
```

3. **Configura las variables de entorno**

La aplicacion lee estas variables del entorno. Si no estan definidas, usa los valores por defecto para localhost que estan en `application.properties`.

```
DB_URL=jdbc:postgresql://localhost:5432/portafolio_db
DB_USER=postgres
DB_PASSWORD=tu_contraseña
JWT_SECRET=tu_clave_secreta_de_256_bits
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

4. **Ejecuta la aplicacion**

```bash
./mvnw spring-boot:run
```

O compila y ejecuta el JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/*.jar
```

5. **Prueba que funcione**

Abre un navegador o Postman y ve a:

```
http://localhost:8080/api/health
```

Debes ver: `"Backend activo y funcionando"`

## Estrategia de manejo de errores

En lugar de devolver trazas de error o paginas 500 vacias, cada error llega como JSON estructurado. Esto es lo que recibe el cliente en cada situacion:

| Situacion | HTTP | Campo error | Mensaje de ejemplo |
|---|---|---|---|
| Proyecto no encontrado | 404 | Not Found | Proyecto no encontrado con id: abc-123 |
| Categoria invalida | 400 | Bad Request | Categoria no valida: MOVIL |
| Falta archivo de imagen | 400 | (mapa de campos) | Falta el archivo obligatorio: image |
| Error de validacion | 400 | (mapa de campos) | El nombre del proyecto es obligatorio |
| Credenciales incorrectas | 401 | Unauthorized | Credenciales incorrectas |
| Token invalido o expirado | 401 | Unauthorized | Token invalido o expirado |
| Limite de peticiones excedido | 429 | Too Many Requests | Has superado el limite de peticiones. Espera un minuto. |
| Fallo al subir archivo | 500 | Internal Server Error | Error al procesar el archivo. |
| Error inesperado | 500 | Internal Server Error | Ha ocurrido un error inesperado en el servidor. |

Los errores de validacion (de `@Valid`) devuelven un mapa de campos con sus mensajes para que el frontend pueda marcar cual campo necesita correccion.

Los errores del servidor se registran internamente con la traza completa. El cliente solo ve un mensaje amigable porque exponer detalles internos es un riesgo de seguridad.

## Medidas de seguridad

- **Contrasenas hasheadas**: Las contrasenas se guardan con BCrypt. Aunque la base de datos se filtre, las contrasenas no se pueden leer.
- **Expiracion de JWT**: Los tokens expiran despues de 24 horas. Luego de eso, hay que iniciar sesion de nuevo.
- **Limite de peticiones**: Cada direccion IP recibe 10 peticiones por minuto. Si lo excede, recibe un 429 por el resto del minuto.
- **CORS**: Solo los origenes configurados pueden llamar a la API. Se configura mediante la variable de entorno `ALLOWED_ORIGIN`.
- **Mensajes de error genericos**: Los intentos de inicio de sesion fallidos dicen "Credenciales incorrectas" sin importar si el email existe o no. Esto evita que atacantes descubran correos validos.
- **Control de DDL**: En produccion, la variable `DB_DDL` se configura como `validate` o `none` para que Hibernate nunca modifique el esquema de la base de datos por accidente.

## Despliegue

El proyecto incluye un `Dockerfile` con construccion en dos etapas:

1. **Etapa de construccion**: Compila la aplicacion usando una imagen de Maven.
2. **Etapa de ejecucion**: Copia solo el JAR en una imagen ligera de Java Alpine.

Esto mantiene la imagen final pequena y rapida de desplegar.

La aplicacion esta disenada para correr en cualquier plataforma que soporte Docker o Java, incluyendo Render, Railway, Fly.io o un VPS. Las variables de entorno controlan las URLs de la base de datos, los secretos y los origenes CORS para que el mismo JAR funcione en todos lados.

## Que agregaria despues

- Pruebas de integracion para la capa de servicio usando una base de datos H2 en memoria.
- Despliegue automatizado con GitHub Actions (compilar, probar, subir a Docker Hub, disparar despliegue).
- Rotacion de token de refresco para que los usuarios sigan conectados mas tiempo sin volver a meter credenciales.
- Paginacion en el endpoint de proyectos por si el portafolio crece a decenas de entradas.
- Una tarea programada para limpiar los cubos de rate limiting expirados y que la memoria no crezca para siempre.
