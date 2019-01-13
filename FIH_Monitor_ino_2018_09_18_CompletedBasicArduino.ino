

// this constant won't change:
const int monoColorPin = 2;    // the pin that gets the MonoColor State
const int copyPulsePin = 4;    // the pin that gets the copy Pulse
const int enableSignalPin = 7;          // the pin that controls if copies are allowed or not.
const int monoColorStatePin = 13;      // the pin that the LED is attached to

// Variables will change:
int copyPageCounter = 0;            // counter for the number of pages remaining in the account
int monoColorCurrentState = 0;      // current state of the MonoColor line
int monoColorLastState = 0;         // previous state of the MonoColor line
int copyPulseCurrentState = 0;      // current state of the CopyPulse line
int copyPulseLastState = 0;         // previous state of the CopyPulse line


String inputString = "";         // a string to hold incoming data
String parseInputString = "";
boolean stringComplete = false;  // whether the string is complete

boolean creditTest = false;


void setup() {
  // initialize the MonoColor pin as an input:
  pinMode(monoColorPin, INPUT);
  // initialize the copyPulse pin as an input:
  pinMode(copyPulsePin, INPUT);
  // initialize the enableSignal as an output:
  pinMode(enableSignalPin, OUTPUT);
  // initialize the LED as an output to monitor for the color/mono state:
  pinMode(monoColorStatePin, OUTPUT);
  // reserve 10 bytes for the inputString:
  inputString.reserve(10);
  // initialize serial communication:
  Serial.begin(38400);
}


void loop() {
  // read the MonoColor input pin:
  monoColorCurrentState = digitalRead(monoColorPin);
  copyPulseCurrentState = digitalRead(copyPulsePin);

  if (stringComplete) {
    if (inputString == "on")
    {
      digitalWrite(enableSignalPin, HIGH);
      Serial.println("EN");
      creditTest = false;
    }
    else if (inputString == "off")
    {
      digitalWrite(enableSignalPin, LOW);
      Serial.println("DIS");
      creditTest = false;
    }    
    else 
    {
      parseInputString = inputString.substring(0,4);
      if (parseInputString == "cred"){
        creditTest = true;
        Serial.print("CR: ");
        copyPageCounter = inputString.substring(5).toInt();
        Serial.println(copyPageCounter);
        
        if(0<copyPageCounter){
          digitalWrite(enableSignalPin, HIGH);
        } else{
          digitalWrite(enableSignalPin, LOW);
        }
        
      }
    }
    // clear the string:
    inputString = "";
    stringComplete = false;
  }

  // compare the monoColorState to its previous state
  if (monoColorCurrentState != monoColorLastState) {
    // if the state has changed, increment the counter
    if (monoColorCurrentState == HIGH) {
      // if the current state is HIGH then the printer transition to COLOR
      digitalWrite(monoColorStatePin, HIGH);
      Serial.println("CON");
    } 
    else {
      // if the current state is LOW then the printer transition to MONO
      digitalWrite(monoColorStatePin, LOW);
      Serial.println("MON");
    }
    // store the current state as the last state to detect the next transition
    //for next time through the loop
    monoColorLastState = monoColorCurrentState;
  }

  // compare the CopyPulseState to its previous state
  if (copyPulseCurrentState != copyPulseLastState) {
    // The CopyPulseState has changed
    if (copyPulseCurrentState == HIGH) {
      // if the current state is HIGH then the printer has printed a page
      // decrement the page limit
      
     // copyPageCounter--;
      // increment the page counter
      if (monoColorCurrentState == HIGH) {
        Serial.println("CC");
        if (creditTest==true){
          copyPageCounter--;
        }
      }
      else {    
        Serial.println("MC");
        if (creditTest==true){
          copyPageCounter--;
        }
      }

      if (creditTest==true){
      Serial.print("RCC: ");
      Serial.println(copyPageCounter);
      if(0<copyPageCounter){
        digitalWrite(enableSignalPin,HIGH);
      } else{
        digitalWrite(enableSignalPin,LOW);
      }
      }
    } 
    // if the transition is from High to Low no action needed
    // store the current state as the last state to detect the next transition
    //for next time through the loop
    copyPulseLastState = copyPulseCurrentState;
  }
}

/*
  SerialEvent occurs whenever a new data comes in the
 hardware serial RX.  This routine is run between each
 time loop() runs, so using delay inside loop can delay
 response.  Multiple bytes of data may be available.
 */
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read(); 
    // add it to the inputString:
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    }
    else if (inChar == '\r'){
      
    }
    else{
      inputString += inChar;
    }
  }
}



