<script>
export default {
data() {
  return {
  searchQuery: '',
  items: [],
  filteredItems: [],
  user: [],
  selectedItemId: null,
        updatedItem: {
          id: null,
          name: "",
          description: "",
          isLoanedOut: false
        },
        createdItem: {
          id: null,
          name: "",
          description: "",
          isLoanedOut: false
        },
  }
},

watch: {
    searchQuery: function(newQuery) {
      this.filterItems(newQuery);
    }
  },

mounted() {
  this.getItems();
  this.getUser();
  this.filterItems(this.searchQuery);
  // this.users = [];
},

 methods: {
  // Gets All Items
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
  filterItems(query) {
      if (query === '') {
        this.filteredItems = this.items; // Show all items if query is empty
      } else {
        const lowerCaseQuery = query.toLowerCase().trim();
        this.filteredItems = this.items.filter(item => {
          return item.name.toLowerCase().includes(lowerCaseQuery);
        });
      }
    },

  executeSearch() {
    this.filterItems(this.searchQuery);
    },

    
    // Get Logged In User
    getUser() {
      fetch(`http://localhost:9000/users/token/${localStorage.getItem('authToken')}`)
      .then(response => response.json())
      .then(data => {
        this.user = data;
        console.log(data);
      })
      .catch(error => {
        console.error(error);
      });
  },
  
  // Lend out an item
  loanItem(userId, itemId){
    const token = localStorage.getItem('authToken');
    fetch('http://localhost:9000/loans', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `${token}`
      },
      body: JSON.stringify({userId: userId, itemId: itemId}),
    })
      .then(response => {
        if (response.ok) {
          console.log("SUCCES" + response)
          return response.text();
        } else {
          throw new Error(`Invalid credentials ${userId} ${itemId} ${token}`);
        }
      })
      .catch(error => {
        console.error(error);
      });
    },
    
  createItem() {
    fetch('http://localhost:9000/items', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.createdItem),
    })
        .then(response => {
            if (response.ok) {
              this.items.push(this.createdItem)
              console.log(`Item With Name ${this.createdItem.name} Created`)
            } else {
                throw new Error(`Could not create item. ${this.createdItem} `);
            }
        })
        .catch(error => {
            console.error(error);
    });
  },

  // Delete Item
  // deleteItem(itemId) {
  //   fetch(`http://localhost:9000/items/${itemId}`, {
  //     method: 'DELETE',
  //     headers: {
  //       'Content-Type': 'application/json',
  //     },
  //   })
  //     .then(response => {
  //       console.log(`Item With ID ${itemId} Deleted`)
  //       if (response.ok) {
  //       const index = this.items.findIndex(item => item.id === itemId);
  //       if (index !== -1) {
  //         this.items.splice(index, 1);
  //       }
  //     } else {
  //       console.error('Item deletion failed: Item may be Loaned Out');
  //     }
  //   }).catch(error => {
  //       console.error(error);
  //     });
  // },

  // Edit Item
  editItem(item) {
      this.selectedItemId = item.id;
      this.updatedItem = { ...item };
      console.log("response OK")
    },

  // updateItem() {
  //   fetch(`http://localhost:9000/items/update/${this.selectedItemId}`, {
  //     method: 'PUT',
  //     headers: {
  //       'Content-Type': 'application/json',
  //     },
  //     body: JSON.stringify(this.updatedItem)
  //   })
  //     .then(response => {
  //       if (response.ok) {
  //           console.log("response OK")
  //           const index = this.items.findIndex(item => item.id === this.updatedItem.id);
  //           if (index !== -1) {
  //             this.items.splice(index, 1, this.updatedItem);
  //           }
  //           this.cancelEdit();
  //         } else {
  //           console.error('Item update failed.');
  //         }
  //       })
  //       .catch(error => {
  //         console.error(error);
  //       });
  // },
  
  cancelEdit() {
      this.selectedItemId = null;
      this.updatedItem = {
        id: null,
        name: "",
        description: "",
        loanStatus: false
      };

 },
},

 updated() {
  
//  console.log(this.items);
}
 }


</script>

<template>
  <nav>
    <router-link to="/admin" v-if="user && user.role === 'admin'">
      Dashboard
    </router-link>
  </nav>
  <h2>Items overview</h2>
  <div>
        <input type="text" v-model="createdItem.name" placeholder="Name" />
        <input type="text" v-model="createdItem.description" placeholder="Description" />
        <input type="hidden" v-model="createdItem.isLoanedOut" />
        <button @click="createItem()">Create item</button>
      </div>
  <div class="search-container">
    <input class="search-input" type="text" v-model="searchQuery" placeholder="Search item" />
  </div>
    <div class="item">
          <ul>
            <div v-if="searchQuery">
                <li v-for="item in filteredItems" :key="item">
                  <div class="item-list">
                    <div class="item-list-info">
                      <h3>{{ item.name }}</h3>
                      <p>{{ item.description }}</p>
                      <span class="unavailable" v-if="item.isLoanedOut">Unavailable</span>
                      <span class="available" v-else>Available</span>
                    </div>
                    <div class="item-list-buttons">
                      <button v-if="!item.isLoanedOut" class="loan-button" @click="loanItem(user.id, item.id)">Lend Out</button>
                      <span v-else></span>
                    </div>
                  </div>
                  <!-- <hr> -->
              </li>
            </div>
            <div v-else>
              <li v-for="item in items" :key="item">
                <div class="item-list">
                  <div class="item-list-info">
                    <h3>{{ item.name }}</h3>
                    <p>{{ item.description }}</p>
                    <span class="unavailable" v-if="item.isLoanedOut">Unavailable</span>
                    <span class="available" v-else>Available</span>
                  </div>
                  <div class="item-list-buttons">
                    <button v-if="!item.isLoanedOut" class="loan-button" @click="loanItem(user.id, item.id)">Lend Out</button>
                    <span v-else></span>
                  </div>
                </div>
                  <!-- <hr> -->
              </li>
            </div>

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

  .search-input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-button {
  padding: 10px 15px;
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.item{
  margin-top: 20px;
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

.update-button {
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

.update-button:hover {
  background-color: #a90000;
}

.edit-form {
  background-color: #f8f8f8;
  padding: 10px;
  margin-top: 10px;
  border-radius: 5px;
}

.edit-form input,
.edit-form textarea {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.edit-form .form-buttons {
  display: flex;
  justify-content: flex-end;
}

.edit-form .form-buttons button {
  margin-left: 10px;
}

.save-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 3px;
  cursor: pointer;
}

.loan-button {
  background-color: white;
  color: black;
  border: black solid 1px;
  padding: 8px 12px;
  border-radius: 30px;
  cursor: pointer;
}

.loan-button:hover {
  background-color: rgba(0, 128, 0, 0.531);
  transform: scale(1.1);
}

.unavailable {
  color: red;
  font-weight: bold;
}

.available {
  color: green;
  font-weight: bold;
}

li {
  /* display: flex; */
  /* width: 100%; */
  margin-bottom: 20px;
  border-bottom: 1px solid black;
}

.item-list,
.user-list,
.loan-list{
  display: flex;
  width: 100%;
  /* margin-bottom: 20px; */
}

.item-list-info,
.user-list-info,
.loan-list-info {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.item-list-buttons,
.user-list-buttons,
.loan-list-buttons {
  margin-left: auto;
  margin-top: auto;
  /* width: 50%;
  top: 50%; */
}

.user-list-buttons button,
.item-list-buttons button,
.loan-list-buttons button {
  margin-bottom: 1em;
}
  </style>