const app = Vue.createApp({
    data() {
        return {
            client: [],
            isOwner: "",
            fromAccount: "",
            toAccount: "",
            transferAmount: "",
            transferDesc: "",
            account: "",
        }
    },
    created() {
        axios.get('/api/clients/current')
            .then(res => {
                this.client = res.data
                console.log(this.client)
            })
    },
    methods: {

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        },
        validar() {
            console.log(this.isOwner)
        },
        disabledFromAcc(a) {
            if (a.number == this.toAccount) {
                return true;
            }
            return false;
        },
        disabledToAcc(a) {
            if (a.number == this.fromAccount) {
                return true;
            }
            return false;
        },
    },
    computed: {

    }
})