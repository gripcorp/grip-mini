Grip Mini API Guide v2 (작업중)
======================
# 사전 정보
- secureKey : sessionKey, API 인증을 위한 fingerprint
- serviceId : 서비스 id
- installKey : 개발앱 설치시 필요 정보
- API host : 서비스 주소

위의 내용은 메일을 확인하시면 됩니다.

# 예약 API 가이드

라이브 예약에 대한 추가, 삭제, 조회 기능을 제공한다.
HMAC 형태의 api 인증을 진행하며 

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



## 2. 예약 기간 조회
- Path : /svc/reservations
- Method : GET
### 설명
```
사용자나 기간에 따른 예약 조회
최대 조회 시간은 1주일을 넘기지 않아야 합니다.
```
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

## 3. 예약 삭제

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



------------------------------------
# 사이트에 적용하기

## 1. sessionKey 생성하기
채팅을 위하여 sessionKey를 입력해야함
이는 사용자 정보이며 userId의 unique 를 보장해줘야함
비로그인 사용자는 sessionKey없이 접근이 가능하나
readOnly로 동작하게 된다.

방법은 Appendix #2를 참조

### 2. iframe 직접 embed하기 
예약이후에 얻어지는 URL을 이용하여 iframe 태그를 사용하여 적용하면됨.
작성시 iframe 속성은 다음과 같음 


iframe의 비율을 3:4 혹은 9:16 비율로 지정하여 디자인 가이드를 확인해주시면됩니다.

예제코드
```html
<iframe th:src="'https://{serverHost}/embed/r/k9lv041no6e7grod?serviceId=serviceId&sessionKey=k9lv041no6e7grodk9lv041no6e7grod" 
frameborder="0" allowfullscreen=true style="width: 320px; height: 500px;" scrolling="no"></iframe> 
```

### 3. javascript를 사용하여 적용하기 
reservationId 을 이용한 초기화와 몇몇 기능을 제공함.

파라메터 설명 

| 파라메터 이름    | 타입           | 필수    | 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| reservationId | String        | Y    | 예약 ID| Path에 존재함 |
| serviceId     | String        | Y    | 발급된 서비스 아이디| |
| sessionKey      | String        | Y    | 암호화된 로그인 정보 | |
| type      | String        | N    | mini의 UI 타입 | 기본 float, 하단의 Mini의 UI타입 참조 |
| z-index      | Int        | N    | 레이어 z-index 값 | |
| confirmCellular | boolean   | N    | 셀루러 사용시 확인 기능 사용 여부 | 기본 false |
| couponCallback | function   | N    | 쿠폰 받기 콜백 | Deprecated | 
| loginCallback  | function   | N    | login 필요 콜백 | Deprecated |
| onShare  | function   | N    | 공유버튼 콜백 | Deprecated |
| onPlayEnd  | function   | N    | 재생 종료 콜백 | Deprecated |


mini의 UI 타입

| type  | 설명 |
|----|----|
| none | floating 형태를 사용하지 않음 |
|float | 우측 하단의 floting type |
|top | 상단 고정형 |


콜백들은 하단의 별도의 형태로 변경되며 이전 사용 메소드는 v2에서는 호환이 가능하나 아래와 같은 방법을 이용해주세요.


| 콜백 이벤트명    | 파라메터      | return |  설명        | 비고            |
| :-----------  | :------------ |:-----------|:-----------|--------------- |
| needLogin  |    | login 필요 | |  |
| share  | url  | 공유버튼 클릭 | |  |
| playEnd  |    | 재생 종료  | |  |
| receiveCoupon | coupon, user | 쿠폰받음 | | 초기화시에 전달된 쿠폰 정보 혹은 그립으로 받은 쿠폰 정보 |
| clickProduct | product, user | 상품 클릭 | | 클릭한 상품 정보 |
| clickCart | product, user | 상품의 카트아이콘 클릭 | | 클릭한 상품 정보 |
| clickShop | user | 상점 아이콘 클릭  | 표시할 상품 목록 정보 |  |


* 상품 목록용 상품 정보

| 파라메터 이름    | 타입           | 필수    	| 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| productId 	| String  	| Y    | 클라이언트사 상품 ID 	|  |
| name     	| String	| Y    | 상품명		| |
| basePrice     | Number 	| Y    | 상품 원본 표시 가격 | |
| salePrice     | Number  	| Y    | 상품 판매 표시 가격 | |
| stock      	| Number  	|  N   | 재고 숫자 		| 없는경우 soldout 처리가 자동으로 되지 않음 |
| soldout      	| Boolean 	| N    | 품절 여부 		| 없는경우 stock이 0인경우 자동 품절 처리 |
| thumbImageUrl | URL     	| Y    | 상품 대표 이미지    | |
| detailUrl 	| URL     	| N    | 상품 목록 클릭시 이동할 경로 | 콜백에서 처리하거나 detailUrl 은 존재해야함 |
| cartUrl 	| URL     	| N    | 상품 목록 카트 클릭시 이동할 경로 | 동작 형태는 detailUrl과 같으며 없는경우 detailUrl 처럼 동작됨 |

* 쿠폰 정보

| 파라메터 이름    | 타입           | 필수    	| 설명        | 비고            |
| :-----------  | :------------ |:-----------|------------ | --------------- |
| couponId 	| String  	| Y    | 클라이언트사 쿠폰 ID 	|  |
| type     	| Integer	| Y    | 쿠폰의 타입 	| 하단의 타입 참조|
| title     	| String 	| Y    | 쿠폰 타이틀 | |
| desc     	| String  	| Y    | 쿠폰 금액이나 설명 | |
| duration     	| Number  	|  N   | 지급 누적시간  시간 | 초단위 값, type이 누적시간인 경우 필수   |


예제코드
```html

<script src="https://dev-resource.grip.show/js/grip.mini.min.js"></script><!-- 개발용 리소스 주소 -->
<script src="https://resource.grip.show/js/grip.mini.min.js"></script> <!-- real용 리소스 주소 -->
<div id="embed" style=""></div>

var mini = GripMini({
	parentId:"embed"
	, reservationId : reservationId
	, sessionKey: sessionKey
	, serviceId : "serviceId"
	, type : "float",
	, coupon : [{
		couponId: "coupon001",  //클라이언트사쿠폰 아이디
		type : 1,   // 1 : 입장쿠폰, 2 : 누적시간 쿠폰
		title : "라이브 입장 쿠폰",     //쿠폰 타이틀
		desc : "2500원 할인",       //쿠폰 금액이나 설명을 위한 정보
		duration : 600,     //누적시간 쿠폰 발급 시간, 초 단위
     	}]
    });


// 상점 아이콘 클릭
mini.on("clickShop", function(){
    //상점 목록에 표현할 상품 목록 반환
    return [{
        productId: "test01",  //클라이언트사 상품 아이디
        name:"테스트 상품",
        basePrice : 12000,
        salePrice : 8000,   //basePrice와 같거나 없으면 미노출
        stock : 120,
        soldout : false, //optional, 없는경우 stock 기준으로 0의 경우 soldout 표시
        thumbImageUrl : "https://api.gripshow/images/test.jpg",
        detailUrl : "https://product.grip.show/prd/test_product?ref=tester",
        cartUrl : "https://cart.grip.show/add/test_product?user=tester", //optional 
    }];
});


mini.on("clickProduct", function(product, user){
    //product : 선택한 상품 정보 전달
    return true;
});

mini.on("clickCart", function(product, user){
    //product : 선택한 상품 정보 전달
    return true;
});


// 해당 사용자에게 제공 가능한 쿠폰
mini.on("receiveCoupon", function(coupon, user){
    //coupon : 전달한 쿠폰의 정보 그대로 반환
    //받는 대상자의 사용자 정보 전달
});

```





# Appendix #1
## 예약 API HMAC 검증 방법
```
서비스용으로 제공된 secureKey를 이용하여 serviceId와 timestamp를 이용하여
fingerprint 전달하면서 상호 검증을 진행함 

* fingerprint값은 검증시 10분의 만료시간을 가지며 만료시 재생성해주시기 바랍니다. *
```

HmacSignature 클래스 예제

```java
public class HmacShaSignature {
	private static final String HMAC_SHA_ALGORITHM = "HmacSHA1";

	private static String toHexString(byte[] bytes) {
		try (Formatter formatter = new Formatter();) {

			for (byte b : bytes) {
				formatter.format("%02x", b);
			}

			return formatter.toString();
		}
	}

	public static String digest(String data, String key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	public static void main(String[] args) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		String secureKey = "35q97RxB6eGPLWBB";	//개발용 테스트키 
		String serviceId = "svcId";	//서비스ID
		long timestamp = System.currentTimeMillis();

		String data = String.format("%s;%d", serviceId, timestamp);
		
		String fingerprint = HmacShaSignature.digest(data, secureKey);
		System.out.println("fingerprint : " + fingerprint + " timestamp : " + timestamp);
		
		//fingerprint : 8a194e2a93ba5b7e149a647f88ff524974b5d942 timestamp : 1567066803336
	}
	
}

```
```
data에는 String.format("%s;%d", serviceId, timestamp) 을 통해 만들어진 문자열
key에는 SecureKey를 String 그대로 사용하여 전달하여 나온 결과를 사용합니다.

이렇게 만들어진 Fingerprint 와 timestamp는 API 호출시에 아래와 같은 헤더값으로 입력하여 요청하시면 됩니다.

Name : X-Fingerprint
Value : fingerprint값 (10분의 만료시간)

Name : X-Fingerprint-Timestamp
Value : fingerprint 만들때 사용된 timestamp 
```

```java
HttpPost request = new HttpPost(url);
request.setHeader("X-Fingerprint", fingerprint);	
request.setHeader("X-Fingerprint-Timestamp", timestamp);
```
*JAVA로 가이드 해드리기는 했으나 곧 library 형태로 배포하거나 다른 개발언어 요청해주시면 추가 가이드 해드리도록 하겠습니다.*


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
