/**
 * MovieGuide 장르 선택 로직
 */
document.addEventListener('DOMContentLoaded', () => {
    const sections = document.querySelectorAll('.genre-grid');

    const preferGrid = sections[0];   // 선호하는 장르 그리드
    const dislikeGrid = sections[1];  // 싫어하는 장르 그리드
    const submitBtn = document.querySelector('button[type="submit"]');

    // 수정 전 상태 저장
    const getSnapshot = () => {
        const allBtns = document.querySelectorAll('.genre-btn');
        return Array.from(allBtns).map(btn => btn.classList.contains('active')); // [true, false, false...]
    };
    const initialState = getSnapshot().join(',');

    // 저장 버튼 비활성화로 시작
    submitBtn.disabled = true;

    const checkChanges = () => {
        const currentState = getSnapshot().join(',');
        // 바뀐 점 확인
        if (initialState === currentState) {
            submitBtn.disabled = true; // 변경 없음
        } else {
            submitBtn.disabled = false; // 변경 있음
        }
    };

    // 호불호 버튼 제어
    const setupGenreButtons = (currentGrid, opponentGrid) => {
        const buttons = currentGrid.querySelectorAll('.genre-btn');
        const opponentButtons = opponentGrid.querySelectorAll('.genre-btn');

        buttons.forEach((btn, index) => {
            // 클릭하기 전부터 활성화 되어있다면 반대편을 누를 수 없게 함
            if (btn.classList.contains('active')) {
                opponentButtons[index].disabled = true;
            }

            btn.addEventListener('click', () => {
                // 지금 버튼 active로
                btn.classList.toggle('active');

                // 반대편 버튼 비활성화
                const targetOpponent = opponentButtons[index];
                if (btn.classList.contains('active')) {
                    targetOpponent.disabled = true;
                } else {
                    targetOpponent.disabled = false;
                }

                // 버튼 누를 때마다 변경 사항이 있는지 체크
                checkChanges();
            });
        });
    };

    // 실제 함수 실행
    setupGenreButtons(preferGrid, dislikeGrid);
    setupGenreButtons(dislikeGrid, preferGrid);
});

document.getElementById('genreForm').addEventListener('submit', function(e) {
    const hiddenContainer = document.getElementById('hiddenInputs');
    hiddenContainer.innerHTML = ''; // 이전 제출 흔적 삭제

    const sections = document.querySelectorAll('.genre-grid');
    
    // 1. 선호 장르 수집 (첫 번째 그리드)
    sections[0].querySelectorAll('.genre-btn.active').forEach(btn => {
        const id = btn.getAttribute('data-id');
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'likeGenres'; // 서버에서 getParameterValues("likeGenres")로 받을 이름
        input.value = id;
        hiddenContainer.appendChild(input);
    });

    // 2. 싫어하는 장르 수집 (두 번째 그리드)
    sections[1].querySelectorAll('.genre-btn.active').forEach(btn => {
        const id = btn.getAttribute('data-id');
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'dislikeGenres'; // 서버에서 getParameterValues("dislikeGenres")로 받을 이름
        input.value = id;
        hiddenContainer.appendChild(input);
    });
    
    // 이 작업이 끝나면 폼이 정상적으로 서버에 제출됩니다.
});