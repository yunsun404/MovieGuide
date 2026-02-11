const API_RECOMMEND = "/MovieGuide/recommend";
const API_WISHLIST = "/MovieGuide/api/wishlist";
const IMG_BASE = "https://image.tmdb.org/t/p/w500";

const gridEl = document.getElementById("movieGrid");
const msgEl = document.getElementById("messageBox");

let currentMovies = [];
let wishedSet = new Set(); // ✅ 내가 위시한 movieId 집합

async function loadUserId() {
  try {
    const res = await fetch("/MovieGuide/api/me", { credentials: "include" });

    if (res.status === 401) {
      // 로그인 안 됨
      const el = document.getElementById("userIdText");
      if (el) el.textContent = "";
      return;
    }

    const data = await res.json();
    const el = document.getElementById("userIdText");
    if (el) el.textContent = data.userId;  // ⭐ 여기서 화면에 들어감
  } catch (e) {
    console.error("api/me 실패", e);
  }
}

function setMsg(text) {
  if (!msgEl) return;
  if (!text) {
    msgEl.style.display = "none";
    msgEl.textContent = "";
    return;
  }
  msgEl.style.display = "block";
  msgEl.textContent = text;
}

function posterUrl(poster_path) {
  if (!poster_path) return "";
  if (poster_path.startsWith("http")) return poster_path;
  if (poster_path.startsWith("/")) return IMG_BASE + poster_path;
  return IMG_BASE + "/" + poster_path;
}

function isWished(movieId) {
  return wishedSet.has(String(movieId));
}

function card(movie) {
  const title = movie.title || "";
  const original = movie.original_title || "";
  const vote = Number(movie.vote_average || 0);
  const release = movie.release_date || "";
  const year = release.length >= 4 ? release.substring(0, 4) : "";

  const poster = posterUrl(movie.poster_path);
  const posterHtml = poster
    ? `<img src="${poster}" alt="${title}">`
    : `<div class="no-poster">No Poster</div>`;

  const wished = isWished(movie.id);

  // ✅ 체크된 상태면 아이콘/클래스 바꿈
  const btnClass = wished ? "add-wishlist wished" : "add-wishlist";
  const icon = wished ? "fa-solid fa-check" : "fa-solid fa-plus";

  return `
    <div class="movie-card" data-movie-id="${movie.id}">
      <div class="poster-area">
        ${posterHtml}
        <button class="${btnClass}" type="button" data-action="toggle-wish" data-movie-id="${movie.id}">
          <i class="${icon}"></i>
        </button>
      </div>

      <div class="movie-details">
        <h3 class="m-title">${title}</h3>
        <p class="m-eng">${original}</p>

        <div class="m-meta">
          <span class="m-rating">
            <i class="fa-solid fa-star"></i> ${vote.toFixed(1)}
          </span>
          <span class="m-year">${year}</span>
        </div>
      </div>
    </div>
  `;
}

// ✅ 1) 내 위시리스트 movieId 목록 가져오기
async function loadWishedSet() {
  const res = await fetch(API_WISHLIST, { credentials: "include" });

  // 로그인 안 된 상태면 401 올 수 있음
  if (res.status === 401) {
    wishedSet = new Set();
    return;
  }

  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.ok === false) {
    throw new Error(data.error || `wishlist 조회 실패 (HTTP ${res.status})`);
  }

  const ids = Array.isArray(data.movieIds) ? data.movieIds : [];
  wishedSet = new Set(ids.map(String));
}

// ✅ 2) 추천 영화 가져오기
async function loadRecommend() {
  const res = await fetch(API_RECOMMEND, { credentials: "include" });

  if (res.status === 401) {
    setMsg("로그인이 필요합니다. 로그인 후 이용해주세요.");
    gridEl.innerHTML = "";
    return;
  }

  if (!res.ok) {
    setMsg(`추천 API 실패 (HTTP ${res.status})`);
    gridEl.innerHTML = "";
    return;
  }

  const movies = await res.json().catch(() => []);
  currentMovies = Array.isArray(movies) ? movies : [];

  if (currentMovies.length === 0) {
    setMsg("추천할 영화가 없습니다.");
    gridEl.innerHTML = "";
    return;
  }

  setMsg("");
  gridEl.innerHTML = currentMovies.map(card).join("");
}

// ✅ 3) 위시 추가
async function addWish(movieId) {
  const res = await fetch(API_WISHLIST, {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ movieId: Number(movieId) }),
  });

  if (res.status === 401) throw new Error("로그인이 필요합니다.");

  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.ok === false) {
    throw new Error(data.error || `위시 저장 실패 (HTTP ${res.status})`);
  }
  return data;
}

// ✅ 4) 위시 삭제
async function deleteWish(movieId) {
  const res = await fetch(`${API_WISHLIST}?movieId=${encodeURIComponent(movieId)}`, {
    method: "DELETE",
    credentials: "include",
  });

  if (res.status === 401) throw new Error("로그인이 필요합니다.");

  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.ok === false) {
    throw new Error(data.error || `위시 삭제 실패 (HTTP ${res.status})`);
  }
  return data;
}

// ✅ 5) 토글 클릭 이벤트
gridEl.addEventListener("click", async (e) => {
  const btn = e.target.closest("button[data-action='toggle-wish']");
  if (!btn) return;

  const movieId = btn.dataset.movieId;
  if (!movieId) return;

  try {
    btn.disabled = true;

    // 현재 상태 확인
    const wished = isWished(movieId);

    if (!wished) {
      await addWish(movieId);
      wishedSet.add(String(movieId));
    } else {
      await deleteWish(movieId);
      wishedSet.delete(String(movieId));
    }

    // ✅ UI만 즉시 갱신 (전체 리렌더)
    gridEl.innerHTML = currentMovies.map(card).join("");

  } catch (err) {
    console.error(err);
    alert(err.message || "위시 처리 실패");
  } finally {
    btn.disabled = false;
  }
});

// ✅ 최초 로딩: 위시목록 → 추천목록
(async function init() {
  try {
    setMsg("불러오는 중...");
	await loadUserId();
    await loadWishedSet();
    await loadRecommend();
  } catch (e) {
    console.error(e);
    setMsg(e.message || "초기 로딩 실패");
  }
})();
