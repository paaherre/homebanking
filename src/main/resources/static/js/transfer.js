const app = Vue.createApp({
    data() {
        return {
            client: "",
            isOwner: "none",
            fromAccount: "",
            toAccount: "",
            transferAmount: "",
            transferDesc: "",
        }
    },
    created() {
        axios.get('/api/clients/current')
            .then(res => {
                this.client = res.data
            })
    },
    methods: {

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        },
        transfer() {

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
        availableAcc() {
            if (this.client != "") {
                return this.client.accounts.filter(e => e.number != this.fromAccount)
            }
        },
        resetAcc() {
            this.fromAccount = ""
            this.toAccount = ""
            this.transferAmount = ""
            this.transferDesc = ""
        },
        resetTransfer() {
            this.isOwner = "none"
            this.resetAcc()
        },
        transferPost() {
            axios.post('/api/transfer', "amount=" + this.transferAmount + "&description=" + this.transferDesc + "&fromNumber=" + this.fromAccount + "&toNumber=" + this.toAccount, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => swal('Transferencia exitosa'))
                .catch(err => swal('No se pudo procesar la transferencia ' + err))
            this.resetTransfer()
        }
    },
    computed: {
        availableAmount() {
            if (this.fromAccount != "") {
                return this.client.accounts.filter(e => e.number == this.fromAccount)[0].balance
            }
        },
        transferData() {
            if (
                this.fromAccount == "none" ||
                this.toAccount == "" ||
                this.transferAmount == "" ||
                this.transferDesc == "") {
                return true
            }
            return false
        }
    }
})