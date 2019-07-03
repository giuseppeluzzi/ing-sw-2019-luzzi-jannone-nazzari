# Adrenalina

## Parametri da linea di comando

Il file `Adrenalina.jar`, se eseguito senza ulteriori parametri, avvia l'applicazione in modalità client **con interfaccia grafica** (GUI).


Per avviare l'applicazione in **modalità server**, passare `--server` come parametro da linea di comando.

Per avviare l'applicazione in modalità client **con interfaccia testuale** (TUI), passare `--tui` come parametro da linea di comando. I seguenti ulteriori parametri sono opzionali:

- Indirizzo IP del server
- Porta del server
- Nome del giocatore
- Modalità di connessione (`0` = RMI, `1` = socket)
- Modalità dominazione (`0` = disattivata, `1` = attivata)

Esempio:

```java -jar Adrenalina.jar --tui 127.0.0.1 2345 NomeGiocatore 1 0```

Il comando sopra avvia il client con interfaccia testuale, imposta l'indirizzo IP del server a `127.0.0.1`, la porta a `2345`, il nome del giocatore a `NomeGiocatore`, la connessione tramite socket e la modalità dominazione disattivata.

Se non sono presenti **tutti i parametri opzionali**, l'applicazione ne richiederà l'inserimento all'avvio.

## Configurazione

I **parametri del server** di Adrenalina possono essere configurati mediante un file `server_config.json` posizionato nella working directory. Se il file non esiste, al momento dell'avvio del server ne viene creato uno con la **configurazione di default**.

Analogamente i **parametri del client** possono essere configurati mediante un file `client_config.json`.

## Interfaccia testuale

![Interfaccia testuale](https://i.imgur.com/MMN85PU.png)

Nella parte alta dell'interfaccia vengono mostrati i **tracciati** dei punti di generazione con i relativi danni (solo se si gioca in modalità dominazione) e il tracciato mortale con teschi e colpi mortali.

Al centro viene stampata una rappresentazione testuale della **mappa** con coordinate per identificare ogni quadrato. In ogni quadrato è presente una **tessera munizioni** o un **punto di generazione**. Le tessere munizioni sono rappresentate con tre lettere, ognuna rappresentante il colore di una munizione o un potenziamento (`R` = rosso, `B` = blu, `Y` = giallo, `P` = potenziamento).

Sulla destra sono rappresentate le **plance dei giocatori**. Ogni plancia mostra, dall'altro verso il basso e da sinistra verso destra: il nome del giocatore, il punteggio (tra parentesi), i simboli delle armi, i marchi, i simboli dei potenziamenti, i danni, il punteggio attribuito all'uccisione, il numero di munizioni di ogni colore.

La plancia del giocatore che sta giocando il turno attuale viene stampata in **colori brillanti**, mentre quelle dei giocatori in attesa del loro turno vengono stampate con colori spenti.

In basso vengono stampate le **armi** presenti in ogni punto di generazione, con i relativi simboli tra parentesi.

## Legenda dei simboli

Le seguenti tabelle illustrano il significato dei simboli usati, per compattezza, nell'interfaccia testuale.

### Armi

| Simbolo |Descrizione|
|:---------:|-------------|
|    `A`    |*Spada fotonica*|
|    `B`    |*Lanciarazzi*|
|    `C`    |*Cannone vortex*|
|    `D`    |*Distruttore*|
|    `E`    |*Fucile a pompa*|
|    `F`    |*Fucile di precisione*|
|    `G`    |*Lanciagranate*|
|    `I`    |*Fucile laser*|
|    `K`    |*Falce protonica*|
|    `L`    |*Lanciafiamme*|
|    `M`    |*Mitragliatrice*|
|    `N`    |*Martello ionico*|
|    `O`    |*Onda d'urto*|
|    `P`    |*Torpedine*|
|    `R`    |*Razzo termico*|
|    `S`    |*Raggio solare*|
|    `T`    |*Raggio traente*|
|    `U`    |*Fucile al plasma*|
|    `V`    |*Vulcanizzatore*|
|    `Y`    |*Cyberguanto*|
|    `Z`    |*Zx-2*|
|    `*`    |Arma generica (usato quando l'arma non è visibile perché carica e di proprietà di un altro giocatore)|

Per le armi una lettera maiuscola rappresenta un'**arma caria**, mentre una lettera minuscola rappresenta un'arma scarica.

### Potenziamenti

| Simbolo |Descrizione|
|:---------:|-------------|
|    `H`    |*Teletrasporto*|
|    `J`    |*Granata a frammentazione*|
|    `Q`    |*Mirino*|
|    `W`    |*Raggio traente*|
|    `+`    |Potenziamento generico (usato quando il potenziamento non è visibile perché di proprietà di un altro giocatore)|

### Altri simboli

| Simbolo |Descrizione|
|:---------:|-------------|
|    `⚑`    |Giocatore|
|    `⊡`    |Punto di generazione|
|    `✦`    |Marchio|
|    `✚`    |Danno o colpo mortale|
|    `✖`    |Colpo mortale con infierimento|
|    `△`    |Teschio|
