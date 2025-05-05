# Progetto Scuole Italiane

## Descrizione
L’obiettivo principale di questo progetto è la realizzazione di un’applicazione software server, in grado di ricavare e elaborare informazioni anagrafiche delle scuole italiane. Le informazioni sono contenute dentro ad un file csv che verrà letto ed inserito nel server.
Il progetto è stato migliorato con l’ausilio di diversi tipi di Client per garantire la diversità di accesso al server e ai dati, rendendolo versatile.

## Codes:
END - Close Client
STOP - Close Client and Server
GET-COMUNE - Take information from a respective Municipality
GET-PROVINCIA - Take information from a respective province
GET-REGIONE - Take information from a respective region
GET-CODICE - Take information from a respective code
GET-ISTITUTO - Take information from a respective istitute
GET-TIPOLOGIA-ISTITUTO - Take information from a respective type of istitute
GET-INDIRIZZO - Take information from a respective address
GET-CODICE-POSTALE - Take information from a respective postcode
GET-TELEFONO - Take information from a respective telephone number
GET-FAX - Take information from a respective FAX
GET-EMAIL - Take information from a respective email
GET-EMAIL-PEC - Take information from a respective PEC email
GET-SITE - Take information from a respective web site
GET-DIREZIONE - Take information from a respective main office
GET-STATALE - get all state schools
GET-PARITARIA - get all private schools

Writing the command, example -> GET-COMUNE: VENEZIA (exceptions -> END, STOP, GET-STATALE, GET-PARITARIA -> are written by themselves, example -> END)

## Installation
1. Open bash
2. Navigate to the folder where you want to clone the project
3. clone this repository:
   ```bash
   git clone https://github.com/Fankyostro17/ProgettoScuoleItaliane.git

4. Use Visual Studio Code or Intellij to open the progect
5. Start server
6. Start one of the Clients (or more)

## License
Distributed under the GNU license. See LICENSE for details.

## Contatti
Thomas Fanciullacci - thomas.fanciullacci@gmail.com
Progetto GitHub - https://github.com/Fankyostro17/ProgettoScuoleItaliane.git