const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const {createApp} = Vue

const options = {
  data(){
    return {
      idAccount:null,
      client: [],
      accounts:[],
      account:[],
      transaction:[],
      isWideScreen:false,
      modalVisibleAlert: false,
      dateStart:"",
      dateEnd:"",
      downloadModal:false
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
      axios.get(CLIENTS)
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
      axios.get(CLIENTS)
        .then (data => {
          this.transaction = []
          this.transaction = this.account.transactions
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
    formatDateTime(array){
      // se formatea la fecha de las peliculas usando un formato
      // en este caso utiliza el formato de fecha/hora local
      // dado que se usa .toLocaleDateString y se establece
      // la configuracion regional "es-ES" y se le da el formato
      // options (dia:numerico, mes:texto, año:numerico)
      // ejemplo (11 julio de 1998)
      const options = {day:'numeric', month:'long', year:'numeric', hour:'numeric', minute:'numeric', second:'numeric',}
      const release = new Date(array.dateTime)
      return release.toLocaleString("en-US",options)
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
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
      download(){
        const dateStart = new Date(this.dateStart)
        const formattedStart = dateStart.toISOString().slice(0, -5)
        const dateEnd = new Date(this.dateEnd)
        const formattedEnd = dateEnd.toISOString().slice(0, -5)
        axios.get("/api/accounts/"+this.idAccount+"/transactions/pdf?dateTime="+formattedStart+"&endDate="+formattedEnd)
        .then(response => {
          if(response.status.toString().startsWith('2')){
            window.open("../api/accounts/"+this.idAccount+"/transactions/pdf?dateTime="+formattedStart+"&endDate="+formattedEnd)
            console.log(response)
          }})
          .catch(error => console.log(error))
      },
      abrirDownload(){
        this.downloadModal = true
        if (this.downloadModal) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarDownload(){
        this.downloadModal = false
        if (this.downloadModal == false) {
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