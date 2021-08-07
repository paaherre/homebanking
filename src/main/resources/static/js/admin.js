const app = Vue.createApp({
    data() {
        return {
            isMenu: false,
            loanName: "",
            maxAmount: 0,
            paymentQuantity: 0,
            payments: [],
            payment: '',
            interest: 0,
        }
    },
    mounted() {

    },
    methods: {
        addPayment() {
            if (this.payment < 1) {
                return swal("Wrong payment")
            }
            parseInt(this.payment)
            this.payments.push(parseInt(this.payment))
            this.payment = ''
        },
        logout() {
            axios.post('/api/logout')
                .then(() => window.location.href = "/index.html")
        },
        reset() {
            this.loanName = ""
            this.maxAmount = 0
            this.paymentQuantity = 0
            this.payments = []
            this.payment = ''
            this.interest = 0
        },
        newLoan() {
            axios.post("/api/admin/loan", { name: this.loanName, maxAmount: parseInt(this.maxAmount), payments: this.payments, interest: parseInt(this.interest) })
                .then(() => swal('Loan created succefully'))
                .then(() => this.reset)
                .catch(() => swal('Incorrect data'))
        },
    },


    computed: {
        paymentsSorted() {
            return this.payments.sort((a, b) => a - b)
        }
    }
})