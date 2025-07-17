// 간단한 마크다운 변환 함수 (기본적인 **bold**, *italic*, 링크 지원)
function simpleMarkdownToHTML(markdown) {
  let html = markdown;
  html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
  html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');
  html = html.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>');
  html = html.replace(/\n/g, '<br>');
  return html;
}

// 타이핑 애니메이션
function typeInfoText(container, text, speed = 20) {
  container.innerHTML = ''; // 초기화
  let i = 0;
  const interval = setInterval(() => {
    container.innerHTML += text.charAt(i);
    i++;
    if (i >= text.length) clearInterval(interval);
  }, speed);
}

// Clova API 호출 및 결과 표시
async function fetchTravelInfo(userText) {
  const infoBox = document.querySelector('.info-box');
  infoBox.innerHTML = '<p>여행 정보를 생성 중입니다...</p>';

  try {
    const response = await fetch('http://localhost:8080/alcohols/clova', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ask: userText })
    });

    const data = await response.json();
    const content = data.result?.message?.content || '추천 정보를 불러오지 못했습니다.';
    const markdown = content; // 또는 data.result?.message?.content가 markdown이라면 그대로 사용

    const html = simpleMarkdownToHTML(markdown);
    infoBox.innerHTML = html;

    // 필요하다면 타이핑 효과 추가
    // typeInfoText(infoBox, content);
  } catch (error) {
    console.error('정보 요청 실패:', error);
    infoBox.innerHTML = '<p>정보를 불러오는 중 오류가 발생했습니다.</p>';
  }
}