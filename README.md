# Documentazione Utente
---
# Indice

1. [Introduzione](#introduzione)
2. [Installazione](#installazione)
3. [Funzionalità Principali](#funzionalità-principali)
   - [Dashboard](#dashboard)
   - [Connessione](#connessione)
   - [INPUT](#input)
      - [Modalità Trazione](#modalità-trazione)
      - [Simulatore](#simulatore)
      - [Parking e Charging Mode](#parking-e-charging-mode)
      - [Profilo di Carica](#profilo-di-carica)
      - [Contattori](#contattori)
      - [Emergency Stop](#emergency-stop)
   - [OUTPUT](#output)
      - [Pacco Batterie](#pacco-batterie)
      - [Caricatore](#caricatore)
      - [Stato Motore](#stato-motore)
      - [Stato Contattori](#stato-contattori)
      - [Stato Emergency Stop](#stato-emergency-stop)
      - [Modalità di Trazione](#modalità-di-trazione)
      - [File Log](#file-log)
4. [Problemi Comuni](#problemi-comuni)
5. [Supporto e Contatti](#supporto-e-contatti)


---
## Introduzione

>Questa applicazione **Java** offre una **Dashboard** per la simulazione interattiva di tutte le componenti presenti nell'abitacolo di un veicolo elettrico.


## Installazione

1. Scarica l'ultima versione del progetto dalla repository di **github**.
2. Compila ed Esegui il progetto in **IntelliJ** o in qualsiasi altro **IDE** di tua scelta.

## Funzionalità Principali
### Dashboard
![Alt text](./readme/image-14.png)

>L'applicazione, come evidenziato chiaramente nell'immagine, offre all'utente una distinta separazione tra le componenti dedicate ai valori di input e quelle destinate ai valori di output. Queste ultime sono visibili nella sezione destra dell'applicazione, garantendo un'organizzazione intuitiva e un accesso agevole a tutte le informazioni pertinenti.

### Connessione
![Alt text](./readme/image.png)
>Il primo passo da compiere una volta avviata l'applicazione, consiste nel connettere il CAN bus, selezionandolo dalla lista generata dalla combo box in alto a sinistra e cliccare sul pulsante "Connect".


### INPUT
>Dopo aver stabilito la connessione con il CAN bus, si avrà la capacità di personalizzare i valori nella sezione di Input. Di seguito, elencheremo tutte le diverse opzioni disponibili:

#### Modalità Trazione
![Alt text](./readme/image-1.png)

>In questa sottosezione sarà possibile scegliere la modalità di trazione, attraverso uno SwitchButton, tra:

- Forward
- Reverse

>In particolare, per quanto riguarda la modalità Forward, sarà possibile scegliere tra due modalità diverse attraverso due RadioButton, ovvero:

- Sport
- Eco

#### Simulatore
![Alt text](./readme/image-15.png)

>Nella sottosezione Simulatore sarà possibile scegliere la modalità di set point, attraverso uno SwitchButton, tra:

- Coppia
- Velocità

> Inoltre, sempre in questa sezione si avrà la possibilità di inserire il set point desiderato, all'interno dei TextField.
Per una più intuitiva comprensione del valore inserito, sarà possibile visualizzarlo in una Gauge adiacente.


#### Parking e Charging Mode
![Alt text](./readme/image-17.png)

>In questa sottosezione sarà possibile scegliere tra la modalità Parking e la modalità Charging.
Nel caso in cui venga scelta la Charging mode, sarà possibile accedere ad un'ulteriore scelta tra modalità Grid e modalità Res.

Infine sarà possibile scegliere dei valori di soglia rispettivamente per la corrente e la tensione di Charging.

#### Profilo di Carica
![Alt text](./readme/image-2.png)

>In questa semplice sottosezione sarà possibile scegliere due diversi profili di carica attraverso uno SwitchButton:

- Corrente Costante
- Custom

#### Contattori
![Alt text](./readme/image-3.png)

>In questa semplice sottosezione sarà possibile scegliere di "caricare" o "scaricare" i due contattori.

#### Emergency Stop
![Alt text](./readme/image-4.png)

>In questa semplice sottosezione sarà possibile premere il pulsante di Emergency Stop, che bloccherà l'applicazione e manderà a zero tutti i valori di input.



### OUTPUT

#### Pacco Batterie
![Alt text](./readme/image-5.png)

>In questa sottosezione sarà possibile visualizzare i valori di Tensione, Corrente e State of Charge attraverso, rispettivamente due Gauge e una Batteria.

#### Caricatore
![Alt text](./readme/image-6.png)

>In questa sottosezione sarà possibile visualizzare i valori di Tensione e Corrente del charging tramite una Gauge e la modalità di charging tramite uno StatusButton.

#### Stato Motore
![Alt text](./readme/image-7.png)

>In questa sottosezione sarà possibile visualizzare i valori Velocità, Coppia e Temperatura del motore, attraverso tre diverse Gauge.

#### Stato Contattori
![Alt text](./readme/image-8.png)

>In questa semplice sottosezione sarà possibile visualizzare lo stato dei contattori.

#### Sato Emergency Stop
![Alt text](./readme/image-9.png)

>In questa semplice sottosezione sarà possibile visualizzare lo stato dell'Emergency Stop tramite uno StatusButton.

#### Modalità di Trazione
![Alt text](./readme/image-10.png)

>In questa semplice sottosezione sarà possibile visualizzare lo stato della modalità di trazione selezionata tramite uno StatusButton.


#### File Log
![Alt text](./readme/image-12.png)

>In questa sottosezione apparirà il percorso del file di log, che verrà creato all'avvio di ogni sessione.
## Problemi Comuni
>Il principale messaggio di errore che può presentarsi riguarda la mancata connessione con un valido CAN bus, generando il seguente pop-up:


![Alt text](./readme/image-13.png)

## Supporto e Contatti

>Per domande o problemi, contattaci a:
- [Achille Cannavale](mailto:achille.cannavale@studentmail.unicas.it)
- [Giada Pietrocola](mailto:giada.pietrocola@studentmail.unicas.it)
- [Paolo Simeone](mailto:paolo.simeone@studentmail.unicas.it)



