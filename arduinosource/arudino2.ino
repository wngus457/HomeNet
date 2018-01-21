#define PI 3.141592                                                    // 피에조 부저로 사이렌 소리를 나타내기 위한 변수
#define piezoPin 9                                                     // 피에조 부저를 디지털포트 9번으로 설정
#define led1 6
#define led2 5
#define led3 3
#include <Wire.h>                                                      // <-- 모듈
#include <LiquidCrystal_I2C.h>                                       // 소스를 사용하기 위한 아두이노 모듈
#include <DHT11.h>                                                  // >--
#include <SoftwareSerial.h>

int threshold  = 102;                                                   // 센서의 민감도 조절 (각자 환경에 맞게 조절하면 된다)
int led = 2;                                                             // 인체감지센서와 연동되는 LED포트를 2번으로 설정
int volume;                                                               // 빗물 센서의 값을 넣을 변수 (센서의 값을 확인할 때 사용)
float sinVal;                                                             // 피에조 부저로 사이렌 소리를 나타내기 위한 변수
int GasPin = A1;                                                          // 가스센서를 아날로그포트 1번으로 설정
int GasValue = 0;                                                         // 가스센서의 값을 넣을 변수
int toneVal;                                                              // 피에조 부저의 소리값을 넣을 변수
int analogPin = 0;                                                        // 빗물센서를 아날로그 포트 0번으로 설정
int val = 0;                                                              // 빗물센서의 전류값을 넣을 변수
int inputPin = 8;                                                         // 인체감지를 디지털포트 8번으로 설정
int pirState = LOW;                                                       // PIR 센서의 초기상태 선언(인체감지 센서)
int valsignal = 0;                                                        // 인체감지 센서의 값을 넣을 변수
float discomfortIndex(float temp, float humi) {                           // <--
return (1.8f*temp)-(0.55*(1-humi/100.0f)*(1.8f*temp-26))+32;              // 불쾌지수를 계산하는 식(기상청 제공)
}                                                                         // -->

LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);          // LCD 설정
 
int pin=7;                                                                // 온습도 센서를 디지털 7번포트로 설정
DHT11 dht11(pin);                                                        // 온습도 센서(DHT11)를 사용하기 위한 식
 
void setup()                                                             // <-- 셋업
{
  lcd.init();
  Serial.begin(9600);
  lcd.begin(16,2);                                                       // LCD를 16x2로 설정
  pinMode(piezoPin, OUTPUT);                                             // piezoPin(피에조부저)를 출력으로 설정
  pinMode(inputPin, INPUT);                                              // inputPin(인체감지센서)를 입력으로 설정
  pinMode(led, OUTPUT);                                                  // 인체감지센서와 연동된 LED를 출력으로 설정
  pinMode(4, OUTPUT);                                                    // 디지털 4번포트(빗물센서)를 출력으로 설정
}                                                                       // -->
 
void loop()
{
      
  Serial.println(analogRead(GasPin));                                   // 가스센서의 값을 읽어들여 시리얼 포트에 출력
  int err;                                                               // 온습도 센서의 오류를 넣을 변수
  float temp, humi;                                                       // 온습도 센서가 측정한 온도와 습도를 넣을 변수들
  val = analogRead(analogPin);                                            // analogPin(빗물센서) 의 변화값(전류값)을 읽음
  valsignal = digitalRead(inputPin);                                      // 인체감지 센서값 읽기
  volume = analogRead(A0);                                                // 아날로그 포트 0번(빗물센서) 에서 입력값을 읽음
  GasValue = analogRead(GasPin);                                          // 가스센서의 값을 GasValue 변수에 넣음
  Serial.println(GasValue);                                               // 위에서 넣은 가스센서의 값을 시리얼 포트에 출력
  
  if (valsignal == HIGH) {                                                  // <-- 인체감지시 pirState(PIR센서의 초기값)을 바꾸고 연동된 LED를 켬
        if (pirState == LOW) {
        Serial.println("Motion Detected");
        pirState = HIGH;
        }
    digitalWrite(led, HIGH);
  }                                                                         // -->
  else {                                                                    // <-- 인체감지가 안됐을 경우, 혹은 인체감지가 됐다가 없어졌을 경우 pirState(PIR센서의 초기값)을 바꾸고 연동된 LED를 끔
    digitalWrite(led, LOW);
        if(pirState == HIGH) {
      Serial.println("Motion Ended!");
      pirState=LOW;
    }
  }                                                                         // -->
    if((err=dht11.read(humi, temp))==0)                                     // <-- DHT11센서(온습도센서)가 에러가 안났을 경우 연동된 LCD에 온도,습도,불쾌지수,날씨, 가스누출여부를 표시
  {
    lcd.backlight();                                      // <-- LCD에 온도,습도,불쾌지수를 표시
    lcd.display();
    lcd.print("T: ");
    lcd.print((int)temp);
    lcd.print("C");
    lcd.setCursor(8,0);
    lcd.print("H: ");
    lcd.print((int)humi);
    lcd.print("%");
    lcd.setCursor(0,1);
    lcd.print("DI: ");
    lcd.print((int)discomfortIndex(temp, humi));                            // -->
    
    Serial.print(val);                                                      // 빗물센서의 값을 시리얼 포트에 출력
    if( val < 800 )                                                         // <-- 빗물센서의 값이 800미만이되면 (비가 오면) LCD에 "RAINY DAY" 출력
    {
      lcd.setCursor(7,1);
      lcd.print("RAINY DAY");
    }                                                                       // -->
    else if ( val > 800)                                                    // <-- 빗물센서의 값이 800이 넘어가면 (비가 안오면) LCD에 "SUNNY DAY" 출력
    {
      lcd.setCursor(7,1);
      lcd.print("SUNNY DAY");
    }                                                                       // -->
    if( GasValue > 200 ) {                                                  // <-- 가스센서의 값이 200이 넘어가면 (가스누출이 되면) LCD에 "Gas Leakage!"라는 메세지가 깜빡거리고, 연동된 피에조 부저에서 사이렌 소리가 남
      for(int i =0; i<180; i++) {
      sinVal=sin(i*PI/180);
      toneVal=(int)(100+1000*sinVal);
        tone(piezoPin,toneVal);
        delay(20);
    }
    lcd.clear();
    lcd.backlight();
    lcd.display();
    lcd.setCursor(6,0);
    lcd.print("Gas");
    lcd.setCursor(4,1);
    lcd.print("Leakage!");
    }                                                                       // -->
    else if ( GasValue < 200 ) {                                            // <-- 가스센서의 값이 200미만이면 (가스누출이 안됐으면, 혹은 가스누출이 됐다가 멈췄으면) 연동된 피에조 부저의 사이렌 소리를 끈다
      noTone(piezoPin);
    }                                                                       // -->
    delay(1000);                                                            //1초의 딜레이를 줌
    lcd.clear();                                                            // LCD를 클리어한다
  }                                                                                 // --> 
  } 
