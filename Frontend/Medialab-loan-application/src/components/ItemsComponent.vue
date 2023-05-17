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

    createItem(createdItem) {
            fetch('http://localhost:9000/items', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(createdItem),
            })
                .then(response => {
                    if (response.ok) {
                      this.items.push(createdItem)
                        console.log("Item creation OK")
                    } else {
                        throw new Error('Could not create item.');
                    }
                })
                .catch(error => {
                    console.error(error);
           });
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

  // Delete Item
  deleteItem(itemId) {
    fetch(`http://localhost:9000/items/${itemId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(response => {
        if (response.ok) {
        const index = this.items.findIndex(item => item.id === itemId);
        if (index !== -1) {
          this.items.splice(index, 1);
        }
      } else {
        console.error('Item deletion failed: Item may be Loaned Out');
      }
    }).catch(error => {
        console.error(error);
      });
  },

  // Edit Item
  editItem(item) {
      this.selectedItemId = item.id;
      this.updatedItem = { ...item };
      console.log("response OK")
    },

  updateItem() {
    fetch(`http://localhost:9000/items/update/${this.selectedItemId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(this.updatedItem)
    })
      .then(response => {
        if (response.ok) {
            console.log("response OK")
            const index = this.items.findIndex(item => item.id === this.updatedItem.id);
            if (index !== -1) {
              this.items.splice(index, 1, this.updatedItem);
            }
            this.cancelEdit();
          } else {
            console.error('Item update failed.');
          }
        })
        .catch(error => {
          console.error(error);
        });
  },
  cancelEdit() {
      this.selectedItemId = null;
      this.updatedItem = {
        id: null,
        name: "",
        description: "",
        loanStatus: false
      };

 },
 // this doesnt get updated through the updateItem method but through the create loan method
 setLoanStatus(isLoanedOut) {
      this.updatedItem.isLoanedOut = isLoanedOut;
    }
},

 updated() {
  
 console.log(this.items);
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
        <button @click="createItem">Create item</button>
      </div>
  <div class="search-container">
    <input class="search-input" type="text" v-model="searchQuery" placeholder="Search item" />
  </div>
    <div class="item">
          <ul>
            <div v-if="searchQuery">
                <li v-for="item in filteredItems" :key="item">
                <h3>{{ item.name }}</h3>
                <p>{{ item.description }}</p>
                <span class="unavailable" v-if="item.isLoanedOut">Unavailable</span>
                <span class="available" v-else>Available</span>
                <div v-if="user && user.role === 'admin'">
                <button class="update-button" @click="editItem(item)">Update</button>
                <div v-if="item.id === selectedItemId" class="edit-form">
                  <input v-model="updatedItem.name" type="text" placeholder="Name" />
                  <textarea v-model="updatedItem.description" placeholder="Description"></textarea>
        
                  <div class="form-buttons">
                  <button class="save-button" @click="updateItem">Save</button>
                  <button class="cancel-button" @click="cancelEdit">Cancel</button>
                  </div>
                  </div>
                <button class="delete-button" @click="deleteItem(item.id)">Delete</button>
              </div>
                <button class="delete-button" @click="loanItem(user.id, item.id)">Lend Out</button>
                <hr>
              </li>
            </div>
            <div v-else>
              <li v-for="item in items" :key="item">
                <h3>{{ item.name }}</h3>
                <p>{{ item.description }}</p>
                <span class="unavailable" v-if="item.isLoanedOut">Unavailable</span>
                <span class="available" v-else>Available</span>
                <div v-if="user && user.role === 'admin'">
                <button class="update-button" @click="editItem(item)">Update</button>
                <div v-if="item.id === selectedItemId" class="edit-form">
                  <input v-model="updatedItem.name" type="text" placeholder="Name" />
                  <textarea v-model="updatedItem.description" placeholder="Description"></textarea>
          
                  <div class="form-buttons">
                  <button class="save-button" @click="updateItem">Save</button>
                  <button class="cancel-button" @click="cancelEdit">Cancel</button>
                  </div>
                  </div>
                <button class="delete-button" @click="deleteItem(item.id)">Delete</button>
              </div>
                <button class="delete-button" @click="loanItem(user.id, item.id)">Lend Out</button>
                <hr>
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

.cancel-button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 3px;
  cursor: pointer;
}

.unavailable {
  color: red;
  font-weight: bold;
}

.available {
  color: green;
  font-weight: bold;
}
  </style>