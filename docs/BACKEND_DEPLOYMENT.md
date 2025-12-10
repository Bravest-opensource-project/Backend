## 개요
당 문서는 Bravest 백엔드를 컨테이너 이미지로 패키징하는 `Dockerfile.backend`에 대해 설명합니다.

작성자 @semi-yu

## 상세

### 사전 지식

자바 애플리케이션은 배포 전에 먼저 바이트코드(JAR)로 컴파일되어야 합니다. 이 과정에서 의존성 해소 등을 위해 Gradle이 관여하지만, 이 프로젝트에서는 `Dockerfile.backend`가 `./gradlew bootJar`를 실행하도록 설정되어 있으므로 별도로 신경 쓸 필요는 없습니다.

다만, 이 컴파일(빌드) 과정을 전용 Dockerfile로 따로 분리하면 설정이 중복되거나 빌드 파이프라인이 복잡해질 수 있습니다. 따라서 하나의 Dockerfile에서 **빌드 스테이지와 실행 스테이지를 모두 정의하는 멀티 스테이지 빌드**를 사용합니다. 이를 통해 어플리케이션 JAR 파일을 생성한 뒤, 실행에 필요한 JDK(내부에 JRE 포함)만 포함하는 컨테이너 이미지를 바로 만들 수 있으며, 결과 이미지 크기 또한 줄일 수 있습니다.

### 기타 통제 사항
`Dockerfile.backend`는 여러분이 요청하신 대로 21 버전의 JDK를 사용하도록 정의하였습니다. 배포판은 Temurin을 사용합니다.
### 실행

한편, 멀티 스테이지 빌드는 **이미지 빌드 시점의 구조**에만 영향을 줄 뿐, 이후 컨테이너를 실행하는 과정 자체에는 특별한 차이를 만들지 않습니다. 아래 절차를 따르면 됩니다.

#### 이미지를 빌드하기 위해서…

- 작업 디렉토리를 `Dockerfile.backend`가 존재하는 프로젝트 루트로 이동한 뒤, 다음 명령어를 실행합니다:

```bash
docker build \
  -f Dockerfile.backend \
  -t bravest-backend \
  .
```
위 명령어는 `Dockerfile.backend`를 참조하여 백엔드 애플리케이션 이미지를 빌드합니다.

#### 컨테이너 인스턴스를 실행하기 위해서...

- `.env` 파일을 `Dockerfile.backend`가 존재하는 경로에 두고, 다음 명령어를 실행합니다:
```bash
docker run -d \
  --name bravest-backend \
  -p 8080:8080 \
  --env-file .env \
  bravest-backend
```
- 위 명령어는 빌드된 bravest-backend 이미지를 기반으로 컨테이너 인스턴스를 실행합니다.
- 컨테이너 인스턴스가 외부 DB에 접속하기 위해 필요한 접속 정보는 `.env` 파일에 정의되어 있어야 하며, `--env-file .env` 옵션을 통해 컨테이너에 주입됩니다.
  - 이를 통해 중요한 정보를 배포 환경의 파일 시스템에 저장하지 않고 환경 변수로 주입할 수 있습니다.
  - 해당 파일을 얻으려면 팀원에게 연락해주세요.
#### 현재는...
- 스프링 부트로 작성된 백엔드의 Dockerfile만 정의되어 있습니다.
- 가까운 시일 내에, 전체 서비스를 정의하는 `docker-compose.yml`를 정의하겠습니다.
### 요약

#### 빌드 및 실행
```bash
# 빌드
docker build -f Dockerfile.backend -t bravest-backend .

# 실행
docker run -d --name bravest-backend -p 8080:8080 --env-file .env bravest-backend

```
- 사전 조건
  - 프로젝트 루트에 `.env` 파일이 있어야 합니다. 파일이 없다면 팀원에게 요청하세요.