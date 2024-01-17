const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const TRANSFER = "/api/transactions"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      accounts:[],
      modalVisibleAlert:false,
      isWideScreen:false,
      selectedColor: "GOLD",
      selectedType: "DEBIT",
      verifyTransfer:false,
      success:false,
      failure:false,
      errormsg:"",
      fromAccount:0,
      toAccount:0,
      amount:0,
      description:"",
      accountBalance:"",
      accountBalance2:"",
      isPersonal:false,
      isOther:false,
      availableAccounts:"",
    } // finaliza return
  }, // finaliza data
  created(){
    this.loadData()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(CLIENTS)
      .then (data => {
        this.client = data.data
        this.accounts = data.data.accounts.sort((a,b)=> a.id-b.id)
        console.log("cliente",this.client)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))      
    },
    accountCheck(){
      this.accountBalance = this.accounts.find(account => account.number == this.fromAccount)
      console.log(this.accountBalance)
      this.transactionType()
    },
    accountCheck2(){
      this.accountBalance2 = this.accounts.find(account => account.number == this.toAccount)
      console.log(this.accountBalance)
      this.transactionType()
    },
    checkScreenSize(){
    this.isWideScreen = window.innerWidth >=1024
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
      },
      abrirSuccess(){
        this.success = true
        if (this.success) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarSuccess(){
        this.success = false
        if (this.success == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      abrirFailure(){
        this.failure = true
        if (this.failure) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarFailure(){
        this.failure = false
        if (this.failure == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      abrirVerify(){
        this.verifyTransfer = true
        if (this.verifyTransfer) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarVerify(){
        this.verifyTransfer = false
        if (this.verifyTransfer == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      abrirAlert() {
        this.modalVisibleAlert = true
        if (this.modalVisibleAlert) {
          document.body.classList.add('overflow-y-hidden')
        }
      }, // finaliza showModal
      cerrarAlert() {
        this.modalVisibleAlert = false
        if (this.modalVisibleAlert == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },// finaliza cerrarModal
    
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
      transactionType(){
        const personal = document.getElementById('personal')
        const other = document.getElementById('other')

        if(personal.checked){
          this.isPersonal = true
          this.isOther = false
          this.availableAccounts = this.accounts.filter(account => account.number !== this.fromAccount)
        } else if(other.checked){
            this.isOther = true
            this.isPersonal = false
          }
      },
      confirmTransaction(){
        if(this.isPersonal){
          const body ={
            "amount":this.amount,
            "descriptions":this.description,
            "sourceAccountNumber":this.fromAccount,
            "targetAccountNumber":this.toAccount
          }
          axios.post(TRANSFER,body)
          .then(response =>{ 
            console.log(response)
            this.cerrarVerify()
            this.abrirSuccess()
            this.fromAccount=""
            this.toAccount=""
            this.amount=0
            this.description=""
          this.loadData()
          })
          .catch(error => {
            console.log(error)
            this.abrirFailure()
            this.cerrarVerify()
            this.errormsg = error.response.data
          })
        } else if(this.isOther){
          const body ={
            "amount":this.amount,
            "descriptions":this.description,
            "sourceAccountNumber":this.fromAccount,
            "targetAccountNumber":"VIN"+this.toAccount
          }
          axios.post(TRANSFER,body)
          .then(response =>{ 
            console.log(response)
            this.cerrarVerify()
            this.abrirSuccess()
            this.fromAccount=""
            this.toAccount=""
            this.amount=0
            this.description=""
          this.loadData()
          })
          .catch(error => {
            console.log(error)
            this.abrirFailure()
            this.cerrarVerify()
            this.errormsg = error.response.data
          })
        }
      },
      maxAmount(){
        this.amount = this.accountBalance.balance
      },
      validateAmount() {
        const regex = /^[0-9]+(\.[0-9]{1,2})?$/;
        if (!regex.test(this.amount)) {
          this.amount = this.amount.slice(0, -1); // Eliminar el último caracter si no es válido
        }
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