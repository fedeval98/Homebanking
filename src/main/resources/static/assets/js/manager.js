const API = "http://localhost:8080/clients"

const {createApp} = Vue

const options = {
  data(){
    return {
      clients: [],
      rawData:[],
      firstName:"",
      lastName:"",
      email:"",
    } // finaliza return
  }, // finaliza data

  created(){
    this.loadData()
  }, //finaliza created

  methods:{
    loadData(){
      axios.get(API)
      .then (data => {
        this.rawData = data.data
        this.clients = data.data._embedded.clients
        console.log(this.clients)
      }) // finaliza then data
      .catch (error => console.log ("Error: ",error))
    },
    addClient(event){
      event.preventDefault()
      const newClient = {
        "firstName": this.firstName,
        "lastName": this.lastName,
        "email": this.email,
      }
      axios.post(API, newClient)
      .then( response => {
        this.clients = response
        this.loadData()
        this.cleanInputs()
      })
      .catch(error => console.log("Error: ",error))
    },
    cleanInputs(){
    this.firstName = ""
    this.lastName = ""
    this.email = ""
    },
  }, //fin methods
} //finaliza createApp

const app = createApp(options) 
app.mount('#app')



