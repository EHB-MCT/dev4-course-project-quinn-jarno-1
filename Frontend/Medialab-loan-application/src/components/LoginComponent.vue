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
        }
    },
};
</script>

<template>
    <div>
        <input type="text" v-model="username" placeholder="Username" />
        <input type="password" v-model="password" placeholder="Password" />
        <button @click="login">Login</button>
        <button @click="logout">Logout</button>
    </div>
</template>

<style>
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