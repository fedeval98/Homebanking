const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const LOANS = "/api/loans"
const GETLOANS = "/api/loans"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      accounts:[],
      loans:[],
      payments:[],
      loanAmount:"",
      isWideScreen:false,
      successCard:false,
      failureCard:false,
      errormsg:"",
      modalVisibleAlert:false,
      selectedLoan:0,
      selectedAccount:0,
      selectedPayments:0,
      amount:0,
    } // finaliza return
  }, // finaliza data
  created(){
    this.loadData()
    this.loadLoans()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(CLIENTS)
      .then (data => {
        this.client = data.data
        this.accounts = data.data.accounts
        console.log("cliente",this.client)
        console.log("accounts", this.accounts)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))      
    },
    checkScreenSize(){
    this.isWideScreen = window.innerWidth >=1024
    },
    loadLoans(){
    axios.get(GETLOANS)
    .then(response =>{
      this.loans = response.data
      console.log(this.loans)})
    .catch(error => console.log(error))
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
      },
      createLoan(){
        const body =
          {
            "loanId":this.selectedLoan,
            "accountNumber":this.selectedAccount,
            "amount":this.amount,
            "payments": this.selectedPayments
          }
      axios.post(LOANS,body)
      .then(response=>{
        if(response.status.toString().startsWith('2')){
          this.abrirSuccess()
        }
        console.log(response)
      })
      .catch(error => {
        console.log(error.response.data)
        this.errormsg = error.response.data
        this.abrirFailure()
      })
      },
      abrirSuccess(){
        this.successCard = true
        if (this.successCard) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarSuccess(){
        this.successCard = false
        if (this.successCard == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      abrirFailure(){
        this.failureCard = true
        if (this.failureCard) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarFailure(){
        this.failureCard = false
        if (this.failureCard == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      socialmedia(event){
        const facebook = document.querySelector(".facebook")
        const instagram = document.querySelector(".instagram") 
        const github = document.querySelector(".github") 
        const linkedin = document.querySelector(".linkedin")
  
      if(event.target === facebook){
        window.open("https://www.facebook.com/fede.val.9")
      }else if (event.target === instagram){
        window.open("https://www.instagram.com/_fede.val/")
      }else if (event.target === github){
        window.open("https://github.com/fedeval98")
      }else if (event.target === linkedin){
        window.open("https://www.linkedin.com/in/federico-val-ab5484238/")
      }
      },
      searchPayments(){
        const pays = this.loans.find(loan => loan.id == this.selectedLoan)
        this.loanAmount = pays.amount
        this.payments = pays.payments
      },
      formatBudget(balance){
        if(balance !== undefined && balance !== null){
          const sign = balance < 0 ? "-":""
          const formattedBalance = Math.abs(balance).toLocaleString("en-US",{
            style: "currency",
            currency: "USD",
            currencyDisplay:"narrowSymbol",
            minimumFractionDigits: 2,
          })
          return `USD ${sign}${formattedBalance}`
        }
      },
      loanInterest(amount){
        const findLoan = this.loans.find(loan => loan.id == this.selectedLoan)
        console.log(findLoan)
        const interest = findLoan.interest_rate
        console.log(interest)
        console.log(amount)
  
        return amount*(1+(interest/100))
      }
  }, //fin methods
    

  mounted(){
  this.checkScreenSize()
  window.addEventListener('resize', this.checkScreenSize)
  },
  beforeDestroy(){
  window.removeEventListener('resize', this.checkScreenSize)
  }

} //finaliza createApp

const app = createApp(options) 
app.mount('#app')