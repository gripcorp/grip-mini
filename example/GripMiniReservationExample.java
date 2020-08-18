public class GripMiniReservationExample {
	static final String SERVICE_ID = "localhost";
	static final String SECURE_KEY = "35q97RxB6eGPLWBP";
	ObjectMapper mapper;
	public static class HmacShaSignature {
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
			String secureKey = "35q97RxB6eGPLWBB"; // 발급된 secretKey
			String serviceId = "svcId"; // 서비스ID
			long timestamp = System.currentTimeMillis();

			String data = String.format("%s;%d", serviceId, timestamp);

			String fingerprint = HmacShaSignature.digest(data, secureKey);
			System.out.println("fingerprint : " + fingerprint + " timestamp : " + timestamp);

			// fingerprint : 8a194e2a93ba5b7e149a647f88ff524974b5d942 timestamp :
			// 1567066803336
		}

	}

	public GripMiniReservationExample() {
		mapper = new ObjectMapper();
	}
	public String makeFingerprint(String serviceId, long timestamp) {
		String data = String.format("%s;%d", serviceId, timestamp);
		try {
			return HmacShaSignature.digest(data, SECURE_KEY);
		} catch (Exception e) {
		}
		return null;
	}

	public String post(String urlString, String serviceId, Map<String, Object> params) throws Exception {
		long timestamp = System.currentTimeMillis();
		String fingerprint = makeFingerprint(SERVICE_ID, timestamp);

		if (fingerprint == null) {
			throw new Exception("invalid fingerprint");
		}

		urlString += "?serviceId=" + serviceId;
		
		//param 을 전달하는 방식은 queryString을 사용하건 json 으로 된 body 를 지원하고 있음		
		//파라메터 전달 1안, URLEncoded query string 방식
		if (!MapUtils.isEmpty(params)) {
			urlString += "&" + mapToQueryString(params);
		}
		
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5000); // set connection timeout (request)
		con.setReadTimeout(5000); // set read timeout (response)
		con.setRequestMethod("POST");
		// Fingerprint 정보 입력
		con.setRequestProperty("X-Fingerprint-Timestamp", String.valueOf(timestamp));
		con.setRequestProperty("X-Fingerprint", fingerprint);
		
		
		//파라메터 전달 2안, json body 
		if (!MapUtils.isEmpty(params)) {	
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			String jsonString = mapper.writeValueAsString(params);
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();

			byte[] postData = jsonString.getBytes(StandardCharsets.UTF_8);
			os.write(postData);
			os.flush();
			os.close();
		}


		// 요청하기
		int responseCode = con.getResponseCode();

		Charset charset = Charset.forName("UTF-8");

		// 응답 결과 읽기
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	public String get(String urlString, String serviceId, Map<String, Object> params) throws Exception {
		long timestamp = System.currentTimeMillis();
		String fingerprint = makeFingerprint(SERVICE_ID, timestamp);

		if (fingerprint == null) {
			throw new Exception("invalid fingerprint");
		}
		urlString += "?serviceId=" + serviceId;

		if (!MapUtils.isEmpty(params)) {
			urlString += "&" + mapToQueryString(params);
		}

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setConnectTimeout(5000); // set connection timeout (request)
		con.setReadTimeout(5000); // set read timeout (response)
		con.setRequestMethod("GET");

		// Fingerprint 정보 입력
		con.setRequestProperty("X-Fingerprint-Timestamp", String.valueOf(timestamp));
		con.setRequestProperty("X-Fingerprint", fingerprint);

		// 요청하기
		int responseCode = con.getResponseCode();

		Charset charset = Charset.forName("UTF-8");

		// 응답 결과 읽기
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	/**
	 * Map 을 문자열로 변환 
	 * 
	 * @param params
	 * @return
	 */
	public String mapToQueryString(Map<String, Object> params) {
		StringBuilder b = new StringBuilder();
		for (Entry<String, Object> entry : params.entrySet()) {
			try {
				b.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return b.toString();
	}

	public static void main(String[] args) {
		GripMiniReservationExample obj = new GripMiniReservationExample();
		
		//예약 등록하기
		
		try {
			Map<String, Object> params = Maps.newHashMap();
			params.put("userCode", "goln0z79");
			params.put("title", "한글제목");
			params.put("description", "한글설명");
			params.put("start", "2020-08-19 10:00:00");
			params.put("coverUrl", "https://www.talkwalker.com/uploads/2017/00001/mc1/Image%20recognition%20example.png");
			
			String response = obj.post("http://eapi.grip.show/svc/reservations", SERVICE_ID, params);
			System.out.println(response);
			/* 응답 예시
			 	{
				   "reservationId":"o5wkm1733g2vlpeq",
				   "userId":"goln0z79",
				   "embedUrl":"http://play.grip.show/embed/r/o5wkm1733g2vlpeq",
				   "createAt":1597730796442
				}
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//예약 조회하기
		try {
			Map<String, Object> params = Maps.newHashMap();
			params.put("userCode", "goln0z79");

			String response = obj.get("http://eapi.grip.show/svc/reservations/7wv3y1q88z109rnk", SERVICE_ID, params);
			System.out.println(response);
			/* 응답 예시
			 {
			   "reservationId":"7wv3y1q88z109rnk",
			   "userId":"goln0z79",
			   "userCode":"goln0z79",
			   "embedUrl":"http://play.grip.show/embed/r/7wv3y1q88z109rnk",
			   "createAt":1597716398000,
			   "state":3,
			   "title":"오늘의 예약",
			   "description":"",
			   "coverUrl":"http://thumb.grip.show/222/10/32wfzt9x4qzp9wz.jpg&client=amp-blogside-v2&signature=eda4c8a29251b6fa26f337bfe4e95973f55c2846?type=w&w=750",
			   "start":"2020-08-18 10:00:00",
			   "end":"2020-08-18 23:00:00"
			}
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		//예약 컨텐츠 조회하기
		try {
			Map<String, Object> params = Maps.newHashMap();

			String response = obj.get("http://eapi.grip.show/svc/reservations/7wv3y1q88z109rnk/content", SERVICE_ID, params);
			System.out.println(response);
			/*
			 {
			   "reservationId":"7wv3y1q88z109rnk",
			   "embedUrl":"http://play.grip.show/embed/r/7wv3y1q88z109rnk",
			   "contents":[
			      {
			         "contentId":"z3xye69d",
			         "playbackUrl":"http://e-live-kr.grip.show/vod/smil:l392ovre1g.smil/playlist.m3u8",
			         "contentType":4
			      },
			      {
			         "contentId":"k3gpyw3w",
			         "playbackUrl":"http://e-live-kr.grip.show/vod/smil:1mo8op8933.smil/playlist.m3u8",
			         "contentType":4
			      }
			   ]
			}
			 
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
