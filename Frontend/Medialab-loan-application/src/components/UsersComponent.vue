<script>
export default {
data() {

return {
 users: []
 }
},

mounted() {
    this.getUsers();
},

  methods: {
    getUsers(){
      fetch('http://localhost:9000/users')
      .then(response => response.json())
      .then(data => {
        this.users = data;
        console.log(data);
      })
      .catch(error => {
      console.error(error);
      });
    }
  },
  updated() {
    console.log(this.users);
  }
}

</script>

<template>
    <div>
      <ul>
         <li v-for="user in users" v-bind:key="user">
              <h3>{{ user.username }}</h3>
              <p>{{ user.email }}</p>
              <p>{{ user.role }}</p>
              <hr>
         </li>
          </ul>
    </div>
</template>