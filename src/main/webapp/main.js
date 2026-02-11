const TMDB_API_KEY = "93f55bc880e6eb7b87f2962cce95349f";
const TMDB_BASE = "https://api.themoviedb.org/3";
const IMG_BASE = "https://image.tmdb.org/t/p/w500";

const gridEl = document.getElementById("movieGrid");
const statusEl = document.getElementById("status");

const WISHLIST_KEY = "movieguide_wishlist"; 
function setStatus(msg) {
  statusEl.textContent = msg || "";
}

async function loadWishlistSet() {
  try {
    const res = await fetch("/MovieGuide/api/wishlist", { credentials: "include" });

    // ✅ 로그아웃/세션없음 → 위시 없음(체크 풀림)
    if (res.status === 401) return new Set();

    const data = await res.json().catch(() => ({}));
    if (!res.ok || data.ok === false) return new Set();

    const ids = Array.isArray(data.movieIds) ? data.movieIds : [];
    // ✅ movie.id가 number라서 number로 맞춤
    return new Set(ids.map(Number));
  } catch (e) {
    console.error("wishlist load fail:", e);
    return new Set();
  }
}


function saveWishlistSet(set) {
  localStorage.setItem(WISHLIST_KEY, JSON.stringify([...set]));
}

async function fetchJson(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

async function getGenreMap() {
  const data = await fetchJson("/MovieGuide/api/genres");
  const map = new Map();
  (data.genres || []).forEach(g => map.set(g.id, g.name));
  return map;
}


async function getTop100Movies() {
  const data = await fetchJson("/MovieGuide/api/top100");
  // 서버가 results 형태로 주면 아래처럼
  const list = data.results || data.movies || data || [];
  return list.slice(0, 100);
}


function yearFromDate(dateStr) {
  if (!dateStr) return "—";
  return String(dateStr).slice(0, 4);
}

function score(voteAverage) {
  if (typeof voteAverage !== "number") return "—";
  return voteAverage.toFixed(1);
}

function posterUrl(posterPath) {
  if (!posterPath) return ""; 
  return `${IMG_BASE}${posterPath}`;
}

function createCard(movie, rank, genreMap, wishlistSet) {
  const title = movie.title || movie.name || "제목 없음";
  const genres = (movie.genre_ids || [])
    .map(id => genreMap.get(id))
    .filter(Boolean)
    .slice(0, 2); 

  const year = yearFromDate(movie.release_date);
  const rating = score(movie.vote_average);

  const isWished = wishlistSet.has(movie.id);

  const card = document.createElement("article");
  card.className = "card";

  const img = document.createElement("img");
  img.className = "poster";
  img.alt = title;
  const pUrl = posterUrl(movie.poster_path);
  if (pUrl) img.src = pUrl;

  
  const rankEl = document.createElement("div");
  rankEl.className = "rank-pill";
  rankEl.textContent = rank;

  
  const wishBtn = document.createElement("button");
  wishBtn.className = `wish-btn ${isWished ? "active" : ""}`;
  wishBtn.type = "button";
  wishBtn.textContent = "+";
  wishBtn.title = isWished ? "위시리스트에서 제거" : "위시리스트에 추가";

wishBtn.addEventListener("click", async (e) => {


  e.stopPropagation();

  const willAdd = !wishlistSet.has(movie.id);

  if (willAdd) {
    wishlistSet.add(movie.id);
    wishBtn.classList.add("active");
    wishBtn.textContent = "✓";

    try {
      console.log("POST 보내기 직전:", movie.id);
      await saveWishlist(movie.id, title);  // ✅ 여기서 서버로 POST 발생
      console.log("POST 성공");
    } catch (err) {
      console.error("POST 실패:", err);
      // 실패 시 롤백
      wishlistSet.delete(movie.id);
      wishBtn.classList.remove("active");
      wishBtn.textContent = "+";
      alert("위시리스트 저장 실패");
    }
  } else {
  wishlistSet.delete(movie.id);
  wishBtn.classList.remove("active");
  wishBtn.textContent = "+";

  try {
    await deleteWishlist(movie.id);
    console.log("DELETE 성공");
  } catch (err) {
    console.error("DELETE 실패:", err);
    // 실패하면 롤백(다시 wished로)
    wishlistSet.add(movie.id);
    wishBtn.classList.add("active");
    wishBtn.textContent = "✓";
    alert("위시리스트 삭제 실패");
  }
}
  

  saveWishlistSet(wishlistSet);
  wishBtn.title = willAdd ? "위시리스트에서 제거" : "위시리스트에 추가";
});



  const body = document.createElement("div");
  body.className = "card-body";

  const h3 = document.createElement("h3");
  h3.className = "movie-title";
  h3.textContent = title;

  const meta = document.createElement("div");
  meta.className = "meta";
  meta.innerHTML = `
    <span>⭐ ${rating}</span>
    <span>${year}</span>
  `;

  const badges = document.createElement("div");
  badges.className = "badges";
  genres.forEach(g => {
    const b = document.createElement("span");
    b.className = "badge";
    b.textContent = g;
    badges.appendChild(b);
  });

  body.appendChild(h3);
  body.appendChild(meta);
  body.appendChild(badges);

  card.appendChild(img);
  card.appendChild(rankEl);
  card.appendChild(wishBtn);
  card.appendChild(body);



  return card;
}

async function saveWishlist(movieId, title) {
  const res = await fetch("/MovieGuide/api/wishlist", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ movieId, title })
  });
  



  const text = await res.text();
  console.log("서버 status:", res.status);
  console.log("서버 body:", text);

  if (!res.ok) throw new Error("save failed: " + res.status);
  return JSON.parse(text);
}

async function deleteWishlist(movieId) {
  const res = await fetch(`/MovieGuide/api/wishlist?movieId=${movieId}`, {
    method: "DELETE",
    credentials: "include"
  });

  const text = await res.text();
  console.log("DELETE status:", res.status);
  console.log("DELETE body:", text);

  if (!res.ok) throw new Error("delete failed: " + res.status);
  return JSON.parse(text);
}








async function init() {
  try {
    setStatus("불러오는 중...");
    gridEl.innerHTML = "";

    const wishlistSet = await loadWishlistSet(); // ✅ 여기만 await
    const genreMap = await getGenreMap();
    const movies = await getTop100Movies();

    movies.forEach((m, idx) => {
      const card = createCard(m, idx + 1, genreMap, wishlistSet);
      gridEl.appendChild(card);
    });

    setStatus("");
  } catch (err) {
    console.error(err);
    setStatus("영화 목록을 불러오지 못했어요. (API Key / 네트워크 / CORS 확인)");
  }
}


init();