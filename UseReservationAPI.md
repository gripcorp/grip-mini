# 예약 관련 API 가이드

라이브 예약에 대한 추가, 삭제, 조회 기능을 제공한다.
HMAC 형태의 api 인증을 진행하며 



예제 파일 참조 

[GripMiniReservationReservation.java]: ./example/GripMiniReservationExample.java	"예제 파일"



## 1. Live 예약하기 

- Path : /svc/reservations
- Method : POST

### 예약의 목적
```
사전에 해당 방송에 대한 정보(제목, 설명, 시간, 섬네일 등)를 입력하여 방송시에 노출되는 URL을 얻기 위함
겹치는 시간이 존재하는 경우 새로운 예약 생성 불가
```

#### 필요 파라메터
| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| serviceId     | String        | Y    | 발급된 서비스 아이디| |
| userCode      | String        | Y    | 방송할 그립사용자의 이용자코드 | |
| title         | String        | Y    | 예약 제목 (표현됨)| |
| coverUrl      | String        | Y    | 커버이미지 URL | 다운로드 가능한 URL |
| description   | String        |  N      | 예약에 대한 설명 | |
| start         | String        | Y    | 방송 예상 시작 시간(해당 예약 시간내에 방송해야 해당 예약 URL에 노출됨 | yyyy-MM-dd HH:mm:ss |
| end           | String        | N      | 예약 | yyyy-MM-dd HH:mm:ss, 기본 시작시간  + 1시간 |
| secret | Boolean | N | 예약을 비공개로 이용함 | 기본은 False |



## 2. 예약 기간 조회
- Path : /svc/reservations
- Method : GET
### 설명
```
사용자나 기간에 따른 예약 조회
최대 조회 시간은 1주일을 넘기지 않아야 합니다.
```
**데이터 운영용 API 이니 서비스에 직접 사용은 지양해주세요.**



#### 필요 파라메터

| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| serviceId     | String        | Y    | 발급된 서비스 아이디| |
| userCode      | String        | Y    | 조회할 그립사용자의 이용자코드 | |
| start         | String        | Y    | 조회 시작 시간  | yyyy-MM-dd HH:mm:ss |
| end           | String        | Y    | 조회 종료 시간  | yyyy-MM-dd HH:mm:ss |


## 3. 예약 조회
- Path : /svc/reservations/{reservationId}
- Method : GET

### 목적
```
reservationId을 통한 조회
```


| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| reservationId | String        | Y    | 예약 ID| Path에 존재함 |
| serviceId     | String        | Y    | 발급된 서비스 아이디| |
| userCode      | String        | Y    | 조회할 그립사용자의 이용자코드 | |

### 응답

```json
{
  "userCode": "geAda33",
  "embedUrl": "https://play.grip.show/embed/Dfa3213E23Dfa3213E23",
  "reservationId": "Dfa3213E23",
  "title":"예약 타이틀",
  "description": "예약 설명",
  "coverUrl":"https://play.grip.show/images/test.jpg",
  "start" : "2019-10-21 14:30:00",
  "end" : "2019-10-21 15:30:00",
  "state" : 0,	
  "statistics": {
    "contentIds": [
      "Dfa3213E23"
    ],
    "accUserCount": 2,
    "startedAt": "2019-10-21T03:20:51.623Z",
    "endedAt": "2019-10-21T03:20:51.623Z"
  },
  "createAt": "2019-10-21T03:20:51.623Z"
}

```
```
statistic의 경우 예약과 관련된 시작 및 종료 시간 및 간단한 통계정보가 내려가며
예약 종료 시간 이후 부터 유효한 값이 내려간다
```
### 예약 상태값

| 상태값    | 설명 |
| :-----------  | :------------ |
| 0 | 대기 상태 |
| 1 | 라이브 |
| 2 | <s>미사용</s> |
| 3 | Live VOD |



## 4. 예약 수정 

- Path : /svc/reservations/{reservationId}
- Method : PATCH
- ContentType : application/json;charset=UTF-8



#### 필요 파라메터

| 파라메터 이름 | 타입    | 필수 | 설명                                                         | 비고                                        |
| :------------ | :------ | :--- | ------------------------------------------------------------ | ------------------------------------------- |
| serviceId     | String  | Y    | 발급된 서비스 아이디                                         |                                             |
| userCode      | String  | Y    | 방송할 그립사용자의 이용자코드                               |                                             |
| title         | String  | Y    | 예약 제목 (표현됨)                                           |                                             |
| coverUrl      | String  | Y    | 커버이미지 URL                                               | 다운로드 가능한 URL                         |
| description   | String  | N    | 예약에 대한 설명                                             |                                             |
| start         | String  | Y    | 방송 예상 시작 시간(해당 예약 시간내에 방송해야 해당 예약 URL에 노출됨 | yyyy-MM-dd HH:mm:ss                         |
| end           | String  | N    | 예약                                                         | yyyy-MM-dd HH:mm:ss, 기본 시작시간  + 1시간 |
| secret        | Boolean | N    | 예약을 비공개로 이용함                                       | 기본은 False                                |



## 5. 예약 삭제

- Path : /svc/reservations/{reservationId}
- Method : DELETE

### 목적
```
reservationId를 이용하여 예약 삭제(취소)
```
| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| reservationId | String        | Y    | 예약 ID| Path에 존재함 |
| serviceId     | String        | Y    | 발급된 서비스 아이디| |
| userCode      | String        | Y    | 그립사용자의 이용자코드 | |



## 6. 예약 컨텐츠 조회

- Path : /svc/reservations/{reservationId}/content
- Method : GET

### 목적

```
reservationId와 연결된 content 정보 조회
```


| 파라메터 이름 | 타입   | 필수 | 설명                 | 비고          |
| :------------ | :----- | :--- | -------------------- | ------------- |
| reservationId | String | Y    | 예약 ID              | Path에 존재함 |
| serviceId     | String | Y    | 발급된 서비스 아이디 |               |



#### 컨텐츠 타입

| 값   | 설명                   | 비고 |
| :--- | ---------------------- | ---- |
| 3    | 라이브 방송            |      |
| 4    | 라이브로 부터 나온 VOD |      |

### 응답

```json
{
    "reservationId": "7wv3y1q88z109rnk",
    "embedUrl": "http://play.grip.show/embed/r/7wv3y1q88z109rndk",
    "contents": [
      {
        "contentId": "z3xye69d2",
        "playbackUrl": "https://e-live-kr.grip.show/live/ngrp:l392ovre1g3_all/playlist.m3u8",
        "contentType": 3	//상단 표 참조 3 : live, 4 : vod
       }
    ],
}
```





#### # errorCode 값

| ErrorCode | 오류 설명                      | 비고 |
| :-------- | :----------------------------- | ---- |
| P001      | 유효하지 않은 ServiceId        |      |
| P002      | 유효하지 않은 사용자 코드      |      |
| P003      | 유효하지 않은 예약Id           |      |
| P004      | 유효하지 않은 시작시간         |      |
| P005      | 유효하지 않은 종료시간         |      |
| E001      | 해당 시간에 예약이 이미 존재함 |      |




# Appendix #2
## 사용자 sessionKey 생성 방법
```
사용자가 채팅을 하기위하여 로그인과 유사한 sessionKey 생성 가이드 입니다. 
```

AESCryptor 예제 코드

```java

public class AESCryptor {
	private static final byte[] IV = new byte[16];
	
	public static String encrypt(String source, String key) {
		try {
			return new String(Base64.encodeBase64(encrypt(source.getBytes(), key.getBytes())), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
	public static String decrypt(String source, String key) {
		try {
			return new String(decrypt(Base64.decodeBase64(source.getBytes()), key.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static byte[] encrypt(byte[] source, byte[] key) {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(IV);
		SecretKeySpec newKey = new SecretKeySpec(key, "AES");

		try {
			Cipher cipher = null;
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

			return cipher.doFinal(source);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	private static byte[] decrypt(byte[] source, byte[] key) {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(IV);
		SecretKeySpec newKey = new SecretKeySpec(key, "AES");

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			return cipher.doFinal(source);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
		//예제
	public static void main(String[] args) throws JsonProcessingException {
		String secureKey = "35q97RxB6eGPLWBB";

		Map<String,Object> param = Maps.newHashMap();
		param.put("userId", "testuserId");
		param.put("nickname", "테스트사용자");
		param.put("host", "snsform.co.kr");
		param.put("serviceId", "svnId");
		param.put("timestamp", System.currentTimeMillis());
		
		ObjectMapper mapper = new ObjectMapper();
		
		String data = mapper.writeValueAsString(param);
		System.out.println(data);
		//{"nickname":"테스트사용자","host":"snsform.co.kr","serviceId":"svnId","userId":"testuserId","timestamp":1567067294714}

		String sessionKey = AESCryptor.encrypt(data, secureKey);
		System.out.println(sessionKey);
		//NL1b1VMNbJ9XyhEh2fY/ZR0i/r6E3YFlQNAgayCq+5vqtWGrXzYt3dNjB1n6+Z1J8/BIgDEmT7FbYeQ6oRlVqVDRyUY24UHx0Ti36xcI+UId/KC3LkTWbNDyFqt+9EIfyT8snRdrN3n+GIgVjdDwz/fQPOw9O8hBQa1/gn49Z60=
	}
}
```
위에서 보여진 AESCryptor의 encrypt(String, String)을 통해 만들어진 데이터를 보내주시면 되며 
전달되는 데이터의 형태는 다음의 json 의 형태로 전달해주시면됩니다.

```js
{
	"userId":"testUserId",
	"nickname:"테스트사용자",
	"host" : "snsform.co.kr",
	"serviceId" : "svcId",
	"timestamp": 1566724286763
}
```
사용자 정보
| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| userId 	| String        | N    		| 클라이언트사 식별용 ID | 실제 아이디보다 식별가능한 유니크ID |
| nickname    	 | String        | N   		| 채팅에 사용될 닉네임 | 개인정보 관련 마스킹|
| host      	| String        | Y		| 호출 페이지의 host 도메인 | |
| serviceId      | String        | Y   		| 발급된 서비스 ID | |
| timestamp      | Long        | Y    		| 해당 sessionKey 가 생성된 timestamp | |

* userId, nickname 이 비어있는경우 Guest 로 로그인 처리됨 
