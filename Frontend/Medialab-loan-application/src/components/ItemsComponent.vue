<script>
export default {
data() {
  return {
  searchQuery: '',
  showLoanForm: false,
  showEndedLoans: false,
  loans: [],
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
  loanItem(userId, itemId, item){
    const newItem = {
        id: item.id,
        name: item.name,
        description: item.description,
        isLoanedOut: true
    }
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
          const index = this.items.findIndex(item => item.id === itemId);
          console.log(index)
          console.log(newItem)
          if (index !== -1) {
            this.items.splice(index, 1, newItem);
          }
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

  openLoanForm(user) {
    const userId = this.user.id; 
    fetch(`http://localhost:9000/loans/${userId}`)
      .then(response => response.json())
      .then(data => {
        this.loans = data;
        console.log(data);
      })
      .catch(error => {
        console.error(error);
      });

    this.showLoanForm = !this.showLoanForm;
  },

  toggleEndedLoans(){
    this.showEndedLoans = !this.showEndedLoans
  },

  },

  updated() {
  }
}


</script>

<template>
  <nav>
    <router-link to="/admin" v-if="user && user.role === 'admin'">
      Dashboard
    </router-link>
  </nav>

    <h3 class="clickable-title" @click="openLoanForm">View My Loans</h3>
    <div v-show="showLoanForm" class="loan-form">
      <h2>My Loans</h2>

      <h3 class="clickable-title" @click="toggleEndedLoans">View Your Loan History</h3>
      
      <ul>
        <li v-for="loan in loans" :key="loan">
          <div v-show="showEndedLoans">
              <div class="my-loans" v-if="loan.returned">    
                  <h3>{{ loan.item.name }}</h3>
                  <p> <i>{{ loan.item.description }}</i> </p>
                  <h4> Loan Date: {{ loan.loanDate }}</h4>
                  <h4> Due Date: {{ loan.dueDate }}</h4>
                  <p class="green-text" v-if="loan.returned">Item has been returned</p>
                  <p class="red-text" v-if="!loan.returned">Item not yet returned</p>
              </div>
          </div>

          <div class="my-loans" v-if="!loan.returned">          
            <h3>{{ loan.item.name }}</h3>
            <p> <i>{{ loan.item.description }}</i> </p>
            <h4> Loan Date: {{ loan.loanDate }}</h4>
            <h4> Due Date: <span class="bold-text">{{ loan.dueDate }}</span></h4>
            <p class="green-text" v-if="loan.returned">Item has been returned</p>
            <p class="red-text" v-if="!loan.returned">Item not yet returned</p>
          </div>
        </li>
      </ul>

    </div>

  <h2 class="items-overview">Items overview</h2>
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
                      <button v-if="!item.isLoanedOut" class="loan-button" @click="loanItem(user.id, item.id, item)">Lend Out</button>
                      <span v-else></span>
                    </div>
                  </div>
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
                    <button v-if="!item.isLoanedOut" class="loan-button" @click="loanItem(user.id, item.id, item)">Lend Out</button>
                  </div>
                </div>
              </li>
            </div>

          </ul>
    </div>
    
</template>
<style>
nav {
 margin-bottom: 1em;
}

ul {
  list-style-type: none;
  padding: 0;
}
  
h2 {
  font-size: 40px;
  margin-bottom: 0;
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

.loanButton {
background-color: #ffffff;
color: rgb(0, 128, 255);
border: none;
padding: 10px 20px;
padding-left: 0;
text-align: center;
text-decoration: none;
display: inline-block;
font-size: 15px;
cursor: pointer;
border-radius: 4px;
font-weight: bold;
}

.loanClose{
  background-color: #af0000;
  color: rgb(255, 255, 255);
  border: none;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 15px;
  cursor: pointer;
  border-radius: 4px;
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


.item-list,
.my-loans {
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
}

.user-list-buttons button,
.item-list-buttons button,
.loan-list-buttons button {
  margin-bottom: 1em;
}

.bold-text{
  font-weight: bold;
}

.green-text{
  color: green;
  font-weight: bold;
}
.red-text{
  color: red;
  font-weight: bold;
}

.items-overview{
  text-align: left;
  width: 100%;
  margin-bottom: 20px;
}

.loan-form h2 {
  text-align: left;
  width: 100%;
  margin-bottom: 0;
}

</style>