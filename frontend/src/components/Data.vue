<template>
  <div id="app">

    <div class="container-fluid m-0 p-0 main-menu">
      <nav class="navbar navbar-dark bg-dark justify-content-between">

        <button v-if="isConnecting" class="btn btn-outline-light my-2 my-sm-0" type="submit"
                @click="disconnect()">Disconnect
        </button>
        <button v-if="!isConnecting" class="btn btn-outline-light my-2 my-sm-0" type="submit"
                @click="connect()">Connect
        </button>

        <form class="form-inline">
          <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-light my-2 my-sm-0" type="submit">Search game</button>
        </form>
      </nav>
    </div>

    <div class="container-fluid d-flex justify-content-between align-items-center bg-dark main-logo">

      <div class="row w-25">
        <GamesList :games="games" :server_messages="server_messages" :stomp="stomp"
                   :isConnecting="isConnecting" :clearFunction="clearInformation"></GamesList>
      </div>

      <div class="row w-50">
        <div class="col d-flex justify-content-center">
          <div class="logo position-relative" v-if="!inGame">
            <p class="text-light">SMN ROOM</p>
          </div>
          <div class="col d-flex justify-content-center flex-wrap" v-if="inGame">
            <Game :players="players" :stomp="stomp" :flop="flop" :tern="tern" :river="river"/>
            <ActionButtons :stomp="stomp"/>
          </div>
        </div>
      </div>

      <div class="row w-25">

      </div>
    </div>


  </div>
</template>
<script>
import User from "../js/User";
import Game from '../components/Game.vue'
import ActionButtons from '../components/ActionButtons.vue'
import Console from '../components/Console.vue'
import GamesList from '../components/GamesList.vue'

const SockJS = require('sockjs-client');
const Stomp = require('stompjs');
const CONNECT_PATH = 'http://127.0.0.1:1010/connect';
const USER_POKER_GAME_SUBSCRIPTION = "/user/poker/game";
const USER_ERROR_SUBSCRIPTION = "/user/poker/error";
const ALL_POKER_GAMES_SUBSCRIPTION = "/poker/games";
const ALL_ERRORS_SUBSCRIPTION = "/poker/error";

export default {
  name: "Data",
  components: {
    Game,
    ActionButtons,
    Console,
    GamesList
  },
  data() {
    return {
      stomp: null,
      sock: null,
      players: null,
      bank: null,
      flop: null,
      tern: null,
      river: null,
      activePlayer: null,
      gameName: null,
      games: null,
      server_messages: []
    }
  },
  created() {
    this.connect()
  },
  computed: {
    isConnecting: function () {
      return this.sock.readyState === 1;
    },
    inGame: function () {
      return this.players != null
    }
  },
  methods: {
    connect() {
      const sock = new SockJS(CONNECT_PATH);
      const stomp = Stomp.over(sock);
      this.sock = sock;
      this.stomp = stomp;
      // this.stomp.debug = null;
      stomp.connect({}, () => {
        stomp.subscribe(USER_POKER_GAME_SUBSCRIPTION, data => {
          this.fillPlayers(data.body);
          this.fillGameInformation(data.body);
        });
        stomp.subscribe(ALL_POKER_GAMES_SUBSCRIPTION, data => {
          this.fillGames(data.body)
        });
        stomp.subscribe(ALL_ERRORS_SUBSCRIPTION, data => {
          this.fillErrors(data.body)
        });
        stomp.subscribe(USER_ERROR_SUBSCRIPTION, data => {
          this.fillErrors(data.body)
        })
      });
    },
    clearInformation() {
      this.players = null;
      this.bank = null;
      this.flop = null;
      this.tern = null;
      this.river = null;
      this.activePlayer = null;
      this.gameName = null;
    },
    fillPlayers(data) {
      const players = [];
      const jsonPlayers = JSON.parse(data).players;
      jsonPlayers.forEach(player => {
        if (player.active){
         const playerBet = document.getElementById("value");
         if (playerBet != null){
           playerBet.value = player.name
         }
        }
        players.push(
            new User(
                player.name,
                player.active,
                player.chipsCount,
                player.cards,
                [this.getCardAsImg(player.cards[0]), this.getCardAsImg(player.cards[1])],
                player.roleType,
                player.stateType,
                player.timeBank,
                player.action
            )
        );
      });
      this.players = players;
      this.players.forEach(player => {
        console.log(player)
      })
    },
    fillGameInformation(data) {
      const jsonInformation = JSON.parse(data);
      this.bank = jsonInformation.bank;
      this.flop = [];

      if (jsonInformation.flop != null) {
        jsonInformation.flop.forEach(card => {
          this.flop.push(this.getCardAsImg(card));
        });
      }
      if (jsonInformation.tern != null) {
        this.tern = this.getCardAsImg(jsonInformation.tern);
      } else {
        this.tern = null
      }
      if (jsonInformation.river != null) {
        this.river = this.getCardAsImg(jsonInformation.river);
      } else {
        this.river = null
      }
      this.activePlayer = jsonInformation.activePlayer;
      this.gameName = jsonInformation.gameName;
    },
    fillGames(data) {
      this.games = JSON.parse(data);
    },
    fillErrors(data) {
      this.server_messages.push(data);
    }
    ,
    disconnect() {
      if (this.sock != null) {
        this.sock.close();
      }
    },
    getCardAsImg(card) {
      let path = ""
      if (typeof card === "object") {
        path = "/cards/" + card.cardType + ".png";
      } else if (typeof card === "undefined") {
        path = "/cards/SHIRT.png";
      } else {
        path = "/cards/" + card + ".png";
      }
      const cards = document.createElement('img');
      cards.src = path;
      cards.width = 35;
      cards.height = 48;
      return cards;

    }
  }
}

</script>
<style>
* {
  padding: 0;
  margin: 0;
  text-shadow: 0 0.05rem 0.1rem rgba(0, 0, 0, .5);
}

.logo {
  text-transform: uppercase;
  font-size: 2.5em;
}

.logo:hover {
  transition: all 1s;
  transform: scale(1.4, 1.4);
}

.main-logo {
  height: 95vh;
}

/*MAIN MENU*/

.main-menu {
  height: 5vh;
  border-bottom: 55px solid white;
}
</style>




