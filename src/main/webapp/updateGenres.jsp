<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dto.GenresDTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieGuide - 장르 수정</title>
<link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="./css/style2.css" />
</head>
<body>
<header class="header">
    <div class="header-inner">
        <div class="header-left">
            <a class="brand" href="./index.html">
                <img class="brand-logo" src="./assets/logo.png" alt="MovieGuide 로고" />
                <span class="brand-name">MovieGuide</span>
            </a>
            <nav class="header-nav">
                <a class="btn btn-ghost nav-link active" href="/MovieGuide/recommend.html">추천</a>
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
String error = request.getParameter("error");
if ("true".equals(error)) {
%>
<script>
  alert('저장에 실패했습니다. 다시 시도해주세요.');
</script>
<%
}
%>

<div class="container">
    <div class="profile-card">
        <h1 class="title" style="margin-bottom: 30px;">마이페이지</h1>

        <%
        List<GenresDTO> allGenres = (List<GenresDTO>) request.getAttribute("allGenres");
        List<GenresDTO> userGenres = (List<GenresDTO>) request.getAttribute("userGenres");
        %>

        <form id="genreForm" action="./updateGenres" method="post">
            <div id="hiddenInputs"></div>

            <!-- ✅ (사진2) 요약/칩 영역 -->
            <div class="summary" style="margin-bottom: 20px;">
                <div class="summary-row">
                    <div class="summary-title">👍 선호하는 장르</div>
                    <div id="preferChips" class="chips"></div>
                </div>

                <div class="summary-row" style="margin-top: 10px;">
                    <div class="summary-title">👎 싫어하는 장르</div>
                    <div id="dislikeChips" class="chips"></div>
                </div>
            </div>

            <div class="genre-section">

                <div class="genre-title">
                    <i class="far fa-thumbs-up" style="color: var(--accent)"></i>
                    선호하는 장르
                </div>

                <!-- ✅ id 추가 -->
                <div class="genre-grid" id="preferGrid">
                    <%
                    for (GenresDTO all : allGenres) {
                        String activeClass = "";
                        if (userGenres != null) {
                            for (GenresDTO user : userGenres) {
                                if (all.getGenresID() == user.getGenresID() && user.getGenresLike() == 1) {
                                    activeClass = "active";
                                    break;
                                }
                            }
                        }
                    %>
                    <button type="button"
                            class="genre-btn <%=activeClass%>"
                            data-id="<%=all.getGenresID()%>"
                            data-name="<%=all.getGenresName()%>">
                        <%=all.getGenresName()%>
                    </button>
                    <%
                    }
                    %>
                </div>

                <div class="genre-title" style="margin-top: 30px;">
                    <i class="far fa-thumbs-down" style="color: var(--muted)"></i>
                    싫어하는 장르
                </div>

                <!-- ✅ id 추가 -->
                <div class="genre-grid" id="dislikeGrid">
                    <%
                    for (GenresDTO all : allGenres) {
                        String activeClass = "";
                        if (userGenres != null) {
                            for (GenresDTO user : userGenres) {
                                if (all.getGenresID() == user.getGenresID() && user.getGenresLike() == 2) {
                                    activeClass = "active";
                                    break;
                                }
                            }
                        }
                    %>
                    <button type="button"
                            class="genre-btn <%=activeClass%>"
                            data-id="<%=all.getGenresID()%>"
                            data-name="<%=all.getGenresName()%>">
                        <%=all.getGenresName()%>
                    </button>
                    <%
                    }
                    %>
                </div>

            </div>

            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                <button type="submit" class="btn btn-primary">저장</button>
            </div>
        </form>
    </div>
</div>

<script src="/MovieGuide/edit.js"></script>
</body>
</html>
