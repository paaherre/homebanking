const app = Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            postFname: "",
            postLname: "",
            postEmail: "",
            postPassword: "",
            isUser: true,
        }
    },
    methods: {
        login() {
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password)
                .then(() => {
                    if (this.email == "admin@admin.com") {
                        window.location.href = "/admin.html"
                    } else window.location.href = "/accounts.html"
                })
                .catch(() => swal('Wrong mail or password'))
        },
        register() {
            axios.post('/api/clients', "firstName=" + this.postFname + "&lastName=" + this.postLname + "&email=" + this.postEmail + "&password=" + this.postPassword + "&type=AHORRO", { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => swal('Hi ' + this.postFname + ', Welcome to JD-HomeBanking !!!'))
                .then(() => {
                    this.isUser = !this.isUser
                    axios.post('/api/login', "email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() => window.location.href = "/accounts.html")

                })
                .catch(err => swal('Datos Incorrectos ' + err))

        }
    },
})