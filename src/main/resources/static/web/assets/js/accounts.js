const CLIENT = "/api/clients/current"
const LOGOUT = "/api/logout"
const CREATEACCOUNT = "/api/clients/current/accounts?type="
const REMOVEACCOUNT = "/api/clients/current/account/remove?id="
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      accounts:[],
      isWideScreen:false,
      modalVisibleAlert: false,
      loans:[],
      balance:"",
      accountForm:false,
      delform:false,
      accountType:"",
      accountNumber:-1,
      accountDelete:false,
      accountCreate:false,
    } // finaliza return
  }, // finaliza data
  created(){
    this.loadData()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(CLIENT)
      .then (data => {
        this.client = data.data
        this.accounts = data.data.accounts
        this.accounts.sort((a,b)=> a.id - b.id)
        this.loans = data.data.loans
        console.log("cliente",this.client)
        console.log("cuentas",this.accounts)
        console.log("prestamos",this.loans)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))      
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
        this.balance = formattedBalance
        return `USD ${sign}${formattedBalance}`
      }
    },
    checkScreenSize(){
    this.isWideScreen = window.innerWidth >=1024
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
    formatDate(array){
      // se formatea la fecha de las peliculas usando un formato
      // en este caso utiliza el formato de fecha/hora local
      // dado que se usa .toLocaleDateString y se establece
      // la configuracion regional "es-ES" y se le da el formato
      // options (dia:numerico, mes:texto, año:numerico)
      // ejemplo (11 julio de 1998)
      const options = {day:'numeric', month:'long', year:'numeric'}
      const release = new Date(array.creationDate)
      return release.toLocaleDateString("en-US",options)
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
      },
    newAccount(){
    axios.post(CREATEACCOUNT+this.accountType)
    .then(response => {
      if(response.status.toString().startsWith('2')){
        this.cerrarAccountForm()
        this.abrirAccountCreated()
        this.loadData()
      }
      console.log(response)})
      .catch(response => console.log(error))
    // window.location.reload()
    },
    deleteAccount(){
      axios.patch(REMOVEACCOUNT+this.accountNumber)
      .then(response => {
        if(response.status.toString().startsWith('2')){
          this.cerrardelform()
          this.abrirAccountDelete()
          this.loadData()
        }
        console.log(response)})
        .catch(error => console.log(error))
      // window.location.reload()
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
    abrirAccountCreated(){
      this.accountCreate = true
      if (this.accountCreate) {
        document.body.classList.add('overflow-y-hidden')
      }
    },
    cerrarAccountCreated(){
      this.accountCreate = false
      if (this.accountCreate == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },
    abrirAccountDelete(){
      this.accountCreate = true
      if (this.accountCreate) {
        document.body.classList.add('overflow-y-hidden')
      }
    },
    cerrarAccountDelete(){
      this.accountCreate = false
      if (this.accountCreate == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },
    abrirAccountForm(){
      this.accountForm = true
      if (this.accountForm) {
        document.body.classList.add('overflow-y-hidden')
      }
    },
    cerrarAccountForm(){
      this.accountForm = false
      if (this.accountForm == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },
    abrirdelform(){
      this.delform = true
      if (this.delform) {
        document.body.classList.add('overflow-y-hidden')
      }
    },
    cerrardelform(){
      this.delform = false
      if (this.delform == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },
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