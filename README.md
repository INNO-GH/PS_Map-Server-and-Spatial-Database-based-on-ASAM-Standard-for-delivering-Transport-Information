## Access 
- AWS에 배포된 서버의 URL로 접근

## Collector 
- OpenDRIVE 파일을 서버 및 데이터베이스에 업로드
    1. [파일선택]에서 OpenDRIVE 파일을 선택
    2. [Send]를 클릭해 업로드
- 입력 좌표에 따라 해당 도로에 상황을 추가하기
    1. [Coordinate]에 좌표를 '위도값, 경도값' 형식으로 입력 (밑의 지도를 클릭하면 자동으로 값이 지정)
    2. [Situation]에 해당 위치에서 발생한 상황을 입력
    3. [Send]를 클릭해 추가하기

## User
- 입력 좌표를 포함하는 OpenDRIVE 파일을 다운로드    
    1. [선택리스트]에서 값을 Yes로 선택
    2. [Coordinate]에 좌표를 '위도값, 경도값' 형식으로 입력 (밑의 지도를 클릭하면 자동으로 값이 지정)
    3. [Receive]를 클릭해 다운로드
- 입력 좌표에 따른 해당 도로 정보를 가져오기
    1. [선택리스트]에서 값을 No로 선택
    2. [Coordinate]에 좌표를 '위도값, 경도값' 형식으로 입력 (밑의 지도를 클릭하면 자동으로 값이 지정)
    3. [Receive]를 클릭해 가져오기

## Note
- 잘못된 파일 형식이나 입력 좌표의 경우, 정보가 업로드되지 않음
- 입력 좌표가 어느 도로에도 속하지 않는 경우, 정보가 출력되지 않거나 오류창 발생