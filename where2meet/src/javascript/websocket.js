const MESSAGE_TYPE = {
    CONNECT: 0,
    UPDATE: 1,
    SCORING: 2
}

let conn;

const setup_live_scores = () => {
    conn = new WebSocket(`wss://${window.location.host}/leaderboard`);

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

setup_live_scores();

//TODO: add this to wherever the user updates the leaderboard.
function updateLeaderboard(votes, user, event){
    let obj = '{"type":'+string(MESSAGE_TYPE.UPDATE)+',"votes:"'+string(votes)+
    ',"user:"'+string(user)+',"event":'+string(event)+ '}';
    let msg = JSON.parse(JSON.stringify(obj));
    conn.send(obj);


}

//TODO: add this to werever the user is created(after modal is filled out).
function send_connection(event, user){
    let obj = '{"type": ' + string(MESSAGE_TYPE.CONNECT) +', "event_id":' +
      string(event.id)',"event_name":'+string(event.name)',"event_lat":'+
      string(event.lat)+',"event_lng":'+string(event.lng)',"event_date:"'+
      string(event.date)+',"event_time":'+string(event.time)+',"user_name:"'+
      string(user.name)+',"user_lat":'+string(user.lat)+',"user_lng":'+
      string(user.lng)+'}';

      let msg = JSON.parse(JSON.stringify(obj));
      conn.send(obj);
}
