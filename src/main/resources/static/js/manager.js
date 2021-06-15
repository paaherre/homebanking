const app = Vue.createApp({
    data() {
        return {
            clients: [],
            email: "",
            showData: false,

        }

    },
    created() {
        // Make a request for a user with a given ID
        axios.get('/api/clients')
            .then(res => this.clients = res.data)
    },
    methods: {
        showData() {

        }
    },

    computed: {
    }
})