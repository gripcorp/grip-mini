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

| 파라메터 이름 | 타입   | 필수 | 설명                                | 비고                                |
| :------------ | :----- | :--- | ----------------------------------- | ----------------------------------- |
| userId        | String | N    | 클라이언트사 식별용 ID              | 실제 아이디보다 식별가능한 유니크ID |
| nickname      | String | N    | 채팅에 사용될 닉네임                | 개인정보 관련 마스킹                |
| host          | String | Y    | 호출 페이지의 host 도메인           |                                     |
| serviceId     | String | Y    | 발급된 서비스 ID                    |                                     |
| timestamp     | Long   | Y    | 해당 sessionKey 가 생성된 timestamp |                                     |

* userId, nickname 이 비어있는경우 Guest 로 로그인 처리됨 