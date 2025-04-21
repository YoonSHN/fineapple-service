document.getElementById('logout-link').addEventListener('click', function(e) {
    e.preventDefault(); // 기본 동작 방지
    document.getElementById('logout-form').submit(); // 폼 제출
});