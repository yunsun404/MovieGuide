const API_WISHLIST = "/MovieGuide/api/wishlist";
const API_MOVIE_DETAIL = "/MovieGuide/api/movie"; // /{id}

const grid = document.getElementById("movieGrid");
const msg = document.getElementById("messageBox");

function setMsg(text) {
  if (!text) {
    msg.style.display = "none";
    msg.textContent = "";
    return;
  }
  msg.style.display = "block";
  msg.textContent = text;
}

function posterUrl(poster_path) {
  if (!poster_path) return "";
  if (poster_path.startsWith("http")) return poster_path;
  return "https://image.tmdb.org/t/p/w500" + poster_path;
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

  return `
    <div class="movie-card" data-movie-id="${movie.id}">
      <div class="poster-area">
        ${posterHtml}
        <button class="add-wishlist" type="button" data-action="delete" title="삭제">
          <i class="fa-solid fa-minus"></i>
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

async function fetchWishlistIds() {
  const res = await fetch(API_WISHLIST, { credentials: "include" });
  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.ok === false) throw new Error(data.error || `wishlist 조회 실패(HTTP ${res.status})`);
  return Array.isArray(data.movieIds) ? data.movieIds : [];
}

async function fetchMovieDetail(id) {
  const res = await fetch(`${API_MOVIE_DETAIL}/${encodeURIComponent(id)}`, { credentials: "include" });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(`movie detail 실패 id=${id} (HTTP ${res.status})`);
  return data;
}

async function deleteWishlist(id) {
  const res = await fetch(`${API_WISHLIST}?movieId=${encodeURIComponent(id)}`, {
    method: "DELETE",
    credentials: "include"
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok || data.ok === false) throw new Error(data.error || `삭제 실패(HTTP ${res.status})`);
  return data;
}

async function load() {
  try {
    if (!grid) throw new Error("movieGrid 요소를 못 찾음 (wishlist.html id 확인)");
    setMsg("불러오는 중...");
    grid.innerHTML = "";

    const ids = await fetchWishlistIds();
    if (ids.length === 0) {
      setMsg("위시리스트가 비어있습니다.");
      return;
    }

    // 병렬 호출
    const movies = await Promise.all(ids.map(fetchMovieDetail));

    setMsg("");
    grid.innerHTML = movies.map(card).join("");
  } catch (e) {
    console.error(e);
    setMsg(e.message || "불러오기 실패");
  }
}

grid?.addEventListener("click", async (e) => {
  const btn = e.target.closest("button[data-action='delete']");
  if (!btn) return;

  const cardEl = e.target.closest(".movie-card");
  const movieId = cardEl?.dataset.movieId;
  if (!movieId) return;

  try {
    btn.disabled = true;
    await deleteWishlist(movieId);
    await load();
  } catch (err) {
    console.error(err);
    alert(err.message || "삭제 실패");
  } finally {
    btn.disabled = false;
  }
});

load();
