const chatBox = document.getElementById('chat-box');
const userInput = document.getElementById('user-input');

function sendMessage() {
  const message = userInput.value.trim();
  if (!message) return;

  appendMessage(message, 'user');
  userInput.value = '';

  setTimeout(() => {
    const reply = getFakeResponse(message);
    appendMessage(reply, 'ai');
  }, 600);
}

function appendMessage(text, sender) {
  const msgRow = document.createElement('div');
  msgRow.className = 'message-row ' + sender;
  msgRow.style.display = 'flex';
  msgRow.style.flexDirection = 'column';
  msgRow.style.maxWidth = '80%';
  msgRow.style.alignSelf = sender === 'user' ? 'flex-end' : 'flex-start';
  msgRow.style.textAlign = sender === 'user' ? 'right' : 'left';

  const bubble = document.createElement('div');
  bubble.textContent = text;
  bubble.style.padding = '12px 16px';
  bubble.style.borderRadius = '20px';
  bubble.style.boxShadow = '0 2px 5px rgba(0,0,0,0.1)';
  bubble.style.display = 'inline-block';
  bubble.style.wordBreak = 'break-word';
  bubble.style.backgroundColor = sender === 'user' ? '#4c6ef5' : '#f1f2f6';
  bubble.style.color = sender === 'user' ? '#fff' : '#333';

  const time = document.createElement('div');
  time.className = 'timestamp';
  time.style.fontSize = '12px';
  time.style.color = '#999';
  time.style.marginTop = '4px';
  const now = new Date();
  time.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) + ', Today';

  msgRow.appendChild(bubble);
  msgRow.appendChild(time);

  chatBox.appendChild(msgRow);
  chatBox.scrollTop = chatBox.scrollHeight;
}

function getFakeResponse(input) {
  if (input.toLowerCase().includes("hello")) {
    return "Hi there! 😊";
  }
  return "Interesting thought. Tell me more!";
}