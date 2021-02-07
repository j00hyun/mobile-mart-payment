# mobile-mart-payment
비교적 제품 수가 적고 간단한 '노브랜드'를 타겟으로 제작하였습니다.
마트에서 계산을 위해 줄을 서지 않아도 되며 소비자가 직접 결제까지 가능합니다. 지점 관리자는 웹 대시보드 형식으로 지점 관리가 가능합니다.

<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/107122226-cd826980-68d9-11eb-9d5a-889aae709081.png" width="30%" alt="mobile-mart-payment">
</p>

디자인과 안드로이드 Repository는 아래 링크에서 확인해주세요.
- 디자인
  - [안드로이드 앱](https://xd.adobe.com/view/f11772d9-a4b1-427f-8b61-4a24e196a001-a444/screen/8e507592-33b2-4513-a61a-eebeac49b42e/)
  - [관리자 웹 대시보드](https://xd.adobe.com/view/5a5ad9ad-2b47-4da9-a4fb-87706e78c95e-0f09/screen/d18af14d-6e21-4f99-9d99-16e853175c43/)
- [안드로이드 Repository](https://github.com/ggj0418/Android)

# 시연 영상
GIF 준비중입니다. 

안드로이드 앱>   
   
관리자 웹 대시보드>

# 주요 기능   
총 40개의 API가 구현되었습니다. 정리된 API문서는 팀에게만 공개되어있으며 그 중 핵심 기능만 README에 나열합니다.   

**고객** - 안드로이드 App
- 로컬 및 소셜 로그인이 가능
- 상품 바코드 스캔을 통해 장바구니에 담을 수 있음
- 장바구니 결제 가능
   
**지점 관리자** - 관리자 웹 대시보드
- 유저 및 상품 관리가 가능
- 지정한 최소 재고량에 따라 본사에 자동 재고 요청
- 일, 주, 월 단위로 매출 현황 확인 가능

# ERD
<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/107119380-804acb80-68ca-11eb-99af-1a7b479947da.png" width="80%" alt="ERD">
  <img src="https://user-images.githubusercontent.com/43433753/107119799-3f07eb00-68cd-11eb-9715-bce5876298de.png" width="80%" alt="DB Columns">
</p>


# 서버 아키텍처   
무중단 scale-out이 가능한 blue/green 배포전략으로 구성했습니다.   
<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/106710721-ca694e00-6639-11eb-983f-ce37f5a7015a.png" width="80%" alt="server architecture">
</p>

# CI·CD   
Github에 소스코드를 PUSH 혹은 PR하면 Jenkins가 소스코드를 가져와서 빌드합니다. 빌드 결과를 프로젝트 팀 Slack으로 알리고 배포 스크립트를 통해 서버에 배포합니다. 여러 개의 도커 컨테이너를 관리하기 위해 docker compose를 사용했습니다.   
<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/106710744-d48b4c80-6639-11eb-901e-7fcc2e247ca7.png" width="80%" alt="ci-cd architecture">
</p>

# 개발환경
<details>
  <summary><strong>프론트엔드</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - React   
  - Graphql
  - Apollo Client
  - Material-UI
</details>
<details>
  <summary><strong>안드로이드</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - RecyclerView   
  - Retrofit2
  - HttpLoggingInterceptor
  - WebViewClient
  - Google Vision
  - BarcodeGraphic
  - CameraSource
  - GraphicOverlay
  - SharedPreference
  - 아임포트
</details>
<details>
  <summary><strong>백엔드</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - Spring Security   
  - Spring Cloud
  - Oauth2 Client
  - jjwt
  - Graphql
  - nurigo
  - redis
  - MySQL(RDS)
  - Amazon S3
  - Swagger2 (정리된 API문서본은 별도로 팀 노션으로 관리)
  - nginx
  - jenkins
  - docker
</details>

# 팀원
**전체**
<table>
  <tbody>
    <tr>
      <td align="center" colspan="2" width="100px"><strong>디자인</strong></td>
      <td align="center" colspan="2" width="100px"><strong>프론트엔드</strong></td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/limhyoyeon">
          <img src="https://avatars.githubusercontent.com/u/75024589?s=400&v=4" width="100px" alt="limhyoyeon" style="max-width:100%;">
          <br>
          <sub>
            <b>임효연</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/cho-hyerim">
          <img src="https://avatars.githubusercontent.com/u/61765403?s=400&v=4" width="100px" alt="cho-hyerim" style="max-width:100%;">
          <br>
          <sub>
            <b>조혜림</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/Kimjongsoo">
          <img src="https://avatars.githubusercontent.com/u/31787166?s=400&v=4" width="100px" alt="Kimjongsoo" style="max-width:100%;">
          <br>
          <sub>
            <b>김종수</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/chaeyeonp">
          <img src="https://avatars.githubusercontent.com/u/61309080?s=400&u=f5fdeef35aafb099f0d20fc5354b3a8693500dd9&v=4" width="100px" alt="chaeyeonp" style="max-width:100%;">
          <br>
          <sub>
            <b>박채연</b>
          </sub>
        </a>
        <br>
      </td>
    </tr>
    <tr>
      <td align="center" colspan="2" width="100px"><strong>안드로이드</strong></td>
      <td align="center" colspan="2" width="100px"><strong>백엔드</strong></td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/ggj0418">
          <img src="https://avatars.githubusercontent.com/u/44552228?s=400&u=2cee25692409f1811912e78bde83a18d1643ec26&v=4" width="100px" alt="ggj0418" style="max-width:100%;">
          <br>
          <sub>
            <b>이현준</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/gamjacode">
          <img src="https://avatars.githubusercontent.com/u/73874976?s=400&v=4" width="100px" alt="gamjacode" style="max-width:100%;">
          <br>
          <sub>
            <b>김형주</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/alexjime">
          <img src="https://avatars.githubusercontent.com/u/43433753?s=400&u=26699d33e38dff51974fb11090397b61db387d4c&v=4" width="100px" alt="alexjime" style="max-width:100%;">
          <br>
          <sub>
            <b>지민수</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/j00hyun">
          <img src="https://avatars.githubusercontent.com/u/64248514?s=400&u=fa1594317b332f9f8a2ada403bc0b5a06c1fcd2c&v=4" width="100px" alt="j00hyun" style="max-width:100%;">
          <br>
          <sub>
            <b>박주현</b>
          </sub>
        </a>
        <br>
      </td>
    </tr>
  </tbody>
</table>
