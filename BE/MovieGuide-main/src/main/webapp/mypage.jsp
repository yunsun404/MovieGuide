<%@ page import="java.util.List"%>
<%@ page import="com.movieguide.dto.UserDTO"%>
<%@ page import="com.movieguide.dto.GenresDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieGuide - 마이페이지</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<% // 데이터 불러오기
	UserDTO user = (UserDTO) request.getAttribute("userInfo"); 
	List<GenresDTO> genresList = (List<GenresDTO>) request.getAttribute("genresList"); %>
	
	<header class="header">
	   <div class="header-inner">
	
	     
	     <div class="header-left">
	       
	       <a class="brand" href="./index.html">
	         <img class="brand-logo" src="./img/logo.png" alt="MovieGuide 로고" />
	         <span class="brand-name">MovieGuide</span>
	       </a>
	
	       
	       <nav class="header-nav">
	         <a class="btn btn-ghost nav-link active" href="./recommend">추천</a>
	         <a class="btn btn-ghost nav-link" href="./wishlist.html">Wishlist</a>
	       </nav>
	     </div>
	
	     
	     <nav class="header-actions">
	       <a class="btn btn-ghost" href="./mypage">마이페이지</a>
	       <a class="btn btn-primary" href="./index.html">로그아웃</a>
	     </nav>
	
	   </div>
	</header>


    <div class="container">
        <div class="profile-card">
            <div class="header-row">
                <h1 class="title">마이페이지</h1>
                <button class="edit-btn" onclick="location.href='./updateGenres'"><i class="fas fa-edit"></i>장르 수정</button>
            </div>

            <div class="user-info">
                <div class="avatar">
                    <i class="fas fa-user"></i>
                </div>
                <div class="user-detail">
                <% if(user != null) { %>
                    <div class="name" id="user-name"><%= user.getUserNickname() %></div> 
                    <div class="email" id="user-email"><i class="far fa-envelope"></i><span><%= user.getUserEmail() %></span></div>
                <% } else { %>
	    		<p>정보를 불러올 수 없습니다.</p>
                <% } %>
                </div>
            </div>

            <div class="genre-section">
                <div class="genre-title">
                    <i class="far fa-thumbs-up" style="color: #e91e63"></i> 선호하는 장르
                </div>
                <div class="genre-tags" id="likeGenres"> 
                	<% if (genresList != null) { 
                		for (GenresDTO genres : genresList) {
                			if(genres.getGenresLike() == 1) { %>
                				<div class="tag like"><%= genres.getGenresName() %></div>
                			<% }
                		}
                	} %>
                </div>

                <div class="genre-title">
                    <i class="far fa-thumbs-down"></i> 싫어하는 장르
                </div>
                <div class="genre-tags" id="dislikeGenres"> 
                	<% if (genresList != null) { 
                		for (GenresDTO genres : genresList) {
                			if(genres.getGenresLike() == 2) { %>
                				<div class="tag dislike"><%= genres.getGenresName() %></div>
                			<% }
                		}
                	} %>
                </div>
            </div>
        </div>
    </div>
</body>
</html>