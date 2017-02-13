#include "DHT.h"
#define DHTPIN 2        // SDA 핀의 설정
#define DHTTYPE DHT22   // DHT22 (AM2302) 센서종류 설정
 
DHT dht(DHTPIN, DHTTYPE);
int cnt = 0;
float sum = 0;
 
void setup() {
  dht.begin();
}
 
void loop() {
  // 센서의 온도와 습도를 읽어온다.
  float h = dht.readHumidity();
  float t = dht.readTemperature();
 
  if (isnan(t) || isnan(h)) {
    //값 읽기 실패시 시리얼 모니터 출력
  } else {
    //온도, 습도 표시 시리얼 모니터 출력
    sum += t;
    cnt++;

    if(cnt == 10){

    fprintf(stdout, "%.1f", sum / 10);
      
        exit(0);
    }
  }
  
  delay(100);
}

