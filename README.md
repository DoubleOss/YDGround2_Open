# 양띵 TV 배틀그라운드 패러디 콘텐츠 모드

## 📝│개요
> ### FPS 패러디
>
> ### 제작기간  2023/09 ~ 2023/11/10
> ### 사용기술: Java, JDA(Java Discord Api), Forge Api, Reflection
> 
> ## 💬│ 콘텐츠 방송 편집본
> ![Video Label](http://img.youtube.com/vi/XqWWxHKpVL8/0.jpg)
>
> (https://youtu.be/XqWWxHKpVL8)


## 👨🏻‍💻│기능 구현 목차
###   1. [OpenGl 자기장 위치 게임 맵 HUD Render](https://github.com/DoubleOss/YDGround2_Open?tab=readme-ov-file#1-opengl-%EC%9E%90%EA%B8%B0%EC%9E%A5-%EC%9C%84%EC%B9%98-%EA%B2%8C%EC%9E%84-%EB%A7%B5-%EC%97%B0%EB%8F%99)
###   2. [스쿼드 멤버 플레이 편의 기능](https://github.com/DoubleOss/YDGround2_Open?tab=readme-ov-file#2-%EC%8A%A4%EC%BF%BC%EB%93%9C-%EB%A9%A4%EB%B2%84-%ED%94%8C%EB%A0%88%EC%9D%B4-%ED%8E%B8%EC%9D%98-%EA%B8%B0%EB%8A%A5-1)
###   3. [OpenGL Infinite Scrolling Texture 구현](https://github.com/DoubleOss/YDGround2_Open?tab=readme-ov-file#3-opengl-infinite-scrolling-texture-%EA%B5%AC%ED%98%84-1)
###   4. [마지막 생존자 승리 연출](https://github.com/DoubleOss/YDGround2_Open?tab=readme-ov-file#4-%EB%A7%88%EC%A7%80%EB%A7%89-%EC%83%9D%EC%A1%B4%EC%9E%90-%EC%8A%B9%EB%A6%AC-%EC%97%B0%EC%B6%9C-1)

***

### 1. OpenGl 자기장 위치 게임 맵 연동
> * ### 2D 텍스쳐 맵 Image 위 OpenGL Line Render
> * #### 시연 GIF
> ![2024-10-16 13;34;16](https://github.com/user-attachments/assets/f1b8875b-c267-4750-ac55-a9c0f120ddfd)
> ## 🔗 코드 링크
> * ### [OpenGL drawSquare 함수](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L809)
> * ### [MAP Hud 적용 부분](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L714)
> * ### [스쿼드 Ping 렌더 ](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L669)

***

### 2. 스쿼드 멤버 플레이 편의 기능
> * ### 스쿼드 간 맵 핑 공유
> * #### 스쿼드 멤버 체력, 상태 공유, 스쿼드 멤버 Glow Effect
> ![2024-10-16 15;31;42](https://github.com/user-attachments/assets/76f86ca7-b97b-444e-b4a5-a34163735b09)
> ![2024-10-16 15;33;21](https://github.com/user-attachments/assets/e198e619-2b7d-43e4-8dd8-9aef0a04b30b)
> ## 🔗 코드 링크
> * ### [핑 클릭시 값 전송 함수](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L265)
> * ### [서버측 핑 값 패킷 도착시 해당 멤버에게 전송 부분](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/packet/SPacketSendPingPos.java#L62)
> * ### [스쿼드 게임시 멤버에게 자신의 체력 값 전송 부분 ](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L295)
>

***

### 3. OpenGL Infinite Scrolling Texture 구현
> * ### OpenGL Textrue U 좌표 값과 Player Yaw 대응한 나침반 HUD
> ![2024-10-16 13;17;40](https://github.com/user-attachments/assets/7905a43b-9ae7-4482-b03b-02be11e24626)
> ## 🔗 코드 링크
> * ### [openGl Texutre UV 값 조절 활용 코드](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/proxy/ClientProxy.java#L560)

***

### 4. 마지막 생존자 승리 연출
> * ### Tween Animation Alpha 적용
> ![2024-10-17 13;49;16](https://github.com/user-attachments/assets/e4116347-d69d-4e54-a41c-bc28449e9174)
> ## 🔗 코드 링크
> * ### [Easing functions Animation](https://github.com/DoubleOss/YDGround2_Open/blob/main/src/main/java/com/doubleos/yd/gui/GuiFriendship.java#L108)
