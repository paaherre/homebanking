const app = Vue.createApp({
    data() {
        return {
            account: [],
            transactions: [],
            a: 0,
        }
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('id');
        axios.get('/api/accounts/' + myParam)
            .then(res => {
                this.transactions = res.data.transactions
                this.account = res.data
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