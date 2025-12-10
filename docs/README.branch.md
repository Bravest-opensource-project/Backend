## 🌿 Branch 전략 & 네이밍 규칙

### 🔹 Branch 종류

| Branch | 역할 | 설명 |
|--------|------|------|
| **main** | 배포용 브랜치 | 실제 서비스에 배포 가능한 안정 버전만 존재합니다. 개발자는 직접 작업하지 않습니다. |
| **develop** | 개발 통합 브랜치 | 기능 개발 브랜치들을 병합하는 브랜치입니다. 테스트 완료 후 `main`로 병합합니다. |
| **feature** | 기능 개발 브랜치 | 새 기능 개발 시 `develop`에서 분기하여 작업 후 `develop`에 병합합니다. |
| **release** | 배포 준비 브랜치 | 배포 전 테스트, 문서, 버그 수정 등을 진행하며 새로운 기능 추가는 하지 않습니다. |
| **hotfix** | 긴급 수정 브랜치 | 배포 후 발생한 버그를 수정할 때 `main`에서 분기 후 `main`, `develop`에 병합합니다. |

---

### 🧩 네이밍 규칙

| 종류 | 예시 | 규칙 |
|------|------|------|
| **main / develop** | 그대로 사용 | 특별한 접두사 없이 `main`, `develop` 사용 |
| **feature** | `feature/1-login-api`, `feature/2-home-ui` | `feature/{issue-number}-{feature-name}` 형식 추천 |
| **release** | `release-1.2`, `release-RB_1.7` | `release-버전명` 형식 사용 |
| **hotfix** | `hotfix-1.4.1` | 긴급 수정용 브랜치. `hotfix-버전명` 형식 사용 |

---

> 💡 **Tip for New Members**
> - 항상 `develop` 브랜치에서 작업을 시작하세요.  
> - 절대 `main` 브랜치에 직접 커밋하지 마세요.  
> - 브랜치 이름만 봐도 어떤 작업인지 한눈에 알 수 있도록 작성하세요.
