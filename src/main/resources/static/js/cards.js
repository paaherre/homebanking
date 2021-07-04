const app = Vue.createApp({
    data() {
        return {
            debit: [],
            credit: [],
            maxCards: "",
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
                .then(response => window.location.href = "/index.html")
        },
        prueba() {
            console.log(this.debit.length + this.credit.length)
        }
    },


})