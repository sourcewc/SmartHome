void setup() {
  // put your setup code here, to run once:
  pinMode(6, OUTPUT);
  digitalWrite(6, HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(6, HIGH);
  Serial.write("kkk");
  delay(10000);
  digitalWrite(6, LOW);
  Serial.write("oooo");
  delay(10000);
}
