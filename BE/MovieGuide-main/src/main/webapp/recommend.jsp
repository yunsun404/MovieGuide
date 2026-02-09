<%@ page import="java.util.List"%>
<%@ page import="com.movieguide.dto.UserDTO"%>
<%@ page import="com.movieguide.dto.GenresDTO"%>
<%@ page import="com.movieguide.dto.MovieDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieGuide - 추천</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
    <header class="header">
	   <div class="header-inner">
	     <div class="header-left">
	       <a class="brand" href="./index.html">
	         <img class="brand-logo" src="./img/logo.png" alt="MovieGuide 로고" />
	         <span class="brand-name">MovieGuide</span>
	       </a>
	       <nav class="header-nav">
	         <a class="btn btn-ghost nav-link active" href="./recommand">추천</a>
	         <a class="btn btn-ghost nav-link" href="./wishlist.html">Wishlist</a>
	       </nav>
	     </div>
	     <nav class="header-actions">
	       <a class="btn btn-ghost" href="./mypage">마이페이지</a>
	       <a class="btn btn-primary" href="./index.html">로그아웃</a>
	     </nav>
	   </div>
	</header>

	<% // 데이터 불러오기
	UserDTO user = (UserDTO) request.getAttribute("userInfo"); 
	List<GenresDTO> genresList = (List<GenresDTO>) request.getAttribute("genresList"); 
	List<MovieDTO> recommendList = (List<MovieDTO>) request.getAttribute("recommendList"); 
	List<GenresDTO> allGenreNames = (List<GenresDTO>) request.getAttribute("allGenreNames"); %>
	
    <main class="main-container">
        <section class="recommend-info">
            <h1 class="recommend-title">
                <i class="fa-solid fa-wand-magic-sparkles" style="color: var(--accent);"></i> 
                <% if(user != null) { %>
                <%= user.getUserNickname() %>
                <% } else { %>
	    		<p>   </p>
                <% } %> 
                님을 위한 추천
            </h1>
            <p class="recommend-desc">선택하신 장르를 기반으로 추천해드립니다</p>
            
            <div class="user-genre-tags">
                <span class="genre-label">선호 장르:</span>
                <% if (genresList != null) { 
               		for (GenresDTO genres : genresList) {
               			if(genres.getGenresLike() == 1) { %>
               				<div class="tag like"><%= genres.getGenresName() %></div>
               			<% }
               		}
               	} %>
            </div>
        </section>

        <div class="movie-grid">
        <!-- api에서 끌고 오기 -->

		<% 
		if (recommendList != null && !recommendList.isEmpty()) { 
		    for (MovieDTO movie : recommendList) { 
        %>
            <div class="movie-card">
                <div class="poster-area">
                    <img src="<%= movie.getFullPosterPath() %>" alt="<%= movie.getTitle() %>">
                    <button class="add-wishlist"><i class="fa-solid fa-plus"></i></button>
                </div>
                <div class="movie-details">
                    <h3 class="m-title"><%= movie.getTitle() %></h3>
                    <p class="m-eng"><%= movie.getOriginal_title() %></p>
                    <div class="m-meta">
                        <%-- 평점 소수점 한자리까지 출력 --%>
                        <span class="m-rating"><i class="fa-solid fa-star"></i> <%= String.format("%.1f", movie.getVote_average()) %></span>
                        <%-- 개봉일 연도만 추출 --%>
                        <span class="m-year"><%= (movie.getRelease_date() != null && movie.getRelease_date().length() >= 4) ? movie.getRelease_date().substring(0, 4) : "" %></span>
                    </div>
                    <%-- 특정 영화의 장르 띄우기 --%>
                    <div class="m-tags">
                        <% if (movie.getGenre_ids() != null) {
			                int count = 0;
			                for (Integer gid : movie.getGenre_ids()) {
			                    for (GenresDTO gInfo : allGenreNames) {
			                        if (gInfo.getGenresID() == gid) {
			            %>
			                            <span class="m-tag"><%= gInfo.getGenresName() %></span>
			            <% 
			                            count++;
			                            break;
			                        }
			                    }
			                }
			            } 
			            %>
                    </div>
                </div>
            </div>
        <%  } 
		} else { %>
            <div class="no-results">추천할 영화를 찾지 못했습니다. 마이페이지에서 장르를 더 선택해보세요!</div>
        <% } %>
            
        </div>
    </main>
</body>
</html>