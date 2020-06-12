## 예약 API 호출 fingerprint 작성방법

* fingerprint값은 검증시 10분의 만료시간을 가지며 만료시 재생성해주시기 바랍니다. 

  

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
		String secureKey = "35q97RxB6eGPLWBB";	//발급된 secretKey
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

