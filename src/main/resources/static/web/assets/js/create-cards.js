const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const CREATECARD = "/api/clients/current/cards"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      cards:[],
      isWideScreen:false,
      selectedColor: "GOLD",
      selectedType: "DEBIT",
      successCard:false,
      failureCard:false,
      errormsg:"",
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
        this.cards = data.data.cards.sort((a,b)=> a.id -b.id)

        console.log("cliente",this.client)
        console.log("cards",this.cards)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))      
    },
    checkScreenSize(){
    this.isWideScreen = window.innerWidth >=1024
    },
    formatDate(fecha){
      // Obtenengo el año y el mes de la fecha
      const año = fecha.getFullYear()
      const mes = fecha.getMonth()
    
      // Obtengo los últimos dos dígitos del año
      const ultimosDigitosAño = año.toString().slice(-2);
      
      // Formateo la fecha como "MM/YY"
      const fechaFormateada = `${mes}/${ultimosDigitosAño}`
      
    return fechaFormateada
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
      },
      createCard(){
      axios.post(CREATECARD+"?color="+this.selectedColor+"&type="+this.selectedType)
      .then(response=>{
        this.loadData()
        this.abrirSuccess()
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
      createCards(){
        window.location.href='./create-cards.html'
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