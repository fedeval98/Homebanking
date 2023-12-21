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
    } // finaliza return
  }, // finaliza data
  created() {
    console.log(this.registered)
    console.log(this.email)
    console.log(this.password)
  }, //finaliza created

  methods: {
    login(){
      axios.post("api/login?email=" +this.email +"&password="+this.password)
        .then(response => {
          console.log(response)
          if(response.status == 200){
          window.location.href="/web/accounts.html"
          }else{
            alert("No pudimos iniciar sesion")
          }
          this.clearData()
        })
        .catch(error => console.log("Error", error))
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
    togglePasswordVisibility() {
      // Obtén referencias a los elementos del DOM
      const passwordInput = document.querySelector("#id_password");
      const eye = document.querySelector("#togglePassword");

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
  }, //fin methods
} //finaliza createApp

const app = createApp(options)
app.mount('#app')