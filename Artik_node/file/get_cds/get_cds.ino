int cnt = 0;
int sum = 0;
void setup() {

}
   

void loop() {

    int data = analogRead(A1);
    sum += data;
    cnt++;

    if(cnt == 10){


    fprintf(stdout, "%d", sum / 10);
      Serial.write("\n");
      exit(0);
    }

    delay(100);
}
