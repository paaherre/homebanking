const app = Vue.createApp({
    data() {
        return {
            client: [],
            clientAccounts: "",
            accounts: "",
            email: "",
            password: "",
            showData: false,
        }

    },
    mounted() {
        // Make a request for a user with a given ID
        axios.get('/api/clients/current')
            .then(res => {
                this.client = res.data
                this.accounts = this.client.accounts.length
            })
            .then(res => console.log(this.client))
    },
    methods: {

        crearCuenta() {
            axios.post('/api/clients/current/accounts', { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => location.reload())
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        }
    }
})