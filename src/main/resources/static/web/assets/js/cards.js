const CLIENTS = "/api/clients/current"
const LOGOUT = "/api/logout"
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      cards:[],
      isWideScreen:false,
      modalVisibleAlert: false,
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