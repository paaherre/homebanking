const app = Vue.createApp({
    data() {
        return {
            transactions: [],
            a: 0,
        }
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('number');
        axios.get('/api/clients/current')
            .then(res => {
                this.transactions = res.data.accounts.filter(e => e.number == myParam)[0].transactions.sort((a, b) => b.id - a.id)
            })
    },
    methods: {
        transactionDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        }
    },
    computed: {
    }
})