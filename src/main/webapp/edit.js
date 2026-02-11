document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('genreForm');
  if (!form) return;

  const preferGrid = document.getElementById('preferGrid');
  const dislikeGrid = document.getElementById('dislikeGrid');
  if (!preferGrid || !dislikeGrid) return;

  const preferChips = document.getElementById('preferChips');
  const dislikeChips = document.getElementById('dislikeChips');

  // ✅ JSP 저장 버튼 클래스는 .btn-primary (btn-save 아님)
  const submitBtn = form.querySelector('button[type="submit"]') || document.querySelector('.btn.btn-primary');

  const preferButtons = Array.from(preferGrid.querySelectorAll('.genre-btn'));
  const dislikeButtons = Array.from(dislikeGrid.querySelectorAll('.genre-btn'));

  // ====== 핵심: 현재 상태를 기준으로 반대편 disabled를 "일관되게" 다시 계산 ======
  const syncDisabled = () => {
    preferButtons.forEach((pBtn, i) => {
      const dBtn = dislikeButtons[i];
      if (!dBtn) return;

      const pOn = pBtn.classList.contains('active');
      const dOn = dBtn.classList.contains('active');

      // ✅ (방어) 둘 다 active면 prefer 우선으로 정리
      if (pOn && dOn) {
        dBtn.classList.remove('active');
      }

      // 다시 계산 (정리 후 상태로)
      const pActive = pBtn.classList.contains('active');
      const dActive = dBtn.classList.contains('active');

      if (pActive) {
        dBtn.disabled = true;
        dBtn.classList.remove('active');
        pBtn.disabled = false;
      } else if (dActive) {
        pBtn.disabled = true;
        pBtn.classList.remove('active');
        dBtn.disabled = false;
      } else {
        // 둘 다 선택 안 되어있으면 둘 다 활성
        pBtn.disabled = false;
        dBtn.disabled = false;
      }
    });
  };

  // ====== 칩 다시 그리기 ======
  const renderChips = () => {
    if (!preferChips || !dislikeChips) return;

    preferChips.innerHTML = '';
    dislikeChips.innerHTML = '';

    preferButtons.forEach((pBtn, i) => {
      const dBtn = dislikeButtons[i];

      const name = (pBtn.dataset.name || pBtn.textContent || '').trim();

      if (pBtn.classList.contains('active')) {
        const chip = document.createElement('span');
        chip.className = 'chip chip-like';
        chip.textContent = name;
        preferChips.appendChild(chip);
      } else if (dBtn && dBtn.classList.contains('active')) {
        const chip = document.createElement('span');
        chip.className = 'chip chip-dislike';
        chip.textContent = name;
        dislikeChips.appendChild(chip);
      }
    });
  };

  // ====== 클릭 이벤트 ======
  const bindClicks = (buttons, isPreferSide) => {
    buttons.forEach((btn, i) => {
      btn.addEventListener('click', (e) => {
        e.preventDefault();
        if (btn.disabled) return;

        // 현재 반대편 버튼
        const opp = isPreferSide ? dislikeButtons[i] : preferButtons[i];

        // ✅ 토글: 켜기/끄기
        const willTurnOn = !btn.classList.contains('active');

        if (willTurnOn) {
          btn.classList.add('active');

          // 반대편은 끄고 disabled
          if (opp) {
            opp.classList.remove('active');
            opp.disabled = true;
          }
        } else {
          // 끄기
          btn.classList.remove('active');

          // 반대편 disabled 풀기
          if (opp) opp.disabled = false;
        }

        // 상태 재정렬 + 칩 갱신
        syncDisabled();
        renderChips();

        // (선택) 저장 버튼 비활성 로직 넣고 싶으면 여기서 처리
        // if (submitBtn) submitBtn.disabled = false;
      });
    });
  };

  bindClicks(preferButtons, true);
  bindClicks(dislikeButtons, false);

  // ====== 초기 로딩 반영 ======
  syncDisabled();
  renderChips();

  // ====== submit 직전에 hidden input 구성 ======
  form.addEventListener('submit', () => {
    const hiddenContainer = document.getElementById('hiddenInputs');
    if (!hiddenContainer) return;

    hiddenContainer.innerHTML = '';

    preferButtons.forEach((pBtn, i) => {
      const genreId = pBtn.dataset.id;
      const dBtn = dislikeButtons[i];

      if (!genreId) return;

      if (pBtn.classList.contains('active')) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'likeGenres';
        input.value = genreId;
        hiddenContainer.appendChild(input);
      } else if (dBtn && dBtn.classList.contains('active')) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'dislikeGenres';
        input.value = genreId;
        hiddenContainer.appendChild(input);
      }
    });
  });
});
