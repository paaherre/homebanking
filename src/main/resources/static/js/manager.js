const app = Vue.createApp({
    data() {
        return {
            clients: [],
            client: [],
            clientAccounts: [],
            accounts: [],
            cards: [],
            loans: [],
            email: "",
            password: "",
            showData: false,
            postFirstName: "",
            postLastName: "",
            postEmail: "",
            postBalance: "",
            postPassword: "",
            restResponse: "",

        }

    },
    created() {
        // Make a request for a user with a given ID
        axios.get('/api/clients')
            .then(res => this.clients = res.data)
    },
    methods: {
        showAccounts() {
            let client = this.clients.filter(e => e.email == this.email)
            axios.get('/api/clients/' + client[0].id)
                .then(res => {
                    this.clientAccounts = res.data
                    if (this.password == this.clientAccounts.password) {
                        this.client = this.clients.filter(e => e.email == this.email)[0]
                        this.showData = !this.showData
                    } else {
                        swal('Contraseña incorrecta...')
                    }
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        dataPost() {
            axios.post('/api/clients', {
                firstName: this.postFirstName,
                lastName: this.postLastName,
                email: this.postEmail,
                password: this.postPassword
            })
        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        }
    }
})