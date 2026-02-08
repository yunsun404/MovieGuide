/* ===============================
   모달 열기/닫기
=============================== */
function openLogin() {
  document.getElementById("loginModal").style.display = "flex";
}
function closeLogin() {
  document.getElementById("loginModal").style.display = "none";
}
function openSignup() {
  document.getElementById("signupModal").style.display = "flex";
}
function closeSignup() {
  document.getElementById("signupModal").style.display = "none";
}



/* ==================================================
   ⭐ 모든 로직
================================================== */
document.addEventListener("DOMContentLoaded", () => {


/* ================= 로그인 ================= */
const loginBtn = document.getElementById("loginBtn");

if (loginBtn) {
  loginBtn.onclick = (e) => {

    e.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPw").value.trim();

    fetch("/MovieGuide/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    })
    .then(res => res.json())
    .then(data => {
      if (data.result) location.href = "/MovieGuide/index.html";
      else alert("로그인 실패");
    });
  };
}



/* ================= 회원가입 ================= */
const signupBtn = document.getElementById("signupBtn");

if (signupBtn) {
  signupBtn.onclick = (e) => {

    e.preventDefault();

    const name = document.getElementById("signupName").value.trim();
    const email = document.getElementById("signupEmail").value.trim();
    const pw = document.getElementById("signupPw").value.trim();
    const pwCheck = document.getElementById("signupPwCheck").value.trim();

    if (pw !== pwCheck) return alert("비밀번호 불일치");

    fetch("/MovieGuide/signup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email, password: pw })
    })
    .then(res => res.json())
    .then(data => {
      if (data.result) location.href = "/MovieGuide/genre.html";
    });
  };
}



/* ================= 장르 ================= */

const genreGrid = document.getElementById("genreGrid");
if (!genreGrid) return;

const finishBtn = document.getElementById("finishBtn");

/* ⭐ { 장르ID : 1 or 2 or 0 } */
const selected = {};

fetch("/MovieGuide/genre/list")
.then(res => res.json())
.then(data => {

  data.forEach(g => {

    const card = document.createElement("div");
    card.className = "genre-card";

    card.innerHTML = `
      <span>${g.name}</span>
      <div class="vote">
        <button class="like">👍</button>
        <button class="hate">👎</button>
      </div>
    `;

    const likeBtn = card.querySelector(".like");
    const hateBtn = card.querySelector(".hate");


    /* 👍 */
    likeBtn.onclick = () => {

      if(selected[g.id] === 1){
        selected[g.id] = 0; // 취소
        card.classList.remove("selected-like");
      }
      else{
        selected[g.id] = 1;
        card.classList.add("selected-like");
        card.classList.remove("selected-dislike");
      }
    };


    /* 👎 */
    hateBtn.onclick = () => {

      if(selected[g.id] === 2){
        selected[g.id] = 0;
        card.classList.remove("selected-dislike");
      }
      else{
        selected[g.id] = 2;
        card.classList.add("selected-dislike");
        card.classList.remove("selected-like");
      }
    };

    genreGrid.appendChild(card);
  });
});


/* 저장 */
finishBtn.onclick = () => {

  fetch("/MovieGuide/genre/select", {
    method:"POST",
    headers:{ "Content-Type":"application/json" },
    body: JSON.stringify(selected)
  })
  .then(()=> location.href="/MovieGuide/index.html");
};

});
