<%@ page import="java.util.List"%>
<%@ page import="dto.UserDTO"%>
<%@ page import="dto.GenresDTO"%>
<%@ page import="dto.MovieDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieGuide - 추천</title>
<link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="./css/style2.css" />
</head>
<body><p style="color:yellow;">DEBUG uri=<%= request.getRequestURI() %></p>
<p style="color:yellow;">DEBUG movies=<%= request.getAttribute("movies") %></p>


<header class="header">
  <div class="header-inner">
    <div class="header-left">
      <a class="brand" href="./index.html">
        <img class="brand-logo" src="./assets/logo.png" alt="MovieGuide 로고" />
        <span class="brand-name">MovieGuide</span>
      </a>
      <nav class="header-nav">
        <a class="btn btn-ghost nav-link active" href="<%=request.getContextPath()%>/recommend">추천</a>
        <a class="btn btn-ghost nav-link" href="./wishlist.html">Wishlist</a>
      </nav>
    </div>
    <nav class="header-actions">
      <a class="btn btn-ghost" href="./mypage">마이페이지</a>
      <a class="btn btn-primary" href="./index.html">로그아웃</a>
    </nav>
  </div>
</header>

<%
UserDTO user = (UserDTO) request.getAttribute("userInfo");
List<GenresDTO> genresList = (List<GenresDTO>) request.getAttribute("genresList");
List<MovieDTO> recommendList = (List<MovieDTO>) request.getAttribute("movies");
List<GenresDTO> allGenreNames = (List<GenresDTO>) request.getAttribute("allGenreNames");
String message = (String) request.getAttribute("message");
%>

<main class="main-container">
  <section class="recommend-info">
    <h1 class="recommend-title">
      <i class="fa-solid fa-wand-magic-sparkles" style="color: var(--accent);"></i>
      <%= (user != null ? user.getUserNickname() : "") %> 님을 위한 추천
    </h1>
    <p class="recommend-desc">선택하신 장르를 기반으로 추천해드립니다</p>

    <div class="user-genre-tags">
      <span class="genre-label">선호 장르:</span>
      <%
      if (genresList != null) {
        for (GenresDTO g : genresList) {
          if (g.getGenresLike() == 1) {
      %>
        <div class="tag like"><%= g.getGenresName() %></div>
      <%
          }
        }
      }
      %>
    </div>
  </section>

  <div class="movie-grid">
    <%
    if (message != null && !message.trim().isEmpty()) {
    %>
      <div class="no-results"><%= message %></div>
    <%
    } else if (recommendList != null && !recommendList.isEmpty()) {
      for (MovieDTO movie : recommendList) {

        String title = (movie.getTitle() == null) ? "" : movie.getTitle();
        String original = (movie.getOriginal_title() == null) ? "" : movie.getOriginal_title();

        String poster = movie.getFullPosterPath(); // ✅ 딱 1번만
        boolean hasPoster = (poster != null && !poster.trim().isEmpty());

        String release = movie.getRelease_date();
        String year = (release != null && release.length() >= 4) ? release.substring(0, 4) : "";

        double vote = movie.getVote_average();
    %>

      <div class="movie-card">
        <div class="poster-area">
          <% if (hasPoster) { %>
            <img src="<%= poster %>" alt="<%= title %>">
          <% } else { %>
            <div class="no-poster">No Poster</div>
          <% } %>

          <button class="add-wishlist" type="button">
            <i class="fa-solid fa-plus"></i>
          </button>
        </div>

        <div class="movie-details">
          <h3 class="m-title"><%= title %></h3>
          <p class="m-eng"><%= original %></p>

          <div class="m-meta">
            <span class="m-rating">
              <i class="fa-solid fa-star"></i> <%= String.format("%.1f", vote) %>
            </span>
            <span class="m-year"><%= year %></span>
          </div>

          <div class="m-tags">
            <%
            if (movie.getGenre_ids() != null && allGenreNames != null) {
              for (Integer gid : movie.getGenre_ids()) {
                for (GenresDTO gInfo : allGenreNames) {
                  if (gInfo.getGenresID() == gid) {
            %>
              <span class="m-tag"><%= gInfo.getGenresName() %></span>
            <%
                    break;
                  }
                }
              }
            }
            %>
          </div>
        </div>
      </div>

    <%
      } // for
    } else {
    %>
      <div class="no-results">추천할 영화를 찾지 못했습니다. 마이페이지에서 장르를 더 선택해보세요!</div>
    <%
    }
    %>
  </div>
</main>

</body>
</html>
