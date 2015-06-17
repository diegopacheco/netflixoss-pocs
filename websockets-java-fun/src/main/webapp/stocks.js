// establish the communication channel over a websocket
var ws = new WebSocket("ws://127.0.0.1:8090/stocks");
 
// called when socket connection established
ws.onopen = function() {
    appendLog("Connected to stock service! Press 'Start' to get stock info.")
};
 
// called when a message received from server
ws.onmessage = function (evt) {
    appendLog(evt.data)
};
 
// called when socket connection closed
ws.onclose = function() {
    appendLog("Disconnected from stock service!")
};
 
// called in case of an error
ws.onerror = function(err) {
    console.log("ERROR!", err )
};
 
// appends logText to log text area
function appendLog(logText) {
    var log = document.getElementById("log");
    log.value = log.value + logText + "\n"
}
 
// sends msg to the server over websocket
function sendToServer(msg) {
    ws.send(msg);
}