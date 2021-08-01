const app = Vue.createApp({
    data() {
        return {
            isMenu: false,

            /*Accounts*/
            client: "",
            accountsLength: "",
            showData: false,
            accType: "",

            /* Delete Account */
            isDeleteAcc: false,
            deletingAccount: "",

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

        /* Accounts */
        createAccount() {
            if (this.accType == "") {
                return swal('Select account type')
            }
            axios.post('/api/accounts', "type=" + this.accType, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => location.reload())
        },
        deleteAccount(account) {
            if (account.balance != 0) {
                this.isDeleteAcc = true
                this.deletingAccount = account
                return
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

        /* Formats & Order */
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        formatBalance(balance) {
            if (balance == null) {
                return
            }
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
        arraySorted(array) {
            if (this.client != "") {
                return array.sort((a, b) => a.id - b.id)
            }
        },
        maxRange() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].maxAmount
            }

        },
        paymentsByLoan() {
            if (this.loanName != "") {
                return this.loans.filter(e => e.name == this.loanName)[0].payments
            }
        },

        /* Resets */
        resetValues() {
            this.loanAccount = ""
            this.loanAmount = 5000
            this.loanPayment = ""
        },
        resetForm() {
            this.resetValues()
            this.loanName = ""
        },
        aplication() {
            axios.post('/api/loans', { account: this.loanAccount, name: this.loanName, amount: this.loanAmount, payment: this.loanPayment })
                .then(res => swal(res.data))
                .catch(err => swal(err.data))
                .then(window.location.href = "/accounts.html")
            this.resetValues()
            this.loanName = ""
        },

        /* Loans */
        aplicationConfirm() {
            if (this.loanAccount == "" || this.loanName == "" || this.loanAmount == 0 || this.loanPayment == "") {
                return true
            }
            return false
        },
        currentBalance() {
            if (this.loanAccount != "") {
                return this.accounts.filter(e => e.number == this.loanAccount)[0].balance
            }
        },
        loanFees() {
            if (this.loanPayment == "" || this.loanAmount == "") {
                return
            }
            if (this.loanName == "Hipotecario") {
                return this.loanPayment + " Fees  of " + this.formatBalance((this.loanAmount * 1.20) / this.loanPayment)
            }
            if (this.loanName == "Automotriz") {
                return this.loanPayment + " Fees  of " + this.formatBalance((this.loanAmount * 1.25) / this.loanPayment)
            }
            if (this.loanName == "Personal") {
                return this.loanPayment + " Fees  of " + this.formatBalance((this.loanAmount * 1.30) / this.loanPayment)
            }
        },

        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/index.html")
        },
        transfer() {
            window.location.href = "./loan-aplication.html"
        },
        toTransferSection(account) {
            window.location.href = "./transfer.html?transferAcc=" + account;
        },
        toTransactionSection(account) {
            window.location.href = "./transfer.html?transactionAcc=" + account + "#transaction"
        },
        toDeleteTransfer() {
            window.location.href = "./transfer.html?delAcc=" + this.deletingAccount.number + "?" + this.deletingAccount.balance
        }
    },
    computed: {

    }
})
