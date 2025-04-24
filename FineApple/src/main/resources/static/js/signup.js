document.addEventListener("DOMContentLoaded", () => {
    // 각 필드와 에러 메시지 요소 가져오기
    const emailInput = document.querySelector('input[name="email"]');
    const emailError = document.getElementById("emailError");

    const passwordInput = document.querySelector('input[name="password"]');
    const confirmPasswordInput = document.getElementById("confirmPassword");
    const pwError = document.getElementById("pwError");

    const nameInput = document.querySelector('input[name="name"]');
    const nameError = document.getElementById("nameError");

    const telInput = document.querySelector('input[name="tel"]');
    const telError = document.getElementById("telError");

    // 이메일 유효성 검사
    emailInput.addEventListener("input", () => {
        const emailValue = emailInput.value;
        const emailRegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 이메일 형식 정규식
        if (!emailRegExp.test(emailValue)) {
            emailError.textContent = "올바른 이메일 형식을 입력해주세요.";
        } else {
            emailError.textContent = ""; // 조건 만족 시 에러 메시지 제거
        }
    });

    // 비밀번호 유효성 검사
    passwordInput.addEventListener("input", () => {
        const passwordValue = passwordInput.value;
        if (passwordValue.length < 3) {
            pwError.textContent = "비밀번호는 최소 3자리 이상이어야 합니다.";
        } else {
            pwError.textContent = ""; // 조건 만족 시 에러 메시지 제거
        }
    });

    // 비밀번호 확인 검사
    confirmPasswordInput.addEventListener("input", () => {
        const confirmPasswordValue = confirmPasswordInput.value;
        if (confirmPasswordValue !== passwordInput.value) {
            pwError.textContent = "비밀번호가 일치하지 않습니다.";
        } else if (passwordInput.value.length >= 3) {
            pwError.textContent = ""; // 조건 만족 시 에러 메시지 제거
        }
    });

    // 이름 유효성 검사
    nameInput.addEventListener("input", () => {
        const nameValue = nameInput.value;
        const nameRegExp = /^[가-힣a-zA-Z]+$/; // 한글 또는 영문만 허용
        if (!nameRegExp.test(nameValue)) {
            nameError.textContent = "이름에는 한글과 영문만 사용 가능합니다.";
        } else {
            nameError.textContent = ""; // 조건 만족 시 에러 메시지 제거
        }
    });

    // 전화번호 유효성 검사
    telInput.addEventListener("input", () => {
        const telValue = telInput.value;
        const telRegExp = /^010-\d{4}-\d{4}$/; // 전화번호 형식: 예) 010-1234-5678
        if (!telRegExp.test(telValue)) {
            telError.textContent = "전화번호는 '010-1234-5678' 형식으로 입력해주세요.";
        } else {
            telError.textContent = ""; // 조건 만족 시 에러 메시지 제거
        }
    });

    // 폼 제출 시 추가 검증
    document.querySelector(".signup-form").addEventListener("submit", (event) => {
        // 모든 에러 메시지가 비어있는지 확인
        if (
            emailError.textContent ||
            pwError.textContent ||
            nameError.textContent ||
            telError.textContent
        ) {
            event.preventDefault(); // 에러가 있으면 제출 방지
            alert("입력한 정보를 확인해주세요.");
        }
    });
});
