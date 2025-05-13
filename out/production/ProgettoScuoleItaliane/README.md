# Progetto Scuole Italiane

## Descrizione
L’obiettivo principale di questo progetto è la realizzazione di un’applicazione software server, in grado di ricavare e elaborare informazioni anagrafiche delle scuole italiane. Le informazioni sono contenute dentro ad un file csv che verrà letto ed inserito nel server.
Il progetto è stato migliorato con l’ausilio di diversi tipi di Client per garantire la diversità di accesso al server e ai dati, rendendolo versatile.

## Codes:
- END - Chiude il client
- STOP - Chiude il client e il server corrente
- GET-COMUNE - Prende le informazioni di un rispettivo comune
- GET-PROVINCIA - Prende le informazioni di una rispettiva provincia
- GET-REGIONE - Prende le informazioni di una rispettiva regione
- GET-CODICE - Prende le informazioni di un rispettivo codice
- GET-ISTITUTO - Prende le informazioni di un rispettivo istituto
- GET-TIPOLOGIA-ISTITUTO - Prende le informazioni di una rispettiva tipologia di istituti
- GET-INDIRIZZO - Prende le informazioni di un rispettivo indirizzo
- GET-CODICE-POSTALE - Prende le informazioni di un rispettivo codice postale
- GET-TELEFONO - Prende le informazioni di un rispettivo numero di telefono
- GET-FAX - Prende le informazioni di un rispettivo FAX
- GET-EMAIL - Prende le informazioni di una rispettiva email
- GET-EMAIL-PEC - Prende le informazioni di un rispettivo email PEC
- GET-SITE - Prende le informazioni di un rispettivo sito web
- GET-DIREZIONE - Prende le informazioni di una rispettiva direzione
- GET-STATALE - Prende tutte le scuole statali
- GET-PARITARIA - Prende tutte le scuole paritarie

Scrittura del comando, esempio -> GET-COMUNE: VENEZIA (eccezioni -> END, STOP, GET-STATALE, GET-PARITARIA -> sono scritti da soli, esempio -> END).

## Scelte Progettuali
1. I dati saranno inseriti all’interno di un TreeMap (che chiameremo ‘archive’), in modo da garantire la velocità, l’univocità dei dati attraverso i codici delle scuole come chiavi e i dati saranno inseriti all’interno di una LinkedList. I titoli saranno inseriti in una LinkedList (che chiameremo ‘titles’) per contenere tutti i titoli presenti; questa lista è efficace se ci sono inserimenti, rimozioni e ricerche.

2. Lettura del file ‘Anagrafe-delle-scuole-italiane.csv’, la prima riga verrà inserita nei titles (ciclo for per Lista), mentre dalla seconda verranno inseriti nell’archive (ciclo while per Map). I dati verranno modificati per facilitare la ricerca, per esempio gli spazzi saranno massimo di 1.

3. La ricerca dei dati sarà effettuata tramite il metodo contains(), in modo da ricercare qualcosa anche se non si inserisce l’intera parola.

4. Ho voluto realizzare più client diversi in modo tale da emulare più connessioni con più dispositivi diversi.

## Installazione
1. Apri la bash
2. Seleziona la cartella dove vuoi salvare il progetto
3. Clona questa repository:
   git clone https://github.com/Fankyostro17/ProgettoScuoleItaliane.git

4. Usa Visual Studio Codce o IntelliJ per aprire il progetto
5. Avvia il server
6. Avvia uno dei client (o più di uno)

## Licenza
Distribuita sotto licenza GNU. Guarda LICENSE per più dettagli.

## Contatti
Thomas Fanciullacci - thomas.fanciullacci@gmail.com
Progetto GitHub - https://github.com/Fankyostro17/ProgettoScuoleItaliane.git