var stompClient = null;
var notificationCount = 0;
var userName = null;
var notificationList = [];

$(document).ready(function() {
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendMessage();
    });

    $("#send-private").click(function() {
        sendPrivateMessage();
    });

    $("#notifications").click(function() {
        resetNotificationCount();
    });

    $('#username').click(function() {
        storeUserName();
    });
});

function connect() {
    var socket = new SockJS('/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        updateNotificationDisplay();
        stompClient.subscribe('/topic/messages', function (message) {
            const parsedJson = JSON.parse(message.body);
            showMessage(parsedJson.from, parsedJson.content);
        });

        stompClient.subscribe('/user/topic/private-messages', function (message) {
            showMessage(JSON.parse(message.body).from, JSON.parse(message.body).content);
        });

        stompClient.subscribe('/topic/global-notifications', function (message) {
            notificationCount = notificationCount + 1;
            addNotification(JSON.parse(message.body).from, JSON.parse(message.body).content);
            updateNotificationDisplay();
        });

        stompClient.subscribe('/user/topic/private-notifications', function (message) {
            notificationCount = notificationCount + 1;
            addNotification(JSON.parse(message.body).from, JSON.parse(message.body).content);
            updateNotificationDisplay();
        });
    });
}


function storeUserName() {
    userName = $("#name").val() != null ? $("#name").val() : 'Default User';
    document.getElementById('name').value = '';
    var elem = document.getElementById("username");
    if (elem.innerHTML === "store") {
        elem.innerHTML = "Name Stored";
        document.getElementById('username').disabled = true;
    } else {
        elem.innerHTML = "store";
        document.getElementById('username').disabled = false;
    }



}

function showMessage(from, message) {
    $("#messages")
    .append("<tr>")
    .append("<td>" + from + "</td>")
    .append("<td>" + message + "</td>")
    .append("</tr>");
}

function sendMessage() {
    console.log("sending message");
    stompClient.send("/ws/message", {}, JSON.stringify({'from': userName,'messageContent': $("#message").val()}));
    document.getElementById('message').value = '';
}

function sendPrivateMessage() {
    console.log("sending private message");
    stompClient.send("/ws/private-message", {}, JSON.stringify({'from': userName, 'messageContent': $("#private-message").val()}));
    document.getElementById('private-message').value = '';
}

function updateNotificationDisplay() {
    if (notificationCount == 0) {
        $('#notifications').hide();
        $('#sender').hide();
    } else {
        $('#notifications').show();
        $('#sender').show();
        $('#notifications').text(notificationCount);
        $('#sender').text(notificationCount);
    }
}

function resetNotificationCount() {
    notificationCount = 0;
    let finalMessage = '';
    notificationList.forEach((message) => {
        finalMessage += `${message}\n`;
    });
    notificationList.splice(0,notificationList.length);
    updateNotificationDisplay();
    alert(finalMessage);
}

function addNotification(from, message) {
    notificationList.push(`${from} : ${message}`);
}