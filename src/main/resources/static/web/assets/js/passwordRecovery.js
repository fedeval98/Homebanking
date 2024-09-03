const { createApp } = Vue

const options = {
  data() {
    return {
      isWideScreen:false,
      email:"",
      password:"",
      token:"",
      passwordSuccess:false,
      imageUrl: './assets/img/success.webp',
      msgResponse:"",
      msgStatus:"",
    } // finaliza return
  }, // finaliza data
  beforeCreate() {
    console.log("beforeCreate is called");
    const search = location.search;
    const params = new URLSearchParams(search);
    this.token = params.get('token');
    console.log("Token ID:", this.token);
  },
  created() {
    console.log(this.email)
    console.log(this.password)
    this.updateToken()
  }, //finaliza created
  computed: {
    statusClass() {
      return this.msgStatus === 'ok' ? 'text-green-600' : 'text-red-600';
    }
  },
  methods: {
    passwordRecovery(){
      let newPassword ={
        email:this.email,
        password:this.password,
        token:this.token
      }
      axios.post("/api/clients/passwordRecovery", newPassword)
        .then(response => {
          console.log(response)
          this.imageUrl = './web/assets/img/success.webp'
          this.msgStatus = 'ok'
          if(response.status.toString().startsWith('2')){
            this.msgResponse = 'Password change successfully'
          }
          this.abrirPasswordSuccess()
        })
        .catch(error => {
          console.log("Error", error)
          this.imageUrl = './web/assets/img/reject.webp'
          this.msgStatus = 'error'
          if(!error.status.toString().startsWith('2') ){
            this.msgResponse = error.response.data
          }
          this.abrirPasswordSuccess()
        })
    },
    checkOperationResult(isSuccess) {
      if (isSuccess) {
        
      } else {
        this.imageUrl = './web/assets/img/reject.webp'; // Reemplaza con la ruta de tu imagen de error
      }
    },
    togglePasswordVisibility() {
      // Obtén referencias a los elementos del DOM
      const passwordInput = document.querySelector(".id_password")
      const eye = document.querySelector(".togglePassword")

      // Cambia el icono del ojo
      eye.classList.toggle("fa-eye-slash");

      // Verifica el tipo actual del input
      const currentInputType = passwordInput.getAttribute("type")

      // Cambia entre tipo text y tipo password del input
      if (currentInputType === "password") {
        passwordInput.setAttribute("type", "text")
      } else if (currentInputType === "text") {
        passwordInput.setAttribute("type", "password")
      }
    },// fin togglePassword
    updateEmail(event) {
      this.email = event.target.value
      console.log(this.email)
    },
    updatePassword(event) {
      this.password = event.target.value
      console.log(this.password)
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
    checkScreenSize(){      
      if(this.isWideScreen = window.innerWidth >=768){
        this.isWideScreen = true
        document.body.classList.add('overflow-y-hidden')
      } else{
        this.isWideScreen = false
        document.body.classList.remove('overflow-y-hidden')
      }
    },
    updateToken(){
      const search = location.search;
      const params = new URLSearchParams(search);
      this.token = params.get('token');
      console.log("token update: ", this.token)
    },
    abrirPasswordSuccess() {
      this.passwordSuccess = true
      if (this.passwordSuccess) {
        document.body.classList.add('overflow-y-hidden')
      }
    }, // finaliza showModal
    cerrarPasswordSuccess() {
      this.passwordSuccess = false
      if (this.passwordSuccess == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    }, // finaliza showModal
  }, //fin methods
  mounted(){
    this.checkScreenSize()
    window.addEventListener('resize', this.checkScreenSize)
    
    },
    beforeDestroy(){
    window.removeEventListener('resize', this.checkScreenSize)
    },
} //finaliza createApp

const app = createApp(options)
app.mount('#app')