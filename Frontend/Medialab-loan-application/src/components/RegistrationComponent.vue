<script>
  export default {
    data() {
      return {
        username: '',
        password: '',
        email: '',
        role: '',
        accessToken: ''
      };
    },
    methods: {
      registerUser() {
        const newUser = {
                username: this.username,
                password: this.password,
                email: this.email,
                role: this.role
            };
        fetch('http://localhost:9000/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newUser),
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        throw new Error('Invalid credentials');
                    }
                })
                .then(token => {
                    console.log('Authentication token:', token);
                    localStorage.setItem('authToken', token);
                    if(token) {
                        this.$router.push('/');
                    } else {
                        console.error("Token is empty")
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        const userData = {
          username: this.username,
          password: this.password,
          email: this.email,
          role: this.role
        };
        console.log(userData);
      },
      back(){
        this.$router.push('/');
      }
    }
  };
  </script>
  <template>
    <div class="registration-container">
      <form class="registration-form" @submit.prevent="registerUser">
        <h2>Register</h2>
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" id="username" placeholder="Username" v-model="username" required>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" id="password" placeholder="password" v-model="password" required>
        </div>
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" id="email" placeholder="email" v-model="email" required>
        </div>
        <div class="form-actions">
          <button class="back-button" @click="back">Back</button>
          <button type="submit" class="register-button">Register</button>
        </div>
      </form>
    </div>
  </template>
  
  <style>
  .registration-container {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
  }
  
  .registration-form {
    background-color: #f4f4f4;
    padding: 20px;
    border-radius: 4px;
    width: 300px;
  }
  
  h2 {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  input[type="text"],
  input[type="password"],
  input[type="email"],
  select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin: 5px;
  }
  
  .form-actions {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
  }
  
  .register-button {
    background-color: #ce0000;
    color: #fff;
    border: none;
    padding: 8px 60px;
    border-radius: 4px;
    cursor: pointer;
    margin: 10px;
  }

  .back-button {
    background-color: #ce0000;
    color: #fff;
    border: none;
    padding: 8px 12px;
    border-radius: 4px;
    cursor: pointer;
    margin: 10px;
    margin-right: 12px;
  }
  
  .register-button:hover,
  .back-button:hover {
    background-color: #000000;
  }
  </style>
  