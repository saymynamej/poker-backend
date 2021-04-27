<template>

  <div id="main-table">
    <div id="table">
      <div id="table-img">
        <div id="upper-players">
          <div class="player" v-if="players != null && index < 3" v-for="(player,index) in players"
               :class="{ activePlayer: player.isActive }">
            <div>
              <div class="player-name">
                {{ player.name }}: {{ player.roleType }} {{ player.action.actionType }} {{
                  player.action.count
                }}
              </div>
              <div class="players-cards">
                <img :src="player.cardsAsImg[0].src" width="40px">
                <img :src="player.cardsAsImg[1].src" width="40px">
              </div>
              <div class="player-chips">
                {{ player.chipsCount }}
                {{ player.timeBank }}
              </div>
            </div>
          </div>
        </div>

        <div id="main-middle-players">
          <div id="middle-players-left">

            <div class="player" v-if="players != null && index === 3" v-for="(player,index) in players"
                 :class="{ activePlayer: player.isActive }">
              <div>
                <div class="player-name">
                  {{ player.name }}: {{ player.roleType }} {{ player.action.actionType }} {{
                    player.action.count
                  }}
                </div>
                <div class="players-cards">
                  <img :src="player.cardsAsImg[0].src" width="40px">
                  <img :src="player.cardsAsImg[1].src" width="40px">
                </div>
                <div class="player-chips">
                  {{ player.chipsCount }}
                  {{ player.timeBank }}
                </div>
              </div>
            </div>
          </div>

          <div id="deck">
            <div id="flop" v-if="flop != null" v-for="card in flop">
              <img :src="card.src" width="45px">
            </div>
            <div id="tern" v-if="tern != null">
              <img :src="tern.src" width="45px">
            </div>
            <div id="river" v-if="river != null">
              <img :src="river.src" width="45px">
            </div>
          </div>
          <div id="middle-players-right">

            <div class="player" v-if="players != null && index === 4" v-for="(player,index) in players"
                 :class="{ activePlayer: player.isActive }">
              <div>
                <div class="player-name">
                  {{ player.name }}: {{ player.roleType }} {{ player.action.actionType }} {{
                    player.action.count
                  }}
                </div>
                <div class="players-cards">
                  <img :src="player.cardsAsImg[0].src" width="40px">
                  <img :src="player.cardsAsImg[1].src" width="40px">
                </div>
                <div class="player-chips">
                  {{ player.chipsCount }}
                  {{ player.timeBank }}
                </div>
              </div>
            </div>
          </div>
        </div>


        <div id="bottom-players">
          <div class="player" v-if="players != null && index >= 5" v-for="(player,index) in players"
               :class="{ activePlayer: player.isActive }">
            <div class="player-name">
              {{ player.name }}: {{ player.roleType }} {{ player.action.actionType }} {{
                player.action.count
              }}
            </div>
            <div class="players-cards">
              <img :src="player.cardsAsImg[0].src" width="40px">
              <img :src="player.cardsAsImg[1].src" width="40px">
            </div>
            <div class="player-chips">
              {{ player.chipsCount }}
              {{ player.timeBank }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

const ADD_PLAYER_PATH = "/addPlayer";

export default {
  name: 'Game',
  props: {
    players: Array,
    flop: Array,
    tern: Object,
    river: Object,
    stomp: Object,
    bank: Object
  },
  methods: {
    joinInQueue() {
      this.stomp.send(ADD_PLAYER_PATH);
    },
    getSource(img) {
      return img.src;
    }
  },
  computed: {
    showPlayers() {
      if (this.players != null) {
        return this.players;
      }
    }
  }

}

</script>

<style>

#main-table {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  height: 60vh;
  width: 75%;
}

#main-deck {
  display: flex;
  width: 100%;
}

#table-img {
  min-width: 769px;
  min-height: 361px;
  align-self: center;
  background: url("/table/table4.png") no-repeat;
  margin-right: 40%;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  flex-wrap: wrap;
}

#table {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  height: 100%;
}

#deck {
  display: flex;
  width: 100%;
  justify-content: center;
}

#upper-players {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 99%;
}

#middle-players-left {
  width: 20%;
  height: 99%;
}

#main-middle-players {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

#middle-players-right {
  display: flex;
  justify-content: flex-end;
  width: 20%;
  height: 99%;
}

#bottom-players {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 99%;
}


.player-name {
  width: 100%;
  text-align: center;
}

.players-cards {
  width: 100%;
  text-align: center;
}

.player-chips {
  width: 80%;
  text-align: center;
}

.player {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  width: 150px;
  height: 100px;
  background-color: gainsboro;
  color: black;
  border-radius: 25px;
}

.activePlayer {
  border: 4px solid black;

}

</style>
