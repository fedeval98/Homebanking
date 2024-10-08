const { createApp } = Vue

const options = {
  data() {
    return {
      email:"",
      password:"",
      registered:false,
      name:"",
      lastName:"",
      modalVisibleAlert: false,
      signupactive:false,
      modalRegistered:false,
      registeredUser:false,
      isWideScreen:false,
      accountType:"-1",
      forgetPassword:false,
      mailSent:false,
    } // finaliza return
  }, // finaliza data
  created() {
    console.log(this.registered)
    console.log(this.email)
    console.log(this.password)
  }, //finaliza created

  methods: {
    login(){
      axios.post("/api/login?email="+this.email+"&password="+this.password)
        .then(response => {
            console.log(response)
            if(this.password =="admin"&&this.email=="admin@admin.com"){
              window.open("./web/admin/create-loan.html")
            }else if(response.status.toString().startsWith('2')){
              window.location.href="/web/accounts.html"
          }
          this.clearData()
          
        })
        .catch(error => {
        console.log("Error", error)
        if(error.response.status.toString().startsWith('4')){
          this.modalRegistered = true
        }

        })
    },
    register(){
      const register =
        {
          "firstName":this.name,
          "lastName":this.lastName,
          "email":this.email,
          "password": this.password,
          "type": this.accountType
        }
      
      axios.post("/api/clients",register)
        .then(response => {
          console.log(response)
          this.abrirModalUserRegistered()
          setTimeout(() => {
            this.cerrarModalUserRegistered()
            this.login()},5000)
        })
        .catch(error => console.log("Error", error))
    },
    passwordRecovery(){
      axios.post("/api/clients/emailSend?email="+this.email)
        .then(response => {
          console.log(response)
          this.cerrarModalPasswordRecovery()
          this.abrirModalMailSent()
        })
        .catch(error => console.log("Error", error))
    },
    swapregister(){
      this.signupactive = !this.signupactive
    },
    clearData(){
      this.email = ""
      this.password = ""
      this.name = ""
      this.lastName = ""
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
    abrirModalRegistered() {
      this.modalRegistered = true
      if (this.modalRegistered) {
        document.body.classList.add('overflow-y-hidden')
      }
    }, // finaliza showModal
    cerrarModalRegistered() {
      this.modalRegistered = false
      if (this.modalRegistered == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },// finaliza cerrarModal
    togglePasswordVisibility() {
      // Obtén referencias a los elementos del DOM
      const passwordInput = document.querySelector(".id_password");
      const eye = document.querySelector(".togglePassword");

      // Cambia el icono del ojo
      eye.classList.toggle("fa-eye-slash");

      // Verifica el tipo actual del input
      const currentInputType = passwordInput.getAttribute("type");

      // Cambia entre tipo text y tipo password del input
      if (currentInputType === "password") {
        passwordInput.setAttribute("type", "text");
      } else if (currentInputType === "text") {
        passwordInput.setAttribute("type", "password");
      }
    },// fin togglePassword
    updateEmail(event) {
      this.email = event.target.value;
      console.log(this.email)
    },
    updatePassword(event) {
      this.password = event.target.value;
      console.log(this.password)
    },
    updateName(event){
      this.name = event.target.value;
      console.log(this.name)
    },
    updateLastName(event){
      this.lastName = event.target.value;
      console.log(this.lastName)
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
    closeModalandSignUp(){
      this.cerrarModalRegistered()
      this.signupactive = true
    },
    abrirModalUserRegistered(){
      this.registeredUser = true
      document.body.classList.add('overflow-y-hidden')
    }, // finaliza showModal
    cerrarModalUserRegistered() {
      this.registeredUser = false
      document.body.classList.remove('overflow-y-hidden')
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
    abrirModalPasswordRecovery() {
      this.forgetPassword = true
      if (this.forgetPassword) {
        document.body.classList.add('overflow-y-hidden')
      }
    }, // finaliza Abrir Modal PasswordRecovery
    cerrarModalPasswordRecovery() {
      this.forgetPassword = false
      if (this.forgetPassword == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    }, // finaliza Cerrar Modal PasswordRecovery
    abrirModalMailSent() {
      this.mailSent = true
      if (this.mailSent) {
        document.body.classList.add('overflow-y-hidden')
      }
    }, // finaliza MAILSENT
    cerrarModalMailSent() {
      this.mailSent = false
      if (this.mailSent == false) {
        document.body.classList.remove('overflow-y-hidden')
      }
    },// finaliza cerrarMAILSENT
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