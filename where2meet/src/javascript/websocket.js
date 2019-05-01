const MESSAGE_TYPE = {
    CONNECT: 0,
    UPDATE: 1,
    SCORING: 2
}

let conn;

const setup_live_scores = () => {
    console.log("Connecting");
    conn = new WebSocket("ws://localhost:3000/voting");

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
      const data = JSON.parse(msg.data);

      switch (data.type) {
          case MESSAGE_TYPE.SCORING:
            //TODO: send suggestions to leaderboard
  };
}

console.log("launching");
setup_live_scores();

//TODO: add this to wherever the user updates the leaderboard.
function updateLeaderboard(votes, user_id, event_id, suggestion){
    let obj = '{"type":'+string(MESSAGE_TYPE.UPDATE)+',"votes:"'+string(votes)+
    ',"user:"'+string(user)+',"event":'+string(event_id)+ ',"suggestion":'+string(suggestion)+'}';
    let msg = JSON.parse(JSON.stringify(obj));
    conn.send(obj);


}

//TODO: add this to werever the user is created(after modal is filled out).
function send_connection(event_id){
    let obj = '{"type": ' + string(MESSAGE_TYPE.CONNECT) +', "event_id":' +
      string(event_id)'}';

      let msg = JSON.parse(JSON.stringify(obj));
      conn.send(obj);
}
