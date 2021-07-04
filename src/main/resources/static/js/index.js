//axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!'))
//axios.post('/api/logout').then(response => console.log('signed out!!!'))
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
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => window.location.href = "/accounts.html")
                .catch(err => swal('Usuario o Contraseña incorrecto'))
        },
        dataPost() {
            axios.post('/api/clients', "firstName=" + this.postFname + "&lastName=" + this.postLname + "&email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => swal('Se creó correctamente el usuario! Bienvenido a JDBank!!!'))
                .then(res => {
                    axios.post('/api/login', "email=" + this.postEmail + "&password=" + this.postPassword, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => window.location.href = "/accounts.html")
                    this.isUser = !this.isUser
                })
                .catch(err => swal('Datos Incorrectos ' + err))

        }
    },
})
