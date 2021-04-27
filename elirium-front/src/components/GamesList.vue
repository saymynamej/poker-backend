<template>
    <div class="list-group ml-2" v-if="games != null && isConnecting">
        <a href="#" class="list-group-item list-group-item-action text-center">Holdem Games</a>

        <div v-for="(game,index) in games" href="#"
             class="list-group-item list-group-item-action list-group-item-dark games d-flex">

            <div class="w-75">
                <div class="game-text">{{ game.name }}</div>
                <div class="game-text">Игроков: {{ game.countPlayers }}</div>
                <div class="game-text">Максиум: {{ game.maxPlayersSize }}</div>
            </div>
            <div class="w-25 d-flex align-items-center flex-wrap">
                <input type="button" @click="joinInGame(index)" style="width: 100%" value="join">
                <input type="button" @click="leaveGame(index)" style="width: 100%" value="leave">
            </div>
        </div>
    </div>
</template>

<script>

    const ADD_PLAYER_IN_GAME_PATH = "/addPlayerInGame";
    const REMOVE_PLAYER_FROM_GAME_PATH = "/leaveGame";

    export default {
        props: {
            games: Array,
            server_messages: Array,
            isConnecting: Boolean,
            stomp: Object,
            clearFunction: Function
        },
        data() {
            return {
                showGames: true,
                showConsole: false
            }
        },
        methods: {
            joinInGame(index) {
                const game = this.games[index];
                this.stomp.send(ADD_PLAYER_IN_GAME_PATH, {}, game.name);
                this.inGame = true;
            },
            leaveGame(index) {
                const game = this.games[index];
                this.stomp.send(REMOVE_PLAYER_FROM_GAME_PATH, {}, game.name);
                this.clearFunction();
            }
        }
    }

</script>
<style>

    .games {
        font-size: 0.6em;
    }
    .game-text {
        text-transform: uppercase;
    }


</style>
