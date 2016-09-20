var qrcodeareaDiv;
var chatareaDiv;
var websocket;
var username;
var chatList;

$(document).ready(function(){
    console.log("Starting WhatsApp Clone");
    qrcodeareaDiv = $("#qrcodearea");
    chatareaDiv = $("#chatarea");
    chatareaDiv.css('display','none')
    chatList = $("#chatlist");
    initWebSocket();

    $("#messageInput").on('keypress', function (e) {
        if (e.which === 13) {
            sendMessage();
        }
    });

})


function sendMessage() {
    var message = $("#messageInput").val();

    $.ajax({
        'url': '/session/msg',
        'type': 'GET',
        'data': {
            'user': username,
            'message': message
        },
        'success': function (data) {
            //clear input field value
            $("#messageInput").val("");
        },
        'error' : function (data) {
            alert(data);
        }
    });
}

function initWebSocket() {

    websocket = new WebSocket("ws://localhost:9000/ws");

    websocket.onopen = function(evt) {
        console.log("WebSocket Opened");
    };

    websocket.onclose = function(evt) {
        console.log("WebSocket Closed");
        websocket = null;
        username = null;
    };

    websocket.onmessage = function(evt) {

        var rawdata = evt.data;

        if (rawdata.startsWith("wsready###")) {

            var qrUuid = rawdata.split("###")[1];
            console.log("UUID for qr received: " + qrUuid);
            $("#qrcode").attr("src", "http://localhost:9000/uuid/" + qrUuid);

        } else if (rawdata.startsWith("authed###")) {

            username = rawdata.split("###")[1];
            console.log("Auth done using mobile for user: " + username);
            qrcodeareaDiv.hide();
            chatareaDiv.css('display','block');

        } else if (rawdata.startsWith("msg###")) {

            var user = rawdata.split("###")[1];
            var msg = rawdata.split("###")[2];
            console.log("message from : " + user + " with data: " + msg + " \n");

            if ( username === user ) {
                // FROM ME
                chatList.append("<div class=\"list-group-item list-group-item-success\">I said:"+msg+"</div>");
            } else {
                // FROM OTHER PEOPLE
                chatList.append("<div class=\"list-group-item list-group-item-warning\" style=\"text-align: right\">"+user+" said:"+msg+"</div>");
            }

        } else {
            console.error("Unknown Stuff: " + rawdata) ;
        }

    };

    websocket.onerror = function(evt) {
        console.error("Error: " + evt) ;
    };

}

