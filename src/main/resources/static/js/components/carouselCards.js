app.component("cards-carousel", {
  props: {
    cards: {
      type: Array,
      required: true,
    },
    type: {
      type: String,
      required: true,
    },
  },
  template:
    /*html*/    
      `
        <div class="cardsContainerFlex">
            <div v-for="(card, i) in cardsRender(cards)"
            :key="i"
            class="cardContainer"
            :class="{activeCard: i === activeItem}"
            @click="selectItem(i)"
            >
                <div v-if="card != null" class="card">
                    <div class="card__front card__part" :class="cardColor(card.cardColor)">
                        <img class="card__front-square card__square" src="./img/logo-bank.png">

                        <p class="card_numer">{{card.number}}</p>
                        <div class="card__space-75">
                            <span class="card__label">Card holder</span>
                            <p class="card__info">{{card.cardHolder}}</p>
                        </div>
                        <div class="card__space-25">
                            <span class="card__label">Expires</span>
                            <p class="card__info" :class="expiredCard(card.truDate) ? 'expiredCard' : ''">
                                {{expires(card.truDate)}}</p>
                        </div>
                    </div>

                    <div class="card__back card__part " :class="cardColor(card.cardColor)">
                        <div class="card__black-line"></div>
                        <div class="card__back-content">
                            <div class="card__secret">
                                <p class="card__secret--last">{{card.cvv}}</p>
                            </div>
                            <button type="button" class="card__back-square card__square btn-danger"
                                @click="deleteCard(card)">
                                Delete
                            </button>
                            <img class="card__back-logo card__logo" src="./img/banelco.png">

                        </div>
                    </div>
                </div>
                <div v-else >
                    <h3>Add new Card!</h3>
                    <div class="newCard" @click="newCard()">
                        <span>&plus;</span>
                    </div>  
                </div>
            </div>
        </div>
        `,

  data() {
    return {
      activeItem: 0,
    };
  },
  methods: {
    cardsRender() {
      let card = [];
      for (i = 0; i < 3; i++) {
        if (this.cards[i] == null) {
          card[i] = null;
        } else {
          card[i] = this.cards[i];
        }
      }
      return card;
    },
    selectItem(i) {
      this.activeItem = i;
    },
    cardColor(color) {
      return color.toLowerCase();
    },
    expiredCard(date) {
      let dateNow = Date(Date.now());
      let expirationDate = new Date(date).toDateString;
      return expirationDate < dateNow;
    },
    expires(date) {
      return date.split("").splice(2, 5).join("");
    },
    deleteCard(card) {
      this.$emit("delete-card", card);
    },
    newCard() {
      this.$emit("new-card", this.type);
    },
  },
});
