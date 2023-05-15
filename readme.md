# Home Inventory System
이 프로젝트는 가정에서 생활용품 재고를 효과적으로 관리하기 위한 마이크로서비스 아키텍처(MSA) 기반의 시스템입니다. 개발 목적은 실제 사용 가능한 서비스를 개발하면서 동시에 스스로의 기술 연습을 위한 것입니다.
## Big Picture
![Big Picture](https://lucid.app/publicSegments/view/777f3362-1e64-446c-b515-e02f3b49f3d7/image.jpeg)
- 1단계
  - Hexagonal 구조의 적용이 가장 큰 목적
  - 가장기본적인 기능 재고의 구입,사용제공
  - Port와 Adapter 정의
  - 재고수량변경은 사용자서비스에서 직접 처리
- 2단계
  - 분산이벤트 스트리밍 플랫폼을 통한 MSA 구조적용
  - 재고수량을 조절하는 재고관리서비스 추가
