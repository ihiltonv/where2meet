const MESSAGE_TYPE = {
    CONNECT: 0,
    UPDATE: 1,
    SCORING: 2
}

let conn;

//event is a js object with fields id, name, lat, lng, date, time.
//user is a js object with fields name, lat, lng
const setup_live_scores = (event, user) => {
    conn = new WebSocket(`wss://${window.location.host}/leaderboard`);

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
      const data = JSON.parse(msg.data);

      switch (data.type) {
          case MESSAGE_TYPE.SCORING:
            let s1 = JSON.parse(data.s1);
            let s2 = JSON.parse(data.s2);
            let s3 = JSON.parse(data.s3);
            s1 = JSON.parse(s1);
            s2 = JSON.parse(s2);
            s3 = JSON.parse(s3);
            //TODO: send suggestions to leaderboard.

  };
}

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
