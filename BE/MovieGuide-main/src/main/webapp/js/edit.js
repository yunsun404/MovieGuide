document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('genreForm');
    const sections = document.querySelectorAll('.genre-grid');
    const preferGrid = sections[0];
    const dislikeGrid = sections[1];
    const submitBtn = document.querySelector('button[type="submit"]');
	
	// 저장 버튼 활성을 위해 추가
	let initialStates = "";
    const getSnapshot = () => {
        const preferButtons = preferGrid.querySelectorAll('.genre-btn');
        const dislikeButtons = dislikeGrid.querySelectorAll('.genre-btn');
        let snapshot = "";
        
        preferButtons.forEach((btn, index) => {
            const dislikeBtn = dislikeButtons[index];
            if (btn.classList.contains('active')) snapshot += "L"; // 선호
            else if (dislikeBtn.classList.contains('active')) snapshot += "D"; // 불호
            else snapshot += "N"; // 선택 안됨
        });
        return snapshot;
    };

    // 장르 종류 버튼 누를 때, 선호와 불호 둘 중 하나만 누를 수 있도록
    const setupGenreButtons = (currentGrid, opponentGrid) => {
        const buttons = currentGrid.querySelectorAll('.genre-btn');
        const opponentButtons = opponentGrid.querySelectorAll('.genre-btn');

        buttons.forEach((btn, index) => {
            btn.addEventListener('click', () => {
                btn.classList.toggle('active');
                
                // 반대편 버튼 비활성화
                const targetOpponent = opponentButtons[index];
                if (btn.classList.contains('active')) {
                    targetOpponent.disabled = true;
                    targetOpponent.classList.remove('active');
                } else {
                    targetOpponent.disabled = false;
                }
                
				// [수정] 클릭할 때마다 초기 상태와 비교해서 버튼 활성화/비활성화
                const currentStatus = getSnapshot();
                submitBtn.disabled = (initialStates === currentStatus);
                
                // 시각적 피드백 (선택사항)
                submitBtn.style.opacity = submitBtn.disabled ? "0.5" : "1";
            });
        });
    };

    setupGenreButtons(preferGrid, dislikeGrid);
    setupGenreButtons(dislikeGrid, preferGrid);
	
	// 유저가 전에 선택했던 사항들을 가져오기
	const initButtonStates = () => {
        const preferButtons = preferGrid.querySelectorAll('.genre-btn');
        const dislikeButtons = dislikeGrid.querySelectorAll('.genre-btn');

        preferButtons.forEach((btn, index) => {
            const dislikeBtn = dislikeButtons[index];

            // 좋아요가 이미 active라면 싫어요를 비활성화
            if (btn.classList.contains('active')) {
                dislikeBtn.disabled = true;
            } 
            // 싫어요가 이미 active라면 좋아요를 비활성화
            else if (dislikeBtn.classList.contains('active')) {
                btn.disabled = true;
            }
        });
    };

    initButtonStates(); // 페이지 로드하자마자 실행
	initialStates = getSnapshot();
    submitBtn.disabled = true;
    submitBtn.style.opacity = "0.5";

    // 사용자가 변경한 데이터
    form.addEventListener('submit', function(e) {
        // console.log("폼 전송 시작 - 데이터 수집 중...");

        const hiddenContainer = document.getElementById('hiddenInputs');
        hiddenContainer.innerHTML = ''; // 초기화

		// 모든 장르 버튼을 가져오기 (모든 장르 종류만 있으면 되서 선호쪽을 가져옴)
	    const preferButtons = preferGrid.querySelectorAll('.genre-btn');
	    const dislikeButtons = dislikeGrid.querySelectorAll('.genre-btn');

	    preferButtons.forEach((btn, index) => {
	        const genreId = btn.getAttribute('data-id');
	        const dislikeBtn = dislikeButtons[index];
	        
	        const input = document.createElement('input');
	        input.type = 'hidden';
	        
	        if (btn.classList.contains('active')) {
	            input.name = 'likeGenres'; 
	            input.value = genreId;
	        } else if (dislikeBtn.classList.contains('active')) {
	            input.name = 'dislikeGenres';
	            input.value = genreId;
	        } else {
	            input.name = 'neutralGenres'; 
	            input.value = genreId;
	        }
	        
	        hiddenContainer.appendChild(input);
	    });

	    // console.log("전체 데이터 매핑 완료!");
    });
});