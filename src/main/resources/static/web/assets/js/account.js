const CLIENTS = "/api/clients/"
const ACCOUNTS = "/api/accounts/"
const TRANSACTION = "/transactions"
const {createApp} = Vue

const options = {
  data(){
    return {
      idclient:1,
      idAccount:null,
      client: [],
      accounts:[],
      account:[],
      transaction:[],
      isWideScreen:false,
      modalVisibleAlert: false,
    } // finaliza return
  }, // finaliza data
  created(){
    const search = location.search
    const params = new URLSearchParams(search)
    this.idAccount = params.get('id')
    this.loadData()
    this.loadTransaction()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(CLIENTS + this.idclient)
      .then (data => {
        this.client = data.data
        this.accounts = data.data.accounts
        console.log(this.idAccount)
        console.log("clientes",this.client)
        console.log("cuentas",this.accounts)
        this.account = this.accounts.find(account => account.id == this.idAccount)
        console.log("cuenta: ",this.account)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))      
    },
    loadTransaction(){
      axios.get(ACCOUNTS + this.idAccount + TRANSACTION)
        .then (data => {
          this.transaction = []
          this.transaction = data.data
          this.transaction.sort((a,b) =>{
            return b.id - a.id
          })
          console.log("transaccion",this.transaction)
        })
        .catch (error => console.log("Error: ",error))
    },
    formatBudget(balance){
      if(balance !== undefined && balance !== null){
        const sign = balance < 0 ? "-":""
        const formattedBalance = Math.abs(balance).toLocaleString("es-MX",{
          style: "currency",
          currency: "ARS",
          currencyDisplay:"narrowSymbol",
          minimumFractingDigits: 0,
        })
        return `ARS ${sign}${formattedBalance}`
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
      return release.toLocaleDateString("es-ES",options)
    },
    formatDateTime(array){
      // se formatea la fecha de las peliculas usando un formato
      // en este caso utiliza el formato de fecha/hora local
      // dado que se usa .toLocaleDateString y se establece
      // la configuracion regional "es-ES" y se le da el formato
      // options (dia:numerico, mes:texto, año:numerico)
      // ejemplo (11 julio de 1998)
      const options = {day:'numeric', month:'long', year:'numeric', hour:'numeric', minute:'numeric', second:'numeric',}
      const release = new Date(array.dateTime)
      return release.toLocaleString("es-ES",options)
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