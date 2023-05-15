<script>

export default {

data() {

return {

 items: []

 }
},

mounted() {

 this.getItems();

 },
 methods: {
  getItems(){
    fetch('http://localhost:9000/items')
    .then(response => response.json())
    .then(data => {
      this.items = data;
      console.log(data);
    })
    .catch(error => {
    console.error(error);
    });
  },

  deleteItem(itemId) {
    fetch(`http://localhost:9000/items/${itemId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(response => {
        console.log(response);
      })
      .catch(error => {
        console.error(error);
      });
  }

 },

 updated() {
  
 console.log(this.items);
}
 }

</script>

<template>
  <h2>Items overview</h2>
    <div class="item">
          <ul>
            <li v-for="item in items" v-bind:key="item">
              <h3>{{ item.name }}</h3>
              <p>{{ item.description }}</p>
              <span v-if="item.isLoanedOut">Reserved: True</span>
              <span v-else>Reserved: False</span>
              <button class="delete-button" @click="deleteItem(item.id)">Delete</button>
              <hr>
            </li>
          </ul>
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