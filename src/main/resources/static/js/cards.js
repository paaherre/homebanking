const app = Vue.createApp({
    data() {
        return {
            isMenu: "",
            cards: "",

            /* Create New Card*/
            isNewCardModal: false,
            cardType: "",
            cardColor: "",

            /* Delete Card */
            isDeleteCardModal: false,
            deleteCardNumber: "",
            deleteCardId: "",

        }
    },
    created() {
        axios.get('/api/clients/current')
            .then(res => {
                this.cards = res.data.cards
            })
    },
    methods: {
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "./index.html")
        },
        cardsByType(type) {
            if (this.cards.length < 1) {
                return []
            }
            return this.cards.filter(e => e.cardType == type)
        },
        newCardModal(type) {
            this.cardType = type
            this.isNewCardModal = true
        },

        createCard() {
            axios.post('/api/cards/create', "type=" + this.cardType.toUpperCase() + "&color=" + this.cardColor.toUpperCase(), { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => swal('Card created successfully'))
                .then(() => window.location.href = "/cards.html")
                .catch(() => swal('You cannot create more cards'))
        },

        deleteCard(card) {
            this.isDeleteCardModal = true
            this.deleteCardId = card.id
            this.deleteCardNumber = card.number
        },
        deleteCardCancel() {
            this.deleteCardId = ""
            this.deleteCardNumber = ""
        },
        deleteCardConfirm() {
            axios.delete('/api/cards/delete/' + this.deleteCardId)
                .then(() => swal('Card deleted'))
                .then(() => this.deleteCardCancel)
                .then(() => location.reload())

                .catch(err => {
                    swal(err)
                    this.deleteCardCancel
                    location.reload()
                })
        },
        expiredCard(date) {
            let dateNow = Date(Date.now())
            let expirationDate = new Date(date).toDateString
            return expirationDate < dateNow
        },


    },
    computed: {

    }
})