const app = Vue.createApp({
    data() {
        return {
            isMenu: false,
            client: "",
            isOwner: "",
            fromAccount: "",
            toAccount: "",
            destinationAccount: "",
            transferAmount: "",
            transferDesc: "",
            transAccount: "",
            fromDate: "",
            toDate: "",
            transactions: "",

        }
    },
    created() {
        axios.get('/api/clients/current')
            .then(res => {
                this.client = res.data
            });
        let params = new URLSearchParams(document.location.search.substring(1));
        let transactionAccount = params.get("transactionAcc")
        let transferAccount = params.get("transferAcc")
        let accDelete = params.get("delAcc")

        if (transferAccount != null) {
            this.isOwner = 'own'
            this.fromAccount = transferAccount
        }
        if (transactionAccount != null) {
            this.transAccount = transactionAccount
            this.transactionFilter()
            this.getTransactions()
        }
        if (accDelete != null) {
            this.fromAccount = accDelete.split("?")[0]
            this.transferAmount = accDelete.split("?")[1]
            this.isOwner = 'own'
        }


    },
    methods: {

        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/index.html")
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
        resetToAccount() {
            this.toAccount = ""
            this.destinationAccount = "";
        },
        resetAcc() {
            this.fromAccount = ""
            this.toAccount = ""
            this.transferAmount = ""
            this.transferDesc = ""
            this.destinationAccount = ""
        },
        resetTransfer() {
            this.isOwner = ""
            this.resetAcc()

        },
        transferValidation() {
            axios.post("/api/transfer/validation", "toNumber=" + this.toAccount, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => this.destinationAccount = res.data)
                .catch(err => console.log(err))
        },
        transferPost() {
            axios.post('/api/transfer', "amount=" + this.transferAmount + "&description=" + this.transferDesc + "&fromNumber=" + this.fromAccount + "&toNumber=" + this.toAccount, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => swal(res.data))
                .catch(err => swal(err.response.data))
            this.resetTransfer()
        },
        transactionDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        transactionFilter() {
            let date = new Date(Date.now())
            date.setDate(date.getDate() - 3)
            this.fromDate = date.toLocaleDateString('en-gb').split("/").reverse().join("-")
            this.getTransactions()
        },
        getTransactions() {
            if (this.fromDate == "" || this.transAccount == "") {
                return swal("From Date and Account can't be null")
            }
            if (this.toDate == "") {
                this.toDate = new Date(Date.now()).toLocaleDateString('en-gb').split("/").reverse().join("-")
            }
            axios.post('./api/transactions', {
                desdeFecha: this.fromDate.replace('/', '-'),
                hastaFecha: this.toDate.replace('/', '-'),
                accountNumber: this.transAccount
            })
                .then(res => this.transactions = res.data)
                .then(() => {
                    if (this.transactions == "") {
                        swal('There is no transactions between selected dates')
                    }
                })
                .catch(err => console.log(err))
        },
        getTransactionsToPDF() {
            if (this.fromDate == "" || this.transAccount == "") {
                return swal("From Date and Account can't be null")
            }
            if (this.toDate == "") {
                this.toDate = new Date(Date.now()).toLocaleDateString('en-gb').split("/").reverse().join("-")
            }
            axios({
                url: 'api/transaction/export/pdf',
                method: 'POST',
                responseType: 'blob',
                data: {
                    desdeFecha: this.fromDate.replace('/', '-'),
                    hastaFecha: this.toDate.replace('/', '-'),
                    accountNumber: this.transAccount
                }
            })
                .then((response) => {
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', 'Transactions_from_' + this.fromDate + '_to_' + this.toDate + '.pdf');
                    document.body.appendChild(link);
                    link.click();
                })
                .catch(err => swal(err))
        },
        transArraySorted() {
            return this.transactions.sort((a, b) => b.transactionDate - a.transactionDate)
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
        },
        balanceFromAccount() {
            if (this.fromAccount == "") {
                return
            }
            return this.client.accounts.filter(a => a.number == this.fromAccount)[0].balance
        },
        afterBalance() {
            return this.balanceFromAccount - this.transferAmount
        }
    }
})