### 깃 코드 클론
```shell
git clone git@github.com:puppypawpaw/Pawpaw_project_BE.git
```
<br>

### 백엔드 루트 디렉토리 이동
```shell
cd Pawpaw_project_BE
```
<br>

### mysql, redis 실행
```shell
docker compose -f docker/docker-compose.yml up -d
```
<br>

### 빌드
```shell
./gradlew clean build
```
<br>

### jar 실행
```shell
java -jar build/libs/pawpaw_project_BE-0.0.1-SNAPSHOT.jar
```