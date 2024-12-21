// Helper functions to manage cookies
function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  return parts.length === 2 ? parts.pop().split(';').shift() : null;
}

function setCookie(name, value, days) {
  const expires = new Date(Date.now() + days * 24 * 60 * 60 * 1000).toUTCString();
  document.cookie = `${name}=${value}; expires=${expires}; path=/`;
}

// User data
let userUuid = getCookie('userUuid');
let userName = getCookie('userName');

// Check and display user dialog if necessary
window.onload = () => {
  if (!userUuid || !userName) {
    showUserDialog();
  }
};

function showUserDialog() {
  const dialogOverlay = document.getElementById('dialogOverlay');
  dialogOverlay.style.display = 'block';
  const dialog = document.getElementById('userDialog');
  dialog.style.display = 'block';

  // Handle save button click
  document.getElementById('saveUserBtn').addEventListener('click', () => {
    const inputName = document.getElementById('userName').value.trim();
    if (inputName) {
      userName = inputName;
      userUuid = crypto.randomUUID();

      // Save to cookies
      setCookie('userName', userName, 7); // Save for 7 days
      setCookie('userUuid', userUuid, 7);

      // Hide dialog
      dialog.style.display = 'none';
      dialogOverlay.style.display = 'none';
    } else {
      alert('Please enter your name!');
    }
  });
}

// Create Room
document.getElementById('createRoomBtn').addEventListener('click', async () => {
  const roomName = prompt('Enter Room Name:');
  if (roomName) {
    try {
      const response = await fetch('http://localhost:8080/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "name": roomName })
      });
      const json = await response.json();
      document.getElementById('roomUuid').value = json.uuid;
    } catch (error) {
      alert('Failed to create room. Please try again.');
    }
  }
});

// Join Room
let socket;
let leaderboard = [];
document.getElementById('joinRoomBtn').addEventListener('click', async () => {
  const roomUuid = document.getElementById('roomUuid').value.trim();
  if (!roomUuid) {
    alert('Please enter a valid Room UUID.');
    return;
  }

  // Connect WebSocket
  socket = new WebSocket(`ws://localhost:8084/realtime/${roomUuid}/${userUuid}`);
  socket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    leaderboard = [...data]

    // Sort the leaderboard by score in descending order
    leaderboard.sort((a, b) => b.score - a.score);

    // Update the HTML to reflect the sorted leaderboard
    updateLeaderboardDisplay();
  };

  // Function to update the leaderboard list in the HTML
  function updateLeaderboardDisplay() {
    const leaderboardList = document.getElementById('leaderboardList');
    leaderboardList.innerHTML = ''; // Clear current list

    // Create and append list items for each user in the leaderboard
    leaderboard.forEach(entry => {
      const listItem = document.createElement('li');
      listItem.textContent = `${entry.userName}: ${entry.score} points`;  // Format: "UserName: Score points"
      leaderboardList.appendChild(listItem);
    });
  }


  socket.onclose = () => {
    console.log('WebSocket connection closed.');
  };

  socket.onerror = (error) => {
    console.error('WebSocket error:', error);
  };

  socket.onopen = async () => {
    try {
      await fetch('http://localhost:8082/answers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ "roomUuid": roomUuid, "userUuid": userUuid, "userName": userName, "answers": [] })
      });
    } catch (error) {
      console.log(error);
    }
  };

  // Fetch questions
  const response = await fetch(`http://localhost:8083/room-question/${roomUuid}`);
  if (!response.ok) {
    throw new Error('Failed to retrieve questions.');
  }
  const questions = await response.json();

  // Display questions
  const questionList = document.getElementById('questionList');
  questionList.innerHTML = '';
  questions.forEach(question => {
    const div = document.createElement('div');
    div.className = 'question';
    div.innerHTML = `
    <p>${question.text}</p>
    <div class="options">
      ${Object.entries(JSON.parse(question.options)).map(
      ([key, value]) =>
        `<label><input type="radio" name="q-${question.uuid}" value="${key}"> ${value}</label>`
    ).join('')}
    </div>
  `;
    questionList.appendChild(div);
  });


});
// Submit Answers
document.getElementById('submitAnswersBtn').addEventListener('click', async () => {
  const roomUuid = document.getElementById('roomUuid').value.trim();
  if (!roomUuid) {
    alert('Please join a room first.');
    return;
  }

  const answers = [];
  const questionList = document.getElementById('questionList').querySelectorAll('.question');
  questionList.forEach(question => {
    const questionUuid = question.querySelector('.options input').name.substring(2);
    const selectedOption = question.querySelector('.options input:checked');
    if (selectedOption) {
      answers.push({
        questionUuid,
        answerOption: parseInt(selectedOption.value, 10)
      });
    }
  });

  try {
    await fetch('http://localhost:8082/answers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ "roomUuid": roomUuid, "userUuid": userUuid, "userName": userName, "answers": answers })
    });
    alert('Answers submitted!');
  } catch (error) {
    alert('Failed to submit answers. Please try again.');
  }
});
