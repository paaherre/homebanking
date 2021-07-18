const app = Vue.createApp({
    data() {
        return {
            cardType: "",
            cardColor: "",
        }
    },
    created() {

    },
    methods: {
        createCards() {
            axios.post('/api/cards/create', "type=" + this.cardType.toUpperCase() + "&color=" + this.cardColor.toUpperCase(), { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => window.location.href = "/cards.html")
                .catch(err => swal('No puede crear más tarjetas de este tipo'))
        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        }
    },

})