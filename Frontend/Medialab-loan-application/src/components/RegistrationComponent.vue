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
      }
    }
  };
  </script>
  <template>
    <div>
      <h2>Registration Form</h2>
      <form @submit.prevent="registerUser">
        <div>
          <label for="username">Username:</label>
          <input type="text" id="username" v-model="username" required>
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" v-model="password" required>
        </div>
        <div>
          <label for="email">Email:</label>
          <input type="email" id="email" v-model="email" required>
        </div>
        <div>
          <label for="role">Role:</label>
          <select id="role" v-model="role" required>
            <option value="">Select Role</option>
            <option value="admin">Admin</option>
            <option value="user">User</option>
          </select>
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  </template>