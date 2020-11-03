# iOS 관련 추가 작업

자동 재생과 관련하여 iOS 쪽에서는 별도의 Webview에 설정 작업이 필요 합니다.



###### UIWebView를 사용하는 경우



```objective-c
[self.webview setMediaPlaybackRequiresUserAction:**NO**];
webview.allowsInlineMediaPlayback = true

```



###### WKWebView를 사용하는 경우 



WKWebviewConfiguration 

**allowsInlineMediaPlayback** = true
**mediaTypesRequiringUserActionForPlayback** = [] 혹은 .audio



```swift
let config = WKWebViewConfiguration()

config.allowsInlineMediaPlayback = true
config.mediaTypesRequiringUserActionForPlayback = [] 혹은 .audio

//mini의  muted=false 를 이용하려면 [] 를 설정해야함
```

