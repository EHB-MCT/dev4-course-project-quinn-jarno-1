<script>

export default {

data() {

return {

 items: [],
 selectedItemId: null,
      updatedItem: {
        id: null,
        name: "",
        description: "",
        isLoanedOut: false
      }

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
        if (response.ok) {
        const index = this.items.findIndex(item => item.id === itemId);
        if (index !== -1) {
          this.items.splice(index, 1);
        }
      } else {
        console.error('Item deletion failed.');
      }
    }).catch(error => {
        console.error(error);
      });
  },

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
  <h2>Items overview</h2>
    <div class="item">
          <ul>
            <li v-for="item in items" v-bind:key="item">
              <h3>{{ item.name }}</h3>
              <p>{{ item.description }}</p>
              <span v-if="item.isLoanedOut">Reserved: True</span>
              <span v-else>Reserved: False</span>
              <button class="update-button" @click="editItem(item)">Update</button>
              <div v-if="item.id === selectedItemId" class="edit-form">
                <input v-model="updatedItem.name" type="text" placeholder="Name" />
                <textarea v-model="updatedItem.description" placeholder="Description"></textarea>
                <div class="loan-status">
        <button
          :class="{ active: updatedItem.isLoanedOut }"
          @click="setLoanStatus(true)"
        >
          Loaned Out
        </button>
        <button
          :class="{ active: !updatedItem.isLoanedOut }"
          @click="setLoanStatus(false)"
        >
          Available
        </button>
      </div>
                <div class="form-buttons">
                <button class="save-button" @click="updateItem">Save</button>
                <button class="cancel-button" @click="cancelEdit">Cancel</button>
                </div>
                </div>
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
  </style>