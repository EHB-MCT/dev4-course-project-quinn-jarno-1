<!-- eslint-disable no-irregular-whitespace -->

<script>
export default {
data() {

return {

 users: []

 }

},

// eslint-disable-next-line no-irregular-whitespace


mounted() {
// Make API request to fetch user data
 fetch('http://localhost:9000/users')
 .then(response => response.json())
.then(data => {
 // Store response data in component's data object
this.users = data;
 console.log(data);
})

 .catch(error => {

 // Handle any errors that occur during the API request
 console.error(error);

 });

 },


 updated() {

// Check that users data is being properly stored in component's data object

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