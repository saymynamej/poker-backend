export default class User {
    name;
    isActive;
    chipsCount;
    cards;
    roleType;
    stateType;
    timeBank;
    action;
    cardsAsImg;

    constructor(name, isActive, chipsCount, cards, cardsAsImg, roleType, stateType, timeBank, action) {
        this.name = name;
        this.isActive = isActive;
        this.chipsCount = chipsCount;
        this.cards = cards;
        this.cardsAsImg = cardsAsImg;
        this.roleType = roleType;
        this.stateType = stateType;
        this.timeBank = timeBank;
        this.action = action;
    }
}