# Adrenalina

## Parametri da linea di comando

Il file `Adrenalina.jar`, se eseguito senza ulteriori parametri, avvia l'applicazione in modalità client con interfaccia grafica (GUI).


Per avviare l'applicazione in modalità server, passare `--server` come parametro da linea di comando.

Per avviare l'applicazione in modalità client con interfaccia testuale (TUI), passare `--tui` come parametro da linea di comando. I seguenti ulteriori parametri sono opzionali:

- Indirizzo IP del server
- Porta del server
- Nome del giocatore
- Modalità di connessione (`0` = RMI, `1` = socket)
- Modalità dominazione (`0` = disattivata, `1` = attivata)

Esempio:

```java -jar Adrenalina.jar --tui 127.0.0.1 2345 NomeGiocatore 1 0```

Il comando sopra avvia il client con interfaccia testuale, imposta l'indirizzo IP del server a `127.0.0.1`, la porta a `2345`, il nome del giocatore a `NomeGiocatore`, la connessione tramite socket e la modalità dominazione disattivata.

Se non sono presenti tutti i parametri opzionali, l'applicazione ne richiederà l'inserimento all'avvio.

## Configurazione

I parametri del server di Adrenalina possono essere configurati mediante un file `server_config.json` posizionato nella working directory. Se il file non esiste, al momento dell'avvio del server ne viene creato uno con la configurazione di default.

Analogamente i parametri del client possono essere configurati mediante un file `client_config.json`.