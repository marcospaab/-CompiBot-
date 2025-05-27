# CompiBot 🤖
<img align="right" width=300px alt="Unicorn" src="https://media.tenor.com/xzKtvBspe5YAAAAi/emo-robot-happy-discord.gif" />


## 🤖 ¿Qué es CompiBot?

**CompiBot** es un asistente conversacional basado en inteligencia artificial que funciona a través de Telegram.  
Este proyecto es un **ensayo** creado por **Marcos Padín** para demostrar cómo integrar un modelo de IA local con un bot de Telegram y ofrecer respuestas inteligentes y personalizadas.

---

## ⚙️ ¿Cómo funciona?  

    1. El usuario envía un mensaje a CompiBot en Telegram.
    2. CompiBot envía el mensaje a un modelo de IA local (como Ollama).
    3. El modelo genera una respuesta basada en el mensaje recibido.
    4. CompiBot devuelve esa respuesta al usuario en Telegram.
    5. Si el usuario envía un sticker, CompiBot lo almacena y puede enviarlo posteriormente.
    6. El usuario puede solicitar un sticker aleatorio con el comando /sticker.

<img align="right" width=300px alt="Unicorn" src="https://github.com/user-attachments/assets/b96a6e8b-81d8-46c8-abaf-36b67d39520a" />


## 🚀 Cómo replicar CompiBot

---

## ¿Qué necesitas para usar CompiBot?

- Java 17+ instalado en tu máquina.  
- Ollama instalado y corriendo en tu ordenador, con el modelo `mistral` disponible.  
- Una cuenta y token de bot de Telegram (lo puedes crear con [BotFather](https://t.me/BotFather)).

---

## 📜 Pasos para probar 🤖CompiBot🤖

1. Clona o descarga este repositorio:

    ```bash
    git clone https://github.com/tu_usuario/compibot.git
    cd compibot
    ```

2. Configura Ollama para que esté corriendo en `http://localhost:11434/api/generate`.

3. Compila el bot con Gradle:

    ```bash
    ./gradlew build
    ```

4. Ejecuta el bot pasando el nombre y token de Telegram como argumentos:

    ```bash
    java -jar build/libs/compibot-1.0-SNAPSHOT.jar CompiBot TU_TOKEN_DE_TELEGRAM
    ```

5. Busca el bot en Telegram por el nombre que configuraste y comienza a enviar mensajes.

6. Prueba la funcionalidad de stickers enviando uno a CompiBot y luego usando /sticker para recibir un sticker aleatorio.

---

## 🎨 Personalización
<img align="right" width=300px alt="Unicorn" src="https://github.com/user-attachments/assets/8e4141a9-b97e-4e7d-add0-3339530bd744" />
Puedes cambiar el nombre del bot en el código para que se presente siempre como "CompiBot", o modificar el prompt que envías a Ollama para ajustar su personalidad o estilo de respuesta.

---

## ⚠️ Problemas comunes
- **Timeout o error de conexión:** Verifica que Ollama esté corriendo y accesible en `localhost:11434`.  
- **Token inválido:** Asegúrate de usar el token correcto proporcionado por BotFather.  
- **Java no instalado:** Instala Java 17 o superior.
- **No se envían stickers:** Puede que no haya stickers almacenados aún, intenta enviándole uno primero.

---
## 📝 Licencia
Este proyecto es de código abierto bajo licencia de libre uso.

---

¡Disfruta hablando con CompiBot!

Si tienes dudas o quieres colaborar, abre un issue o contacta conmigo.
