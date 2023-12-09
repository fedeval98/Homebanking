const API = "/api/clients/"
const ID = 1
const {createApp} = Vue

const options = {
  data(){
    return {
      client: [],
      accounts:[],
      isWideScreen:false,
    } // finaliza return
  }, // finaliza data

  created(){
    this.loadData()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(API + ID)
      .then (data => {
        this.client = data.data
        this.accounts = data.data.accounts
        console.log(this.client)
        console.log(this.accounts)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))
    },
    formatBudget(balance){
      if(balance !== undefined && balance !== null){
        return balance.toLocaleString("en-US",{
          style: "currency",
          currency: "ARS",
          minimumFractingDigits: 0,
        })
      }
    },
    checkScreenSize(){
    this.isWideScreen = window.innerWidth >=768
    }
  }, //fin methods
  mounted(){
  this.checkScreenSize()
  window.addEventListener('resize', this.checkScreenSize)
  },
  beforeDestroy(){
  window.removeEventListener('risize', this.checkScreenSize)
  }

} //finaliza createApp

const app = createApp(options) 
app.mount('#app')