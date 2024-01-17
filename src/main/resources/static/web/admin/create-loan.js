const { createApp } = Vue

const options = {
  data() {
    return {
      name:"",
      maxAmount:"",
      payments:"",
      interest:"",
    } // finaliza return
  }, // finaliza data
  methods: {
    createLoan(){
      const paymentArray = this.payments.split(',').map(Number)
      const newLoan =
      {
      "name":this.name,
      "maxAmount":this.maxAmount,
      "payments":paymentArray,
      "interest_rate":this.interest,
      }      
      axios.post("/api/loans/create",newLoan)
        .then(response => {
          console.log(response)
        })
        .catch(error => console.log("Error", error))
    }
  }
} //finaliza createApp

const app = createApp(options)
app.mount('#app')