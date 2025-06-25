$(document).ready(function() {
  // 모달 관련 요소 가져오기
  var modal = $('#successModal');
  var span = $('.close-button');
  var confirmButton = $('#confirmButton');
  var modalMessage = $('#modalMessage');

  // 닫기 버튼 클릭 시 모달 닫기
  span.on('click', function() {
    modal.hide();
    window.location.href = '/'; // 메인 페이지로 이동
  });

  // 확인 버튼 클릭 시 모달 닫고 메인 페이지로 이동
  confirmButton.on('click', function() {
    modal.hide();
    window.location.href = '/'; // 메인 페이지로 이동
  });

  // 모달 외부 클릭 시 닫기
  $(window).on('click', function(event) {
    if ($(event.target).is(modal)) {
      modal.hide();
      window.location.href = '/'; // 메인 페이지로 이동
    }
  });

  $('#registrationForm').on('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지

    // 클라이언트 측 유효성 검사
    let isValid = true;

    // 모든 피드백 메시지 초기화
    $('.invalid-feedback').removeClass('d-block').text('');

    var name = $('#name').val();
    if (name.length < 2 || name.length > 20) {
      $('#nameFeedback').text('이름은 2자 이상 20자 이하여야 합니다.').addClass('d-block');
      isValid = false;
    }

    var email = $('#email').val();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      $('#emailFeedback').text('유효한 이메일 주소를 입력하세요.').addClass('d-block');
      isValid = false;
    }

    var password = $('#password').val();
    if (password.length < 8) {
      $('#passwordFeedback').text('비밀번호는 8자 이상이어야 합니다.').addClass('d-block');
      isValid = false;
    }

    var confirmPwd = $('#confirmPwd').val();
    if (password !== confirmPwd) {
      $('#confirmPwdFeedback').text('비밀번호가 일치하지 않습니다.').addClass('d-block');
      isValid = false;
    }

    var nickname = $('#nickname').val();
    if (nickname.length < 2 || nickname.length > 20) {
      $('#nicknameFeedback').text('닉네임은 2자 이상 20자 이하여야 합니다.').addClass('d-block');
      isValid = false;
    }

    var birthday = $('#birthday').val();
    const birthdayRegex = /^(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/;
    if (birthday && !birthdayRegex.test(birthday)) {
      $('#birthdayFeedback').text('생년월일은 YYYYMMDD 형식의 8자리 숫자여야 합니다.').addClass('d-block');
      isValid = false;
    } else if (!birthday && $('#birthday').prop('required')) {
      $('#birthdayFeedback').text('생년월일은 필수 입력 값입니다.').addClass('d-block');
      isValid = false;
    }

    var address = $('#address').val();
    if (address.length > 100) {
      $('#addressFeedback').text('주소는 100자를 초과할 수 없습니다.').addClass('d-block');
      isValid = false;
    }

    if (!isValid) {
      return; // 유효성 검사 실패 시 제출 중단
    }

    // AJAX 요청
    $.ajax({
      url: '/user/regist-process', // UserController의 경로
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        name: name,
        email: email,
        password: password,
        confirmPwd: confirmPwd,
        nickname: nickname,
        birthday: birthday,
        address: address
      }),
      success: function(response) {
        var res = JSON.parse(response);
        modalMessage.text(res.message);
        modal.css('display', 'flex');
      },
      error: function(xhr) {
        var errorData = JSON.parse(xhr.responseText);
        alert('회원가입 실패: ' + errorData.message);
      }
    });
  });
});