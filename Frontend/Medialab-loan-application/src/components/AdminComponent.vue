<script>

export default {

data() {
  return {
    showAllItems: false,
    showAllUsers: false,
    showAllLoans: false,
    items: [],
    loanedItems: [],
    users: [],
    user: [],
    selectedItemId: null,
    selectedDeleteItemId: null,
    selectedUserId: null,
    selectedDeleteUserId: null,
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
    updatedUser: {
      id: null,
      username: "",
      password: "",
      email: "",
      role: ""
    },
  }
},

mounted() {
  this.getItems();
  this.getUsers();
  this.getUser();
  this.getLoans();
},

methods: {
  toggleAllItems() {
    this.showAllItems = !this.showAllItems; 
  },
  toggleAllUsers() {
    this.showAllUsers = !this.showAllUsers; 
  },
  toggleAllLoans() {
    this.showAllLoans = !this.showAllLoans; 
  },

  // Gets All Items
  getItems() {
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

  getUsers() {
    fetch('http://localhost:9000/users')
      .then(response => response.json())
      .then(data => {
        this.users = data;
        console.log(data);
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

  getLoans() {
    fetch('http://localhost:9000/loans')
      .then(response => response.json())
      .then(data => {
        this.loanedItems = data;
        console.log(data);
      })
      .catch(error => {
        console.error(error);
      });
  },
  
  returnItem(loanId, loan) {
    const newLoan = {
      id: loan.id,
      loanDate: loan.loanDate,
      dueDate: loan.dueDate,
      user: loan.user,
      item: loan.item,
      returned: true
    }
    
    fetch(`http://localhost:9000/loans/return/${loanId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ loanId: loanId}),
    })
      .then(response => {
        console.log(response);
        if (response.ok) {
          const index = this.loanedItems.findIndex(loan => loan.id === loanId);
          console.log(index)
          console.log(loan)
          if (index !== -1) {
            this.loanedItems.splice(index, 1, newLoan);
          }
          console.log("SUCCES" + response)
          return response.text();
          
        } else {
          throw new Error(response);
        }
      })
      .catch(error => {
        console.error(error);
      });
  },

  // Delete Item
  deleteItemModal(item) {
    this.selectedDeleteItemId = item.id;
    this.updatedItem = { ...item };
    console.log(`${this.selectedDeleteItemId}`)
  },
    
  deleteItem(itemId) {
    console.log(itemId)
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

  cancelItemDelete() {
    this.selectedDeleteItemId = null;
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
          this.cancelItemUpdate();
        } else {
          console.error('Item update failed.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  },

  cancelItemUpdate() {
    this.selectedItemId = null;
    this.updatedItem = {
      id: null,
      name: "",
      description: "",
      loanStatus: false
    };

  },

  // Create Item
  // Need to refresh page to be able to edit it. doenst have id yet so need to fetch again from back end.
  createItem() {
    const newItem = {
      name: this.createdItem.name,
      description: this.createdItem.description,
      isLoanedOut: this.createdItem.isLoanedOut
    }
    fetch('http://localhost:9000/items', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newItem),
    })
      .then(response => {
        if (response.ok) {
          this.items.push(newItem);
          console.log(`Item With ID & Name ${newItem.id} ${newItem.name} Created`)
        } else {
          throw new Error(`Could not create item. ${this.createdItem} `);
        }
      })
      .catch(error => {
        console.error(error);
      });
  },

  // DELETE USER
  deleteUserModal(user){
    this.selectedDeleteUserId = user.id;
    this.updatedUser = { ...user };
    console.log(`${this.selectedDeleteUserId}`)
  },

  deleteUser(userId) {
    fetch(`http://localhost:9000/users/delete/${userId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userId: userId }),
    })
      .then(response => {
        console.log(`clicked ${userId}`)
        if (response.ok) {
          const index = this.users.findIndex(user => user.id === userId);
          if (index !== -1) {
            this.users.splice(index, 1);
          }
        } else {
          console.error('User deletion failed');
        }
      }).catch(error => {
        console.error(error);
      });
  },

  cancelUserDelete() {
    this.selectedDeleteUserId = null;
  },

  //USER EDIT
  editUser(user) {
    this.selectedUserId = user.id;
    this.updatedUser = { ...user };
    console.log(`${this.selectedUserId}`)
  },

  updateUser() {
    fetch(`http://localhost:9000/users/update/${this.selectedUserId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(this.updatedUser)
    })
      .then(response => {
        console.log("response OK")
        if (response.ok) {
          const index = this.users.findIndex(user => user.id === this.updatedUser.id);
          if (index !== -1) {
            this.users.splice(index, 1, this.updatedUser);
            
          }
          this.cancelUserUpdate();
        } else {
          console.error('User update failed.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  },
  
  cancelUserUpdate() {
    this.selectedUserId = null;
    this.updatedUser = {
      id: null,
      username: "",
      password: "",
      email: "",
      role: ""
    };

  },
  
  updateRole(userId) {
    fetch(`http://localhost:9000/users/updateRole/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(this.updatedUser)
    })
      .then(response => {
        if (response.ok) {
          console.log("response OK")
          const index = this.users.findIndex(user => user.id === this.updatedUser.id);
          console.log(index)
          if (index !== -1) {
            this.users.splice(index, 1, this.updatedUser);
          }
          this.cancelUserUpdate();
        } else {
          console.error('Role change failed.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  },
},

updated() {

}
}


</script>

<template>
<nav>
    <router-link to="/items">
      Back
    </router-link>
  </nav>

  <div v-if="user && user.role === 'admin'">
    
    <div class="item">
      <h2 class="clickable-title" @click="toggleAllItems">Items overview</h2>
      <div v-show="showAllItems">
      <div class="createItem">
        <input type="text" v-model="createdItem.name" placeholder="Name" class="input-field"/>
        <input type="text" v-model="createdItem.description" placeholder="Description" class="input-field" />
        <input type="hidden" v-model="createdItem.isLoanedOut" />
        <div class="align-create-button">
          <button class="create-button" @click="createItem()">Create item</button>
        </div>
      </div>
      <ul>
        <li v-for="item in items" v-bind:key="item">
          <div class="item-list">
            <div class="item-list-info">
              <h3>{{ item.name }}</h3>
              <p>{{ item.description }}</p>
              <span class="unavailable" v-if="item.isLoanedOut">Unavailable</span>
              <span class="available" v-else>Available</span>
            </div>
  
            <div class="item-list-buttons">            
              <button class="modal-button" @click="deleteItemModal(item)">Delete</button>
              <button class="update-button" @click="editItem(item)">Update</button>
            </div>
          </div>
          
          <!-- Edit Form -->
          <div v-if="item.id === selectedItemId" class="edit-form">
            <input v-model="updatedItem.name" type="text" placeholder="Name" />
            <textarea v-model="updatedItem.description" placeholder="Description"></textarea>
            <div class="form-buttons">
              <button class="save-button" @click="updateItem">Save</button>
              <button class="cancel-button" @click="cancelItemUpdate">Cancel</button>
            </div>
          </div>

          <!-- Delete -->
          <div  v-if="item.id === selectedDeleteItemId" class="modal">
            <div class="modal-content">
              <h1>Are you sure you want to delete this item?</h1>
              <button class="modal-cancel-button" @click="cancelItemDelete()">Cancel</button>
              <button class="delete-button" @click="deleteItem(item.id)">Delete</button>
            </div>
          </div>

        </li>
      </ul>
    </div>
    </div>
  
    
    <div class="item">
      <h2 class="clickable-title" @click="toggleAllUsers">Users overview</h2>
      <div v-show="showAllUsers">
      <ul>

        <li v-for="uniqueUser in users" :key="user">
          <div class="user-list">
            <div class="user-list-info">
              <div class="user-list-creds">
                <h3>{{ uniqueUser.username }}</h3>
                <p>Email: {{ uniqueUser.email }}</p>
              </div>  
                <p>Role: {{ uniqueUser.role }}</p>
            </div>
            <div class="user-list-buttons">
              <button class="modal-button" @click="deleteUserModal(uniqueUser)">Delete</button>
              <button class="update-button" @click="editUser(uniqueUser)">Edit</button>
            </div>
          </div>

          <div v-if="uniqueUser.id === selectedDeleteUserId" class="modal">
            <div class="modal-content">
              <h1>Are you sure you want to delete this user?</h1>
              <button class="modal-cancel-button" @click="cancelUserDelete()">Cancel</button>
              <button class="delete-button" @click="deleteUser(uniqueUser.id)">Delete</button>
            </div>
          </div>

          <!-- User Edit Form -->
          <div v-if="uniqueUser.id === selectedUserId" class="edit-form">
            <input v-model="updatedUser.username" type="text" placeholder="Username" />
            <textarea v-model="updatedUser.email" placeholder="Email"></textarea>
            <label>Select Role</label>
            <select v-model="updatedUser.role" id="role" >
              <option value="admin">Admin</option>
              <option value="user">User</option>
            </select>
            <div class="form-buttons">
              <button class="update-button" @click="updateRole(uniqueUser.id)">Change Role</button>
              <button class="save-button" @click="updateUser()">Save</button>
              <button class="cancel-button" @click="cancelUserUpdate()">Cancel</button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    </div>
  
    <div class="item">
      <h2 class="clickable-title" @click="toggleAllLoans">Loans overview</h2>
      <div v-show="showAllLoans">
      <ul>
        <li v-for="loan in loanedItems" v-bind:key="loan">
          <div class="loan-list">
            <div class="loan-list-info">
              <h3>Loan: {{ loan.id }}</h3>
              <p> Item: {{loan.item.name }}</p>
              <p> User: {{ loan.user.username }} with ID: {{ loan.user.id }}</p>
              <h4> Loan Date: {{ loan.loanDate }}</h4>
              <h4> Due Date: {{ loan.dueDate }}</h4>
              <p class="bold-text" v-if="loan.returned">Item has been returned</p>
              <p v-else>Item is Still Loaned Out</p>
            </div>
            <div class="loan-list-buttons">
              <span v-if="loan.returned">
              </span>
              <span v-else>
                <button class="return-button" @click="returnItem(loan.id, loan)">Return Item</button>
              </span>
              <h2><span class="loan-ended" v-if="loan.returned">ENDED</span><span class="loan-active" v-else>ACTIVE</span></h2>

            </div>
          </div>
        </li>
      </ul>
    </div>

    </div>

  </div>
  <div v-else>
    <p>You are not an admin.</p>
  </div>
</template>

<style>
.createItem {
  display: flex;
  flex-direction: column;
  align-items: left;
  margin-bottom: 20px;
}

.input-field {
  margin-bottom: 20px;
  padding: 10px;
  width: 200px;
}

.create-button {
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  width: 150px;
  margin-left: 6px;
}

.create-button:hover {
  background-color: #45a049;
}

.align-create-button {
  margin-top: 20px;
  text-align: right;
}

.bold-text{
  font-weight: bold;
}

.loan-ended { 
  font-weight: bold;
  color: red;
}

.loan-active{
  font-weight: bold;
  color: green;
  float: right;
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

.loan-list-buttons {
  display: flex;
  flex-direction: column;
}

.user-list-buttons button,
.item-list-buttons button,
.loan-list-buttons button {
  margin-bottom: 1em;
}

.user-list-creds {
  display: flex;
  width: 100%;
}

.user-list-creds p{
  margin-left: 10px;
  margin-bottom: 0;
  top: 20%;
}

.clickable-title {
  cursor: pointer;
  transition: background-color 0.3s;
}

.clickable-title:hover {
  background-color: lightgray;
}

.clickable-title:active {
  background-color: darkgray;
}
ul {
  list-style-type: none;
  padding: 0;
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

h4 {
  margin-bottom: 10px;
}

p {
  margin-bottom: 10px;
}

span {
  font-weight: lighter;
  margin-bottom: 5px;
}

.modal-cancel-button,
.modal-button,
.return-button,
.delete-button,
.update-button,
.save-button,
.cancel-button {
  /* background-color: hsl(0, 0%, 0%); */
  color: rgb(255, 255, 255);
  border: none;
  padding: 8px 16px;
  margin-left: 3px;
  margin-bottom: -10px;
  border-radius: 4px;
  cursor: pointer;
  float: right;
}

/* .modal-button:hover,
.return-button:hover,
.delete-button:hover,
.update-button:hover {
  background-color: #a90000;
} */

.modal-cancel-button,
.update-button,
.save-button,
.return-button {
  background-color: rgb(24, 105, 255);
}

.modal-cancel-button:hover,
.update-button:hover,
.save-button:hover,
.return-button:hover {
  background-color: rgb(24, 105, 255, 0.483);
} 

.modal-button,
.delete-button,
.cancel-button {
  background-color: red;
}

.modal-button:hover,
.delete-button:hover,
.cancel-button:hover {
  background-color: rgba(255, 0, 0, 0.483);
} 

.form-buttons {
  margin-bottom: 20px;
}

.loan {
  background-color: #f8f8f8;
  padding: 10px;
  margin-top: 10px;
  border-radius: 5px;
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

.unavailable {
  color: red;
  font-weight: bold;
}

.available {
  color: green;
  font-weight: bold;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.132); 
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>