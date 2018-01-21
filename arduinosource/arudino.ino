#include <SoftwareSerial.h> 
SoftwareSerial bluetooth(10, 11); //블루투스 모듈을 10번과, 11번 포트로 설정

#define led1 6        //led1을 6번 포트로 설정 
#define led2 9        //led2을 9번 포트로 설정 
#define led3 4        //led3을 4번 포트로 설정 
String comand;        //안드로이드에서 받아올 문자열변수
void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(led1, OUTPUT);   //led1을 출력으로 설정
  pinMode(led2, OUTPUT);   //led2을 출력으로 설정
  pinMode(led3, OUTPUT);   //led3을 출력으로 설정

  // put your setup code here, to run once:

}

void loop() {
 //led 제어문
  comand = "";         //공백 문자열로 설정
  if(bluetooth.available()){         //블루투스 데이터를 받음
    while(bluetooth.available()){
      char caracter = bluetooth.read();  //블루투스에서 얻어온 값을 caracter에 저장

      comand += caracter;
      delay(10);
      Serial.println("a");
    }
    //Serial.print("comand: ");
    //Serial.println(comand);

    if(comand.indexOf("led1") >= 0){            //comand의 값이 led1이면 켜졌다 껐다를 반복
     digitalWrite(led1, !digitalRead(led1));
    
      Serial.println("vai Liger/Desligar Led1");
    }
    if(comand.indexOf("led2") >= 0){           //comand의 값이 led2이면 켜졌다 껐다를 반복
     digitalWrite(led2, !digitalRead(led2));
    
      Serial.println("vai Liger/Desligar Led2");
    }
    if(comand.indexOf("led3") >= 0){             //comand의 값이 led3이면 켜졌다 껐다를 반복
     digitalWrite(led3, !digitalRead(led3));
    
      Serial.println("vai Liger/Desligar Led3");
    }
 
  }


}
