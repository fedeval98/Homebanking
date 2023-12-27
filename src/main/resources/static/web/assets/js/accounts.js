const CLIENT = "/api/clients/current"
const LOGOUT = "/api/logout"
const CREATEACCOUNT = "/api/clients/current/accounts"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      accounts:[],
      isWideScreen:false,
      modalVisibleAlert: false,
      loans:[],
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
    axios.post(CREATEACCOUNT)
    .then(response => console.log(response))
    .catch(response => console.log(error))
    window.location.reload()
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