# mobile-mart-payment
This project targets No Brand, a retail store with a relatively small and simple product selection.
Customers can complete purchases using their smartphones without waiting in checkout lines.
Store managers can manage each branch through a web-based dashboard.

<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/107122226-cd826980-68d9-11eb-9d5a-889aae709081.png" width="30%" alt="mobile-mart-payment">
</p>

You can view the app and admin dashboard designs via the following links:
- Design Prototypes
  - [Android App](https://xd.adobe.com/view/f11772d9-a4b1-427f-8b61-4a24e196a001-a444/screen/8e507592-33b2-4513-a61a-eebeac49b42e/)
  - [Admin Web Dashboard](https://xd.adobe.com/view/5a5ad9ad-2b47-4da9-a4fb-87706e78c95e-0f09/screen/d18af14d-6e21-4f99-9d99-16e853175c43/)
- [Android GitHub Repository](https://github.com/ggj0418/Android)

# Key Features   
A total of 40 APIs were implemented. While the full API documentation is shared only with team members, the core functionalities are summarized here:   

**Customer** – Android App
- Supports both local and social logins
- Allows customers to scan product barcodes to add items to the cart
- Enables checkout directly within the app

   
**Branch Manager** – Admin Web Dashboard
- Manage users and products
- Automatically requests inventory restocks from HQ when levels drop below threshold
- Provides daily, weekly, and monthly sales analytics


# ERD
<p align="center">
  <img src="https://user-images.githubusercontent.com/64248514/108815245-7b3e8980-75f7-11eb-8785-e8d9cffc4b11.png" width="80%" alt="ERD">
  <img src="https://user-images.githubusercontent.com/64248514/108815255-7e397a00-75f7-11eb-841a-c0e73bb70695.png" width="80%" alt="DB Columns">
</p>


# Server Architecture   
We adopted a blue/green deployment strategy for seamless and zero-downtime scaling.   
<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/106710721-ca694e00-6639-11eb-983f-ce37f5a7015a.png" width="80%" alt="server architecture">
</p>

# CI/CD Pipeline  
When code is pushed or a pull request is made to GitHub, Jenkins pulls the code, builds the project, sends a notification to our Slack channel, and deploys it to the server using automated deployment scripts. We used Docker Compose to manage multiple Docker containers.
<p align="center">
  <img src="https://user-images.githubusercontent.com/43433753/106710744-d48b4c80-6639-11eb-901e-7fcc2e247ca7.png" width="80%" alt="ci-cd architecture">
</p>

# Development Environment
<details>
  <summary><strong>Frontend</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - React   
  - Graphql
  - Apollo Client
  - Material-UI
</details>
<details>
  <summary><strong>Android</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - RecyclerView   
  - Retrofit2
  - HttpLoggingInterceptor
  - WebViewClient
  - Google Vision
  - BarcodeGraphic
  - CameraSource
  - GraphicOverlay
  - SharedPreference
  - I'mport (payment API)
</details>
<details>
  <summary><strong>Backend</strong></summary> <!-- summary 아래 한칸 공백 두고 내용 삽입 -->
                 
  - Spring Security   
  - Spring Cloud
  - Oauth2 Client
  - jjwt
  - Graphql
  - nurigo
  - redis
  - MySQL(RDS)
  - Amazon S3
  - Swagger2 (API documentation managed separately in team Notion)
  - nginx
  - jenkins
  - docker
</details>

# Team Members
**Overall Roles**
<table>
  <tbody>
    <tr>
      <td align="center" colspan="2" width="100px"><strong>Design</strong></td>
      <td align="center" colspan="2" width="100px"><strong>Frontend</strong></td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/limhyoyeon">
          <img src="https://avatars.githubusercontent.com/u/75024589?s=400&v=4" width="100px" alt="limhyoyeon" style="max-width:100%;">
          <br>
          <sub>
            <b>Lim Hyo-yeon</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/cho-hyerim">
          <img src="https://avatars.githubusercontent.com/u/61765403?s=400&v=4" width="100px" alt="cho-hyerim" style="max-width:100%;">
          <br>
          <sub>
            <b>Cho Hye-rim</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center" colspan="2">
        <a href="https://github.com/chaeyeonp">
          <img src="https://avatars.githubusercontent.com/u/61309080?s=400&u=f5fdeef35aafb099f0d20fc5354b3a8693500dd9&v=4" width="100px" alt="chaeyeonp" style="max-width:100%;">
          <br>
          <sub>
            <b>Chae-yeon Park</b>
          </sub>
        </a>
        <br>
      </td>
    </tr>
    <tr>
      <td align="center" colspan="2" width="100px"><strong>Android</strong></td>
      <td align="center" colspan="2" width="100px"><strong>Backend</strong></td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/ggj0418">
          <img src="https://avatars.githubusercontent.com/u/44552228?s=400&u=2cee25692409f1811912e78bde83a18d1643ec26&v=4" width="100px" alt="ggj0418" style="max-width:100%;">
          <br>
          <sub>
            <b>Hyun-jun Lee</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/gamjacode">
          <img src="https://avatars.githubusercontent.com/u/73874976?s=400&v=4" width="100px" alt="gamjacode" style="max-width:100%;">
          <br>
          <sub>
            <b>Hyung-joo Kim</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/alexjime">
          <img src="https://avatars.githubusercontent.com/u/43433753?s=400&u=26699d33e38dff51974fb11090397b61db387d4c&v=4" width="100px" alt="alexjime" style="max-width:100%;">
          <br>
          <sub>
            <b>Min-su Ji</b>
          </sub>
        </a>
        <br>
      </td>
      <td align="center">
        <a href="https://github.com/j00hyun">
          <img src="https://avatars.githubusercontent.com/u/64248514?s=400&u=fa1594317b332f9f8a2ada403bc0b5a06c1fcd2c&v=4" width="100px" alt="j00hyun" style="max-width:100%;">
          <br>
          <sub>
            <b>Joo-hyun Park</b>
          </sub>
        </a>
        <br>
      </td>
    </tr>
  </tbody>
</table>
