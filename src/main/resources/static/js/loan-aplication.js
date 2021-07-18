const app = Vue.createApp({
    data() {
        return {
            accounts: "",
            loans: "",
            loanAccount: "",
            loanName: "",
            loanAmount: 5000,
            loanPayment: "",
        }
    },
    created() {
        axios.get('/api/loans')
            .then(res => {
                this.loans = res.data
                console.log(res.data)
            })
        axios.get('/api/clients/current')
            .then(res => {
                this.accounts = res.data.accounts
                console.log(res.data.accounts)
            })
    },
    methods: {
        aplication() {
            axios.post('/api/loans', { account: this.loanAccount, name: this.loanName, amount: this.loanAmount, payment: this.loanPayment })
                .then(res => swal(res.data))
                .catch(err => swal(err.data))
                .then(window.location.href = "/accounts.html")
            this.resetValues()
            this.loanName = ""
        },
        maxRange() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].maxAmount
            }

        },
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/index.html")
        },
        resetValues() {
            this.loanAccount = ""
            this.loanAmount = 5000
            this.loanPayment = ""
        },
        paymentsByLoan() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].payments
            }
        },
        resetForm() {
            this.resetValues()
            this.loanName = ""
        },
        aplicationConfirm() {
            if (this.loanAccount == "" || this.loanName == "" || this.loanAmount == 0 || this.loanPayment == "") {
                return true
            }
            return false
        }
    }

})