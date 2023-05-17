<script>
export default {
    data() {
        return {
            username: '',
            password: '',
        };
    },
    methods: {
        login() {
            const user = {
                username: this.username,
                password: this.password,
            };

            fetch('http://localhost:9000/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user),
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
                        this.$router.push('/items');
                    } else {
                        console.error("Token is empty")
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        },

        logout() {
            localStorage.removeItem('authToken')
        },

        register() {
          this.$router.push('/register');
        }
    },
};
</script>

<template>
  
    <div class="login-container">
      <img alt="Vue logo" class="logo" src="../assets/ehblogo.jpg" width="225" height="225" />
      <div class="login-form">
        <h2>Login</h2>
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" v-model="username" required id="username" placeholder="Username" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" v-model="password" required id="password" placeholder="Password" />
        </div>
        <div class="form-actions">
          <button class="login-button" @click="login">Login</button>
          <button class="login-button" @click="logout">Logout</button>
          <button class="register-button2" @click="register">Register</button>
        </div>
      </div>
    </div>
</template>
  
<style>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 90vh;
  padding: auto;
}

.login-form {
  background-color: #f5f5f5;
  border: 0px solid #ebebeb;
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
input[type="password"] {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login-button,
.register-button2 {
  background-color: #ce0000;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin: 10px;
}

.login-button:hover,
.register-button2:hover {
  background-color: #000000;
}

.register-button2 {
  background-color: #ce0000;
}

.register-button2:hover {
  background-color: #000000;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
}

h2 {
  font-size: 40px;
  margin-bottom: 20px;
  font-weight: bold;
}

h3 {
  font-size: 20px;
  margin-bottom: 5px;
  font-weight: bold;
}

p {
  margin-bottom: 10px;
}

span {
  font-weight: lighter;
  margin-bottom: 5px;
}

.delete-button {
  background-color: hsl(0, 0%, 0%);
  color: rgb(255, 255, 255);
  border: none;
  padding: 8px 16px;
  margin-left: 3px;
  margin-bottom: -10px;
  border-radius: 4px;
  cursor: pointer;
  float: right;
}

.delete-button:hover {
  background-color: #a90000;
}
</style>