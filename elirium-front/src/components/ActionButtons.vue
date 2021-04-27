<template>

  <div id="actions">
    <div id="actions-block">
      <div id="admin-panel">
        <input @input="changeRange()" type="text" id="output" value="0"/>
        <input type="hidden" id="value">
      </div>

      <div id="buttons">
        <input type="button" @click="takeChips" id="chips" value="TAKE CHIPS">
        <input type="button" @click="changeState" id="state" value="CHANGE STATE">
        <input type="button" @click="sendCall" id="call" value="CALL">
        <input type="button" @click="sendRaise" id="raise" value="RAISE">
        <input type="button" @click="sendCheck" id="check" value="CHECK">
        <input type="button" @click="sendFold" id="fold" value="FOLD">
        <input type="button" @click="sendAllIn" id="all-in" value="ALL-IN">
        <input type="button" @click="sendBet" id="bet" value="BET">
      </div>

      <div id="slider">
        <input type="range" @input="changeValue()" :min="1" :max="5000" value="0" id="count">
      </div>
    </div>
  </div>
</template>


<script>
const SEND_ACTION_PATH_START = '/poker/game';
const ADD_PLAYER_PATH = "/addPlayer";
const RELOAD_PATH = "/reload";
const CHANGE_STATE_PATH = "/admin/changeStateType";
const ADD_CHIPS_PATH = "/admin/addChips";
const TAKE_CHIPS_PATH = "/admin/addChips";
const SEND_ACTION_PATH = "/admin/doAction";
const ADD_IN_GAME_PATH = "/addPlayerInGame";


export default {
  props: {
    stomp: Object
  },
  methods: {
    sendCall: function () {
      const message = {
        "count": document.getElementById("count").value,
        "name": document.getElementById("value").value,
        "actionType": "CALL"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message))
      // this.clearValues()
    },
    sendBet: function () {
      const message = {
        "count": document.getElementById("count").value,
        "name": document.getElementById("value").value,
        "actionType": "BET"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message));
      // this.clearValues()
    },
    sendRaise: function () {
      const message = {
        "count": document.getElementById("count").value,
        "name": document.getElementById("value").value,
        "actionType": "RAISE"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message));
    },
    takeChips: function () {
      this.stomp.send(TAKE_CHIPS_PATH, {}, document.getElementById("value").value);
    },
    sendAllIn: function () {
      const message = {
        "count": document.getElementById("count").value,
        "name": document.getElementById("value").value,
        "actionType": "ALLIN"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message));
      // this.clearValues()
    },
    sendFold: function () {
      const message = {
        "count": "0",
        "name": document.getElementById("value").value,
        "actionType": "FOLD"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message));
      // this.clearValues()
    }
    ,
    changeState: function () {
      this.stomp.send(CHANGE_STATE_PATH, {}, document.getElementById("value").value);
    },
    sendCheck: function () {
      const message = {
        "count": "0",
        "name": document.getElementById("value").value,
        "actionType": "CHECK"
      };
      this.stomp.send(SEND_ACTION_PATH, {}, JSON.stringify(message));
      // this.clearValues()
    },
    changeValue() {
      const output = document.getElementById("output");
      const range = document.getElementById("count");
      output.value = range.value;
    },
    changeRange() {
      const slider = document.getElementById("count");
      const output = document.getElementById("output");
      slider.value = output.value
    },
    clearValues() {
      const slider = document.getElementById("count");
      const output = document.getElementById("output");
      slider.value = 0;
      output.value = 0;
    }
  }
}
</script>


<style>

#buttons input[type="button"] {
  width: 10%;
  height: 40%;
  background-color: #0A1747;
  color: white;
  margin-left: 1%;
  margin-right: 1%;
}

#buttons input[type="button"]:hover {
  background-color: #8D07F6;
}

#output {
  width: 15%;
  height: 65%;
}

#value {
  width: 15%;
  height: 65%;
  margin-right: 1%;
}

#actions-block {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  height: 12vh;
  width: 100%;
  box-shadow: 0 0 10px rgba(1, 58, 64, 0.8); /* Параметры тени */
  position: absolute;
}

#admin-panel {
  display: flex;
  height: 20%;
  width: 100%;
  justify-content: flex-end;
  align-items: center;
  background: dimgrey;
  box-shadow: 0 0 10px rgba(0, 0, 0, 1); /* Параметры тени */
}

#actions {
  display: flex;
  justify-content: center;
  height: 12vh;
  width: 50%;
}

#slider {
  display: flex;
  justify-content: center;
  width: 100%;
  height: 20%;
}

#count {
  width: 50%;
}

#buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 60%;
}

</style>
