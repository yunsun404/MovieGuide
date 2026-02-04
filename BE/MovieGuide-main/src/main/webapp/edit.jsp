<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieGuide - 장르 수정</title>
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
                    <a class="btn btn-ghost nav-link active" href="./recommand.html">추천</a>
                    <a class="btn btn-ghost nav-link" href="./wishlist.html">Wishlist</a>
                </nav>
            </div>
            <nav class="header-actions">
                <a class="btn btn-ghost" href="./login.html">마이페이지</a>
                <a class="btn btn-primary" href="./signup.html">로그아웃</a>
            </nav>
        </div>
    </header>

    <div class="container">
        <div class="profile-card">
            <h1 class="title" style="margin-bottom: 30px;">마이페이지</h1>

            <div class="user-info">
                <div class="avatar">
                    <i class="fas fa-user"></i>
                </div>
                <div class="user-detail">
                    <div class="name">dfsa</div> 
                    <div class="email"><i class="far fa-envelope"></i><span>dfsa@fdsafds</span></div>
                </div>
            </div>

            <form action="./updateGenres" method="post">
                <div class="genre-section">
                    <div class="genre-title">
                        <i class="far fa-thumbs-up" style="color: var(--accent)"></i> 선호하는 장르
                    </div>
                    <div class="genre-grid">
                        <button type="button" class="genre-btn" data-id="1">액션</button>
                        <button type="button" class="genre-btn" data-id="1">모험</button>
                        <button type="button" class="genre-btn" data-id="1">애니메이션</button>
                        <button type="button" class="genre-btn" data-id="1">코미디</button>
                        <button type="button" class="genre-btn" data-id="1">범죄</button>
                        <button type="button" class="genre-btn" data-id="1">다큐멘터리</button>
                        <button type="button" class="genre-btn" data-id="1">드라마</button>
                        <button type="button" class="genre-btn" data-id="1">가족</button>
                        <button type="button" class="genre-btn" data-id="1">판타지</button>
                        <button type="button" class="genre-btn" data-id="1">역사</button>
                        <button type="button" class="genre-btn" data-id="1">공포</button>
                        <button type="button" class="genre-btn" data-id="1">음악</button>
                        <button type="button" class="genre-btn" data-id="1">미스터리</button>
                        <button type="button" class="genre-btn" data-id="1">로맨스</button>
                        <button type="button" class="genre-btn" data-id="1">SF</button>
                        <button type="button" class="genre-btn" data-id="1">TV 영화</button>
                        <button type="button" class="genre-btn" data-id="1">스릴러</button>
                        <button type="button" class="genre-btn" data-id="1">전쟁</button>
                        <button type="button" class="genre-btn" data-id="1">서부</button>
                    </div>

                    <div class="genre-title" style="margin-top: 30px;">
                        <i class="far fa-thumbs-down" style="color: var(--muted)"></i> 싫어하는 장르
                    </div>
                    <div class="genre-grid">
                        <button type="button" class="genre-btn" data-id="1">액션</button>
                        <button type="button" class="genre-btn" data-id="1">모험</button>
                        <button type="button" class="genre-btn" data-id="1">애니메이션</button>
                        <button type="button" class="genre-btn" data-id="1">코미디</button>
                        <button type="button" class="genre-btn" data-id="1">범죄</button>
                        <button type="button" class="genre-btn" data-id="1">다큐멘터리</button>
                        <button type="button" class="genre-btn" data-id="1">드라마</button>
                        <button type="button" class="genre-btn" data-id="1">가족</button>
                        <button type="button" class="genre-btn" data-id="1">판타지</button>
                        <button type="button" class="genre-btn" data-id="1">역사</button>
                        <button type="button" class="genre-btn" data-id="1">공포</button>
                        <button type="button" class="genre-btn" data-id="1">음악</button>
                        <button type="button" class="genre-btn" data-id="1">미스터리</button>
                        <button type="button" class="genre-btn" data-id="1">로맨스</button>
                        <button type="button" class="genre-btn" data-id="1">SF</button>
                        <button type="button" class="genre-btn" data-id="1">TV 영화</button>
                        <button type="button" class="genre-btn" data-id="1">스릴러</button>
                        <button type="button" class="genre-btn" data-id="1">전쟁</button>
                        <button type="button" class="genre-btn" data-id="1">서부</button>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
                    <button type="submit" class="btn btn-primary">저장</button>
                </div>
            </form>
        </div>
    </div>
    <script src="./js/edit.js"></script>
</body>
</html>