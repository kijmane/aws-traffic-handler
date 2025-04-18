## High Traffic Load Performance Test
- 총 1,000,000건의 매니저 등록 요청 처리 및 로그 저장
- 소요 시간: 약 70초
- CPU : 일정하게 유지, 과부하 없음
- Memory : 지속 증가 → GC 튐 없이 안정적 종료
- 테스트 목적 : 대량 요청 처리 중 서비스 안정성과 DB 기록 처리 성능 확인
<img width="1286" alt="스크린샷 2025-03-28 오후 8 22 18" src="https://github.com/user-attachments/assets/fc048f27-ce9d-430e-8ec2-623941e1bf19" />

## AWS
### EC2
<img width="1434" alt="스크린샷 2025-03-14 오후 2 10 12" src="https://github.com/user-attachments/assets/999c0fbe-7ed2-47cb-af47-23906263ac21" />

### RDS
<img width="1440" alt="RDS 데이터베이스 생성" src="https://github.com/user-attachments/assets/443128dd-f153-4b68-aa93-95317c05714b" />

### S3
<img width="1069" alt="스크린샷 2025-03-14 오후 2 58 21" src="https://github.com/user-attachments/assets/cf5a4823-7857-46c8-846d-1e3beaa150fc" />

### Health Check API
> 배포 후 서버가 정상 작동 중일 때 /health 엔드포인트에 접속한 결과입니다.
<img width="1207" alt="스크린샷 2025-03-14 오후 2 15 14" src="https://github.com/user-attachments/assets/99684801-e229-4fee-8439-976f9590e3fc" />

### Kotlin Conversion
- 기존 Java 기반 프로젝트를 Kotlin으로 전환
- Kotlin의 기본 문법과 데이터 클래스 활용
- QueryDSL, JPA 환경에서도 정상 동작하도록 Gradle 설정 조정

### Applying Transactions
- 예외(Connection is read-only)를 분석하고 트랜잭션의 읽기 전용 속성 설정 문제임을 파악하여 수정
- 로그 기록과 주요 비즈니스 로직이 서로 영향을 주지 않도록 REQUIRES_NEW 옵션을 활용하여 트랜잭션 분리 경험

### JPA/QueryDSL
- QueryDSL을 활용해 기존 JPQL 쿼리를 리팩토링하며 가독성 향상과 N+1 문제 예방을 위한 fetch join 적용

### Improve Performance
- 연관 엔티티 조회 시 발생하는 N+1 문제를 분석하고 즉시 로딩 및 최적화된 쿼리로 개선

### Troubleshooting AOP Problems
- 특정 메서드 실행 전 로깅이 동작하지 않던 문제를 분석하고 Poincut 조건 수정 및 Advice 위치 조정을 통해 정상 동작하도록 개선

### Improving the authentication structure
- 기존 JWT 필터 기반 인증 코드를 Spring Security 기반으로 전환
- UserDetailsService, JWT 필터 커스터마이징을 통해 인증 및 권한 관리 로직을 Spring Security 구조에 맞게 재정비

### Test code refactoring
- 컨트롤러 단위 테스트가 실패하는 상황을 분석하고 예외 발생 케이스를 고려한 Mock 데이터 및 검증 로직 수정
- 실제 예외 메시지와 상태 코드까지 정확히 검증하는 테스트 코드로 보완

### JPA Related Relationships and Cascade Practice
- 할 일을 생성한 사용자가 자동으로 담당자로 등록되도록 Cascade 설정 및 연관 관계 매핑 구조 개선
