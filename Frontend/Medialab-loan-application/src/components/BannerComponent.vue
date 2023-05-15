<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="greetings">
    <h1 class="red">{{ msg }}</h1>
    <h3>
      Check out the available items:
    </h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: left;
}

.greetings{
  margin-left: 5%;
  margin-top: 7%;
  width: 1000px;
}


</style>
