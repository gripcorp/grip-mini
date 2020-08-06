# GripMini 적용 방법



제공되는 js 파일을 이용하여 적용 되고자 하는 페이지에 적용하는 방법입니다.



### 적용예시

```html

<script src="https://resource.grip.show/js/grip.mini.2.min.js?ts=20200622"></script>
<div id="embed" style=""></div>

<script>
var mini = GripMini({
	parentId:"embed"
	, reservationId : reservationId
	, sessionKey: sessionKey
	, serviceId : "serviceId"
    
	, floating : {
        type : "float",
        options : {
            width : 100,
            height: 150,
            bottom : 100,
            right : 10,
            "z-index" : 100
        }
    }
	, coupons : [{
		couponId: "coupon001",  //클라이언트사쿠폰 아이디
		type : "join",   // "join" : 입장쿠폰, "duration" : 누적시간 쿠폰
		title : "라이브 입장 쿠폰",     //쿠폰 타이틀
		price : "2500원 할인",       //쿠폰 금액이나 설명을 위한 정보
		desc :"2만원 이상 사용가능",
		duration : 600,     //누적시간 쿠폰 발급 시간, 초 단위
     	}]
    
    ,  excludeNoti : [6,7]	//좋아요, follow 노티 노출 제외
    });

mini.on("productList", function(){
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


mini.on("product", function(product){
    //product : 선택한 상품 정보 전달
    return true;
});

mini.on("cart", function(product){
    //product : 선택한 상품 정보 전달
    return true;
});


// 해당 사용자에게 제공 가능한 쿠폰
mini.on("coupon", function(coupon){
    //coupon : 전달한 쿠폰의 정보 그대로 반환
    return true;
});
    
mini.pause();	//영상 일시 정지 
mini.resume();	//영상 다시 재생
</script>
```



###### GripMini 초기화 옵션 파라메터 

| 파라메터명      | 타입    | 필수여부 | 비고                                   |
| --------------- | ------- | -------- | -------------------------------------- |
| parentId        | String  | Y        | 플레이어가 embed될 element ID          |
| serviceId       | String  | Y        | 발급된 serviceId                       |
| sessionKey      | String  | N        | 로그인 사용자 sessionKey               |
| type            | String  | N        | 기본 float                             |
| confirmCellular | Boolean | N        | 3G/LTE 데이터 사용 확인 창 이용        |
| productCount    | Integer | N (Y)    | **상품 목록 기능 사용시 필수**         |
| z-index         | Integer | N        | 기본 10000                             |
| coupons          | List    | N        | 쿠폰 데이터, 쿠폰 기능 사용시 필수     |
| floating        | Map     | N        | 플로팅 뷰 이용시 설정 , 하단 설명 참조 |
| muted           | Boolean | N        | 음소거 상태 재생 여부, 기본값 true     |
| share           | Boolean | N        | 공유버튼 사용 여부, 기본값 true        |
| messageCount    | Integer | N        | 메시지 유지수, 기본값 300, 최대 500    |
| excludeNoti     | List    | N        | 라이브 노티 노출 제외 목록            |
| useClose | Boolean | N | 기본값 false, 닫기 버튼 노출 여부 |



excludeNoti 지원 타입

라이브 노티 제외용 값 설명

| 정보         | 값   | 비고                      |
| ------------ | ---- | ------------------------- |
| NORMAL_INFO  | 0    | 기본 라이브와 관련된 정보 |
| CONNECT_USER | 1    | 사용자 접속               |
| CONCURRENT   | 2    | 누적사용자 수 (10명 단위) |
| CART         | 3    | 장바구니 넣음             |
| SOLDOUT      | 4    | 품절                      |
| SOLDOUT_SOON | 5    | 품절 임박                 |
| LIKE         | 6    | 좋아요 누적(5000 단위)    |
| FOLLOW       | 7    | 팔로우                    |



###### floating 옵션 

floating 은 화면을 벗어났을때 표현하는 방법 선택 할 수 있는 설정입니다.

type 은 float, top 이 존재하며 float는 우측 하단에 작은 버전 top 은 상단에 일부를 고정하는 기능입니다.

크기 및 위치에 대한 옵션을 오버라이드 가능하며 지원가능한 옵션은 하단의 표를 참조하시면됩니다.



| 옵션명  | 타입    | 비고                                                    |
| ------- | ------- | ------------------------------------------------------- |
| width   | Integer | float의 작은 화면의 넓이                                |
| height  | Integer | float의 작은 화면의 높이                                |
| bottom  | Integer | float의 하단 여백                                       |
| right   | Integer | float의 우측 여백                                       |
| height  | Integer | top의 높이                                              |
| z-index | Integer | z-index값, 없는경우 mini의 z-index값 사용, 기본값 10000 |



###### 쿠폰 데이터 구조

| 파라메터명 | 타입    | 필수여부 | 비고                                       |
| ---------- | ------- | -------- | ------------------------------------------ |
| couponId   | String  | Y        | 인지 가능한 쿠폰 ID                        |
| type       | String  | Y        | join : 진입 쿠폰, duration : 누적시청 쿠폰 |
| title      | String  | Y        | 쿠폰명 예) 라이브 입장 쿠폰                |
| price      | String  | Y        | 금액이나 설명 정보 예) 2500원 할인         |
| desc       | String  | Y        | 쿠폰 적용 정보  예) 2만원 이상 적용        |
| duration   | Integer | N(Y)     | duration 타입 사용시 필수                  |



#### 지원하는 메소드 목록

미니에서 제공하는 메소드(영상 제어등)

| 콜백이름 | 목적           | 비고 |
| -------- | -------------- | ---- |
| pause    | 영상 일시 정지 |      |
| resume   | 영상 다시 재생 |      |



#### 지원하는 콜백 목록

모든 콜백 메소드는 해당 응답을 처리한 경우 true 를 return 해야 합니다. 

콜백에서 false를 리턴하는경우 기본 동작이 수행됩니다.

| 콜백이름    | 목적               | 비고                                |
| ----------- | ------------------ | ----------------------------------- |
| needLogin   | 로그인 요청        |                                     |
| product     | 상품 클릭          |                                     |
| cart        | 장바구니 클릭      |                                     |
| coupon      | 쿠폰 받음          |                                     |
| share       | 공유버튼 누름      |                                     |
| playend     | 재생이 종료됨(VOD) |                                     |
| productList | 상품목록 요청      | 상품 목록 기능을 이용할 때 필수기능 |
| close       | close버튼 누름     |                                     |



##### 콜백별 파라메터 및 예제

1. 기본적인 콜백

```html
<script>
mini.on("product", function(product){ //product : 클릭한 상품 정보
    //TODO : 상품 클릭 동작을 처리한다.
    return true;
});

mini.on("cart", function(product){ //product : 클릭한 상품 정보
    //TODO : 상품의 카트 클릭 동작을 처리한다.
    return true;
});

mini.on("coupon", function(coupon){ //coupon : 전달한 쿠폰의 정보
    //TODO : 해당 사용자에게 사이트의 쿠폰을 발행한다.
    return true;
});
    
mini.on("needLogin", function(){
    //TODO : 로그인 유도용 페이지로 이동한다.
    return true;
});
    
mini.on("share", function(){ 
    //TODO : 클립보드에 공유용 URL를 기록하고 알림을 준다.
    return true;
});

mini.on("playend", function(){
    //TODO : 예약목록을 갱신해준다 (필요한경우 이용)
    return true;
});
    
</script>
```



2. 상품목록 제공용 콜백

```html
<script>
mini.on("productList", function(){
    return [{
        productId: "test01",  //클라이언트사 상품 아이디
        name:"테스트 상품",		//상품명 
        basePrice : 12000,		//할인전
        salePrice : 8000,   //basePrice와 같거나 없으면 미노출
        stock : 120,		//재고 수, 0 인경우 품절로 표시
        soldout : false, //optional, 없는경우 stock 기준으로 0의 경우 soldout 표시
        thumbImageUrl : "https://api.gripshow/images/test.jpg",
        productUrl : "https://product.grip.show/prd/test_product?ref=tester",
        cartUrl : "https://cart.grip.show/add/test_product?user=tester", //optional 
    },
    {
        productId: "test02",  //클라이언트사 상품 아이디
        name:"테스트 상품2",		//상품명 
        basePrice : 12000,		//할인전
        salePrice : 8000,   //basePrice와 같거나 없으면 미노출
        stock : 10,		//재고 수, 0 인경우 품절로 표시
        soldout : false, //optional, 없는경우 stock 기준으로 0의 경우 soldout 표시
        thumbImageUrl : "https://api.gripshow/images/test.jpg",
        productUrl : "https://product.grip.show/prd/test_product?ref=tester",
        cartUrl : "https://cart.grip.show/add/test_product?user=tester", //optional 
    }];
});
    
</script>

```





###### 상품 데이터 구조

| 파라메터명    | 타입    | 필수여부 | 비고                                                        |
| ------------- | ------- | -------- | ----------------------------------------------------------- |
| productId     | String  | Y        | 클라이언트사 상품 코드                                      |
| name          | String  | Y        | 상품명                                                      |
| basePrice     | Integer | Y        | 상품 원가                                                   |
| thumbImageUrl | String  | Y        | 상품이미지                                                  |
| productUrl    | String  | N        | 상품 클릭시 이동 URL (콜백 등록시 미필요)                   |
| cartUrl       | String  | N        | 카트 클릭시 이동 URL (콜백 등록시 미필요)                   |
| salePrice     | Integer | N        | 할인가격 (없으면 미노출)                                    |
| stock         | Integer | N        | 존재하는경우 0의값은 soldout처리, 없으면 soldout 처리 못함  |
| soldout       | Boolean | N        | 재고가 0보다 크더라고 soldout 이 존재하고 true라면 품절표시 |

