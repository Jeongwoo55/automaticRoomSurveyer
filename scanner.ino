#include <Servo.h>

Servo servo;

int trig = 12;
int echo = 13;
int duration;
double cm;
int angle = 0;
int start;
double rad;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
  servo.attach(9);
  servo.write(0);
  start = millis();

  check180();
  check180();
  
}

void loop() {

  
}

double check() {
  digitalWrite(trig, LOW);
  delay(2);
  digitalWrite(trig, HIGH);
  delay(10);
  digitalWrite(trig, LOW);
  pinMode(echo, INPUT);
  duration = pulseIn(echo, HIGH);

  cm = (duration/2) * 0.0343;
  return cm;
}

void check180() {
  pulseIn(2, HIGH, 3000000);//3 seconds
  delay(1000);//1 second, increase to 10 before presentation
  
  angle = 0;
  servo.write(angle);
  delay(100);
  
  while(angle < 180) {  
    if (millis() >= start + 20) {//increase the rightmost constant to slow down and get more points
      angle++;
      servo.write(angle);
      start = millis();
    }
    
    Serial.print(check());
    Serial.print(' ');
  
  }
}
