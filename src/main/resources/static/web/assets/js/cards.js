const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const CREATECARD = "/api/clients/current/cards"
const DELETECARD = "/api/clients/current/cards/remove"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      cards:[],
      isWideScreen:false,
      modalVisibleAlert: false,
      modalCard:false,
      selectedNumber: -1,
      selectedType: "DEBIT",
      successCard:false,
      failureCard:false,
      errormsg:"",
      deleteCardModal: false,
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
    formatDate(fecha){

      const newDate = new Date(fecha)
      // Obtenengo el año y el mes de la fecha
      const año = newDate.getFullYear()
      const mes = newDate.getMonth() + 1 //agregue un +1 porque getMonth() devuelve 0 en enero.
          
      // Obtengo los últimos dos dígitos del año
      const ultimosDigitosAño = año.toString().slice(-2);
      const mesForamteado = mes < 10 ? `0${mes}`: mes
      // Formateo la fecha como "MM/YY"
      const fechaFormateada = `${mesForamteado}/${ultimosDigitosAño}`
      
    return fechaFormateada
    },
    logout(){
      axios.post(LOGOUT)
      .then (data => {
        window.location.href="/index.html"})
      .catch (error => console.log("Error: ",error))
      },
      abrirCreateCard(){
        this.modalCard = true
        if (this.modalCard) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarCreateCard(){
        this.modalCard = false
        if (this.modalCard == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
      },
      deleteCard(){
      axios.patch(DELETECARD+"?status=ACTIVE"+"&number="+this.selectedNumber)
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
      abrirDelCard(){
        this.deleteCardModal = true
        if (this.deleteCardModal) {
          document.body.classList.add('overflow-y-hidden')
        }
      },
      cerrarDelCard(){
        this.deleteCardModal = false
        if (this.deleteCardModal == false) {
          document.body.classList.remove('overflow-y-hidden')
        }
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