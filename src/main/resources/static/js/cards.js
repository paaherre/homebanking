const app = Vue.createApp({
    data() {
        return {
            debit: [],
            credit: [],
            maxCards: "",
            deleteCardId: "",
            deleteCardNumber: "",
        }
    },
    created() {
        let cards = []
        axios.get('/api/clients/current')
            .then(res => {
                cards = res.data.cards
                this.debit = cards.filter(e => e.cardType == "DEBITO")
                this.credit = cards.filter(e => e.cardType == "CREDITO")
                this.maxCards = this.credit.length + this.debit.length
            })
    },
    methods: {
        expires(date) {
            return date.split("").splice(2, 5).join("");
        },
        typeCard(color) {
            return color.toLowerCase()
        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "./index.html")
        },
        prueba() {
            console.log(this.debit.length + this.credit.length)
        },
        deleteCard(card) {
            this.deleteCardId = card.id
            this.deleteCardNumber = card.number

        },
        deleteCardCancel() {
            this.deleteCardId = ""
            this.deleteCardNumber = ""
        },
        deleteCardConfirm() {
            axios.delete('/api/cards/delete/' + this.deleteCardId)
                .then(res => {
                    swal(res.data)
                    this.deleteCardCancel
                    location.reload()
                })
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
        }

    },


})