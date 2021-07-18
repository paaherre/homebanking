const app = Vue.createApp({
    data() {
        return {
            isMenu: false,

            /*Accounts*/
            client: "",
            accountsLength: "",
            showData: false,
            accType: "",

            /* Loans*/
            accounts: "",
            loans: "",
            loanAccount: "",
            loanName: "",
            loanAmount: 5000,
            loanPayment: "",

            isLoansModal: false,
        }

    },
    mounted() {
        axios.get('/api/clients/current')
            .then(res => {
                this.client = res.data
                this.accounts = res.data.accounts
                this.accountsLength = this.client.accounts.length
            })
        axios.get('/api/loans')
            .then(res => {
                this.loans = res.data
            })
    },
    methods: {
        createAccount() {
            if (this.accType == "") {
                return swal('Select account type')
            }
            axios.post('/api/accounts', "type=" + this.accType, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => location.reload())
        },
        deleteAccount(account) {
            if (account.balance != 0) {
                return swal('Account Balance must be $ 0.00')
            }
            swal({
                title: "Are you sure?",
                text: "Once deleted, you will not be able to recover this Account!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        axios.delete('/api/clients/current/accounts/' + account.number, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                            .then(() => swal("The Account has been deleted", {
                                icon: "success",
                            }))
                            .then(() => location.reload())
                            .catch(err => console.log(err))
                    }
                });
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        formatBalance(balance) {
            let amount = new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
            })
            return amount.format(balance)
        },
        accountType(type) {
            if (type == "AHORRO") {
                return "Saving Account"
            } else {
                return "Checking Account"
            }
        },
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
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/index.html")
        },
        arraySorted(array) {
            if (this.client != "") {
                return array.sort((a, b) => a.id - b.id)
            }
        },
        transfer() {
            window.location.href = "./loan-aplication.html"
        },
    },
    computed: {

    }
})
